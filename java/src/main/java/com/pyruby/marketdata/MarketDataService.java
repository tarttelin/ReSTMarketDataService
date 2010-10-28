package com.pyruby.marketdata;

import com.pyruby.marketdata.serializer.BondRepresentation;
import com.pyruby.marketdata.serializer.LiborCurveRepresentation;

public interface MarketDataService {

    void storeBond(BondRepresentation bond) throws MarketDataServiceException;

    BondRepresentation findBondByNameAndMaturity(String name, String maturity);

    void storeLiborCurve(LiborCurveRepresentation liborCurveRepr);
}
