package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;

public interface MarketDataRepository {
    Bond save(Bond bond);

    Bond findByNameAndMaturity(String name, String maturity);
}
