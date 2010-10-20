package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.Tenor;
import com.pyruby.marketdata.serializer.BondRepresentation;
import com.pyruby.marketdata.serializer.TenorRepresentation;

import java.util.ArrayList;
import java.util.List;


public class BondBuilder {
    private String name = "Sample Bond";
    private String ticker = "NYSE:TST";
    private String maturity = "5Y";
    private String issuer = "Parent Co";
    private TenorBuilder tenor = new TenorBuilder().newTenor("3m", 40).newTenor("6m", 42.3);

    public static BondBuilder newBond() {
        return new BondBuilder();
    }

    public static TenorBuilder newTenor(String interval, double bps) {
        return new TenorBuilder().newTenor(interval, bps);
    }

    public BondBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BondBuilder withTenors(TenorBuilder tenors) {
        this.tenor = tenors;
        return this;
    }

    public BondRepresentation createRepresentation() {
        BondRepresentation repr = new BondRepresentation();
        repr.setName(name);
        repr.setTicker(ticker);
        repr.setMaturity(maturity);
        repr.setIssuer(issuer);
        for (TenorRepresentation tenorRepr : tenor.createRepresentation()) {
            repr.addTenor(tenorRepr);
        }
        return repr;
    }

    public Bond createBond() {
        Bond bond = new Bond();
        bond.setName(name);
        bond.setTicker(ticker);
        bond.setMaturity(maturity);
        bond.setIssuer(issuer);
        bond.setTenors(tenor.createTenors());
        return bond;
    }

    public BondBuilder ticker(String ticker) {
        this.ticker = ticker;
        return this;
    }

    public BondBuilder maturity(String maturity) {
        this.maturity = maturity;
        return this;
    }

    public BondBuilder issuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    static class Pair<K,V> {
        final K key;
        final V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    static class TenorBuilder {
        List<Pair<String, Double>> tenors = new ArrayList<Pair<String, Double>>();

        public TenorBuilder newTenor(String interval, double bps) {
            tenors.add(new Pair<String, Double>(interval, bps));
            return this;
        }

        public List<TenorRepresentation> createRepresentation() {
            List<TenorRepresentation> reprs = new ArrayList<TenorRepresentation>();
            for (Pair<String, Double> tenor : tenors) {
                TenorRepresentation repr = new TenorRepresentation();
                repr.setPeriod(tenor.key);
                repr.setBasisPoints(tenor.value);
                reprs.add(repr);
            }
            return reprs;
        }

        public List<Tenor> createTenors() {
            List<Tenor> tenorList = new ArrayList<Tenor>();
            for (Pair<String, Double> tenor : tenors) {
                tenorList.add(new Tenor(tenor.key, tenor.value));
            }
            return tenorList;
        }
    }
}

