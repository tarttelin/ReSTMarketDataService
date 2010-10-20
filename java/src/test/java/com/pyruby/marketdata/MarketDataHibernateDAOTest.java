package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.Tenor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

        Bond foundBond = repo.findByNameAndMaturity(bond.getName(), bond.getMaturity());
        assertEquals(bond.getName(), foundBond.getName());
        assertEquals(bond.getMaturity(), foundBond.getMaturity());
        assertEquals(bond.getTenors().size(), foundBond.getTenors().size());
    }

    private Bond createBond() {
        Bond bond = new Bond();
        bond.setIssuer("British Petrolium");
        bond.setMaturity("5Y");
        bond.setName("British Petrolium Ltd");
        bond.setTicker("LON:BP");

        bond.setTenors(Arrays.asList(new Tenor("3m", 35.7), new Tenor("6m", 40.2), new Tenor("1Y", 54.76)));
        return bond;
    }
}
