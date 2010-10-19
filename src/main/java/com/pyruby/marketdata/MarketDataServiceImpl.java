package com.pyruby.marketdata;

import com.pyruby.marketdata.serializer.BondRepresentation;


public class MarketDataServiceImpl implements MarketDataService {
    private MarketDataRepository repo;

    public MarketDataServiceImpl(MarketDataRepository repo) {
        this.repo = repo;
    }

    public void storeBond(BondRepresentation bondRepr) {
        repo.save(bondRepr.toBond());
    }
}
