package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.Tenor;
import com.pyruby.marketdata.serializer.BondRepresentation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.pyruby.marketdata.BondBuilder.newBond;
import static com.pyruby.marketdata.BondBuilder.newTenor;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MarketDataServiceTest {
    private MarketDataRepository repo;
    private MarketDataService svc;

    @Before
    public void setUp() {
        repo = mock(MarketDataRepository.class);
        svc = new MarketDataServiceImpl(repo);
    }
    
    @Test
    public void storeYieldCurve_marshalsXmlRequestIntoDomainObjectBeforeCallingPersistenceLayer() {
        BondRepresentation bondRepr = newBond()
                .name("British Petrolium")
                .ticker("LON:BP")
                .maturity("5Y")
                .issuer("UK Government")
                .withTenors(newTenor("3m", 40d)
                        .newTenor("6m", 42.3d)
                        .newTenor("9m", 42.3d)
                        .newTenor("1Y", 45d)
                        .newTenor("2Y", 47.3d)
                        .newTenor("3Y", 49.8d)
                        .newTenor("4Y", 52.3d)
                        .newTenor("5Y", 55.3d)
                ).createRepresentation();
        when(repo.save(isA(Bond.class))).thenAnswer(new Answer<Bond>() {
            public Bond answer(InvocationOnMock invocationOnMock) throws Throwable {
                Bond bond = (Bond) invocationOnMock.getArguments()[0];
                bond.setId(123L);
                return bond;
            }
        });
        ArgumentCaptor<Bond> capture = ArgumentCaptor.forClass(Bond.class);

        svc.storeBond(bondRepr);

        verify(repo).save(capture.capture());
        assertNotNull(capture.getValue());

        Bond bond = capture.getValue();

        assertEquals(8, bond.getTenors().size());
        assertEquals("British Petrolium", bond.getName());
        assertEquals("LON:BP", bond.getTicker());
        assertEquals("5Y", bond.getMaturity());
        assertEquals("UK Government", bond.getIssuer());
        assertEquals(new Tenor("3m", 40.0), bond.getTenors().get(0));
        assertEquals(123L, bond.getId());
    }

    @Test
    public void findBondByNameAndMaturity_returnsNull_givenNoCorrespondingBond() {
        MarketDataRepository repo = mock(MarketDataRepository.class);
        MarketDataService svc = new MarketDataServiceImpl(repo);

        assertNull(svc.findBondByNameAndMaturity("NoSuchBond","2Y"));
    }

    @Test
    public void findBondByNameAndMaturity_returnsBondRepresentation_givenAStoredBond() {
        Bond bond = newBond().name("Yahoo").maturity("3Y").createBond();
        MarketDataRepository repo = mock(MarketDataRepository.class);
        MarketDataService svc = new MarketDataServiceImpl(repo);
        when(repo.findByNameAndMaturity("Yahoo", "3Y")).thenReturn(bond);

        BondRepresentation found = svc.findBondByNameAndMaturity("Yahoo", "3Y");

        assertEquals(bond.getName(), found.getName());
        assertEquals(bond.getMaturity(), found.getMaturity());
    }

}
