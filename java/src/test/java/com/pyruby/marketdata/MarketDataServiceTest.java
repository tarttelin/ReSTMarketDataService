package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.Tenor;
import com.pyruby.marketdata.serializer.BondRepresentation;
import com.pyruby.marketdata.serializer.TenorRepresentation;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class MarketDataServiceTest {

    @Test
    public void storeYieldCurve_marshalsXmlRequestIntoDomainObjectBeforeCallingPersistenceLayer() {
        
        BondRepresentation bondRepr = new BondRepresentation();
        bondRepr.setName("British Petrolium");
        bondRepr.setTicker("LON:BP");
        bondRepr.setMaturity("5Y");
        bondRepr.setIssuer("British Petrolium");
        bondRepr.addTenor(createTenor("3m", 40D));
        bondRepr.addTenor(createTenor("6m", 40D));
        bondRepr.addTenor(createTenor("9m", 40D));
        bondRepr.addTenor(createTenor("1Y", 40D));
        bondRepr.addTenor(createTenor("2Y", 160D));
        bondRepr.addTenor(createTenor("3Y", 160D));
        bondRepr.addTenor(createTenor("4Y", 160D));
        bondRepr.addTenor(createTenor("5Y", 160D));
        MarketDataRepository repo = mock(MarketDataRepository.class);
        MarketDataService svc = new MarketDataServiceImpl(repo);
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
        assertEquals("British Petrolium", bond.getIssuer());
        assertEquals(new Tenor("3m", 40.0), bond.getTenors().get(0));
        assertEquals(123L, bond.getId());
    }

    private TenorRepresentation createTenor(String period, double bps) {
        TenorRepresentation tenorRepr = new TenorRepresentation();
        tenorRepr.setPeriod(period);
        tenorRepr.setBasisPoints(bps);
        return tenorRepr;
    }

}
