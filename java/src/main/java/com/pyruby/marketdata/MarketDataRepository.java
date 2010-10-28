package com.pyruby.marketdata;

import com.pyruby.marketdata.model.AbstractCurve;
import com.pyruby.marketdata.model.Bond;

public interface MarketDataRepository {
    AbstractCurve save(AbstractCurve curve);

    Bond findBondByNameAndMaturity(String name, String maturity);
}
