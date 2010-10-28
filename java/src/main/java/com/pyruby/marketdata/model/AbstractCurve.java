package com.pyruby.marketdata.model;

import java.util.List;

public abstract class AbstractCurve {
    private long id;
    protected List<Tenor> tenors;
    protected String name;

    public void setId(long id) {
        this.id = id;
    }

    public List<Tenor> getTenors() {
        return tenors;
    }

    public String getName() {
        return name;
    }

    public void setTenors(List<Tenor> tenors) {
        this.tenors = tenors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }
}
