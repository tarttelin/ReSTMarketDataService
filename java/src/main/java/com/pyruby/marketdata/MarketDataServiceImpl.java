package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.LiborCurve;
import com.pyruby.marketdata.serializer.BondRepresentation;
import com.pyruby.marketdata.serializer.LiborCurveRepresentation;

public class MarketDataServiceImpl implements MarketDataService {
    private MarketDataRepository repo;

    public MarketDataServiceImpl(MarketDataRepository repo) {
        this.repo = repo;
    }

    public void storeBond(BondRepresentation bondRepr) throws MarketDataServiceException {
        Bond bond = bondRepr.toBond();
        if (bond.isValid()) repo.save(bond);
        else throw new MarketDataServiceException("Bond does not contain mandatory information", null);
    }

    public BondRepresentation findBondByNameAndMaturity(String name, String maturity) {
        Bond bond = repo.findBondByNameAndMaturity(name, maturity);
        return bond != null ? new BondRepresentation(bond) : null;
    }

    public void storeLiborCurve(LiborCurveRepresentation liborCurveRepr) {
        LiborCurve curve = liborCurveRepr.toLiborCurve();
        repo.save(curve);
    }
}
