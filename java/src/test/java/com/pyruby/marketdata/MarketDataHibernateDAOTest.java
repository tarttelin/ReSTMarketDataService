package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.LiborCurve;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import static com.pyruby.marketdata.CurveBuilder.newBond;
import static com.pyruby.marketdata.CurveBuilder.newLiborCurve;
import static com.pyruby.marketdata.CurveBuilder.newTenor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class MarketDataHibernateDAOTest extends MarketDataIntegrationTestCase {
    private MarketDataRepository repo;

    @Before
    public void setUp() {
        repo = super.applicationContext.getBean("marketDataRepository", MarketDataRepository.class);

    }

    @Test
    @Rollback
    public void store_storesBondWithItsTenors() {
        Bond bond = createBond();
        repo.save(bond);

        assertFalse(bond.getId() == 0); 
    }

    @Test
    @Rollback
    public void findByNameAndMaturity_returnsASingleMatch_givenAnExistingEntry() {
        Bond bond = createBond();
        repo.save(bond);
        Bond bond2 = createBond();
        bond2.setMaturity("3Y");
        repo.save(bond2);

        Bond foundBond = repo.findBondByNameAndMaturity(bond2.getName(), bond2.getMaturity());
        assertEquals(bond2.getName(), foundBond.getName());
        assertEquals(bond2.getMaturity(), foundBond.getMaturity());
        assertEquals(bond2.getTenors().size(), foundBond.getTenors().size());
    }

    private Bond createBond() {
        return newBond()
                .issuer("British Petrolium")
                .maturity("5Y")
                .name("British Petrolium Ltd")
                .ticker("LON:BP")
                .withTenors(newTenor("3m", 35.7)
                .newTenor("6m", 40.2)
                .newTenor("1Y", 54.76))
                .createCurve();
    }
    
    @Test
    @Rollback
    public void store_storesLiborCurveWithItsTenors() {
        LiborCurve libor = newLiborCurve()
                .name("EURIBOR")
                .currency("EURO")
                .withTenors(newTenor("3m", 12)
                    .newTenor("6m", 12.25)
                    .newTenor("1Y", 12.75)
                ).createCurve();
        repo.save(libor);

        assertFalse(libor.getId() == 0);
    }

    @Test
    @Rollback
    public void findLiborCurveByName_returnsLiborCurve_givenAnExistingLiborCurve() {
        LiborCurve curve = newLiborCurve().name("TEST.JPY").createCurve();
        repo.save(curve);

        LiborCurve match = repo.findLiborCurveByName(curve.getName());

        assertNotNull(match);
        assertEquals(curve.getName(), match.getName());
        assertEquals(curve.getCurrency(), match.getCurrency());
        assertEquals(curve.getTenors().size(), match.getTenors().size());
    }
}
