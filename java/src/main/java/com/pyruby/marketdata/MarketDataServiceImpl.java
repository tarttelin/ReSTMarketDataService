package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.serializer.BondRepresentation;

public class MarketDataServiceImpl implements MarketDataService {
    private MarketDataRepository repo;

    public MarketDataServiceImpl(MarketDataRepository repo) {
        this.repo = repo;
    }

    public void storeBond(BondRepresentation bondRepr) {
        repo.save(bondRepr.toBond());
    }

    public BondRepresentation findBondByNameAndMaturity(String name, String maturity) {
        Bond bond = repo.findByNameAndMaturity(name, maturity);
        return bond != null ? new BondRepresentation(bond) : null;
    }
}
