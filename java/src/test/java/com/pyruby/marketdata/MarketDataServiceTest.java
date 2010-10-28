package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.LiborCurve;
import com.pyruby.marketdata.model.Tenor;
import com.pyruby.marketdata.serializer.BondRepresentation;
import com.pyruby.marketdata.serializer.LiborCurveRepresentation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static com.pyruby.marketdata.CurveBuilder.newBond;
import static com.pyruby.marketdata.CurveBuilder.newLiborCurve;
import static com.pyruby.marketdata.CurveBuilder.newTenor;
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
    public void storeBond_convertsRepresentationIntoDomainObjectBeforeCallingPersistenceLayer() throws MarketDataServiceException {
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


    @Test(expected=MarketDataServiceException.class)
    public void storeBond_throwsAMarketDataServiceException_givenAnInvalidBond() throws MarketDataServiceException {
        MarketDataRepository repo = mock(MarketDataRepository.class);
        MarketDataService svc = new MarketDataServiceImpl(repo);

        svc.storeBond(newBond().name(null).createRepresentation());        
    }

    @Test
    public void findBondByNameAndMaturity_returnsNull_givenNoCorrespondingBond() {
        MarketDataRepository repo = mock(MarketDataRepository.class);
        MarketDataService svc = new MarketDataServiceImpl(repo);

        assertNull(svc.findBondByNameAndMaturity("NoSuchBond","2Y"));
    }

    @Test
    public void findBondByNameAndMaturity_returnsBondRepresentation_givenAStoredBond() {
        Bond bond = newBond().name("Yahoo").maturity("3Y").createCurve();
        MarketDataRepository repo = mock(MarketDataRepository.class);
        MarketDataService svc = new MarketDataServiceImpl(repo);
        when(repo.findBondByNameAndMaturity("Yahoo", "3Y")).thenReturn(bond);

        BondRepresentation found = svc.findBondByNameAndMaturity("Yahoo", "3Y");

        assertEquals(bond.getName(), found.getName());
        assertEquals(bond.getMaturity(), found.getMaturity());
    }

    @Test
    public void storeLiborCurve_convertsRepresentationIntoDomainObjectBeforeCallingPersistenceLayer() throws MarketDataServiceException {
        LiborCurveRepresentation liborCurveRepr = newLiborCurve()
                .name("EURIBOR")
                .currency("EUR")
                .withTenors(newTenor("3m", 40d)
                        .newTenor("6m", 42.3d)
                        .newTenor("9m", 42.3d)
                        .newTenor("1Y", 45d)
                        .newTenor("2Y", 47.3d)
                        .newTenor("3Y", 49.8d)
                        .newTenor("4Y", 52.3d)
                        .newTenor("5Y", 55.3d)
                ).createRepresentation();
        when(repo.save(isA(LiborCurve.class))).thenAnswer(new Answer<LiborCurve>() {
            public LiborCurve answer(InvocationOnMock invocationOnMock) throws Throwable {
                LiborCurve curve = (LiborCurve) invocationOnMock.getArguments()[0];
                curve.setId(123L);
                return curve;
            }
        });
        ArgumentCaptor<LiborCurve> capture = ArgumentCaptor.forClass(LiborCurve.class);

        svc.storeLiborCurve(liborCurveRepr);

        verify(repo).save(capture.capture());
        assertNotNull(capture.getValue());

        LiborCurve liborCurve = capture.getValue();

        assertEquals(8, liborCurve.getTenors().size());
        assertEquals("EURIBOR", liborCurve.getName());
        assertEquals("EUR", liborCurve.getCurrency());
        assertEquals(new Tenor("3m", 40.0), liborCurve.getTenors().get(0));
        assertEquals(123L, liborCurve.getId());
    }

    @Test(expected=MarketDataServiceException.class)
    public void storeLiborCurve_throwsAMarketDataServiceException_givenAnInvalidLiborCurve() throws MarketDataServiceException {
        MarketDataRepository repo = mock(MarketDataRepository.class);
        MarketDataService svc = new MarketDataServiceImpl(repo);

        svc.storeLiborCurve(newLiborCurve().name(null).createRepresentation());
    }
}
