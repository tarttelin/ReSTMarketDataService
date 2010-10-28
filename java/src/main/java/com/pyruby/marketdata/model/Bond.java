package com.pyruby.marketdata.model;

import java.util.List;

public class Bond extends AbstractCurve {
    private String ticker;
    private String maturity;
    private String issuer;

    public String getTicker() {
        return ticker;
    }

    public String getMaturity() {
        return maturity;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setMaturity(String maturity) {
        this.maturity = maturity;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public boolean isValid() {
        return name != null && maturity != null && tenors != null && !tenors.isEmpty();
    }
}
