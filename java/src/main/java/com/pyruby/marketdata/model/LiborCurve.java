package com.pyruby.marketdata.model;

public class LiborCurve extends AbstractCurve {
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
