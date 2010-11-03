package com.pyruby.marketdata;

import com.pyruby.marketdata.model.AbstractCurve;
import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.LiborCurve;

public interface MarketDataRepository {
    AbstractCurve save(AbstractCurve curve);

    Bond findBondByNameAndMaturity(String name, String maturity);

    LiborCurve findLiborCurveByName(String name);
}
