package com.pyruby.marketdata;

import com.pyruby.marketdata.serializer.BondRepresentation;

public interface MarketDataService {

    void storeBond(BondRepresentation bond);

    BondRepresentation findBondByNameAndMaturity(String name, String maturity);
}
