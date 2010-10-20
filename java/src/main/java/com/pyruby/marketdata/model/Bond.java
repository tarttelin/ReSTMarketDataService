package com.pyruby.marketdata.model;

import java.util.List;

public class Bond {
    private long id;
    private List<Tenor> tenors;
    private String name;
    private String ticker;
    private String maturity;
    private String issuer;

    public void setId(long id) {
        this.id = id;
    }

    public List<Tenor> getTenors() {
        return tenors;
    }

    public String getName() {
        return name;
    }

    public String getTicker() {
        return ticker;
    }

    public String getMaturity() {
        return maturity;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setTenors(List<Tenor> tenors) {
        this.tenors = tenors;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getId() {
        return id;
    }
}
