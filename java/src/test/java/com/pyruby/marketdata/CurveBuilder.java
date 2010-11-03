package com.pyruby.marketdata;

import com.pyruby.marketdata.model.Bond;
import com.pyruby.marketdata.model.LiborCurve;
import com.pyruby.marketdata.model.Tenor;
import com.pyruby.marketdata.serializer.BondRepresentation;
import com.pyruby.marketdata.serializer.LiborCurveRepresentation;
import com.pyruby.marketdata.serializer.TenorRepresentation;

import java.util.ArrayList;
import java.util.List;


public class CurveBuilder<T> {
    protected String name = "Sample Bond";
    protected TenorBuilder tenor = new TenorBuilder().newTenor("3m", 40).newTenor("6m", 42.3);
    protected T me;

    public static BondBuilder newBond() {
        return new BondBuilder();
    }

    public static LiborCurveBuilder newLiborCurve() {
        return new LiborCurveBuilder();
    }

    public static TenorBuilder newTenor(String interval, double bps) {
        return new TenorBuilder().newTenor(interval, bps);
    }

    public T name(String name) {
        this.name = name;
        return me;
    }

    public T withTenors(TenorBuilder tenors) {
        this.tenor = tenors;
        return me;
    }

    public T withoutTenors() {
        tenor.clear();
        return me;
    }


    static class Pair<K, V> {
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

        public String createXml() {
            StringBuilder xml = new StringBuilder("<Tenors>");
            for (Pair<String, Double> tenor : tenors) {
                xml.append("<Tenor period=\"").append(tenor.key).append("\" bps=\"").append(tenor.value).append("\"/>");
            }
            xml.append("</Tenors>");
            return xml.toString();
        }

        public void clear() {
            tenors.clear();
        }
    }

    public static class BondBuilder extends CurveBuilder<BondBuilder> {
        private String ticker = "NYSE:TST";
        private String maturity = "5Y";
        private String issuer = "Parent Co";

        public BondBuilder() {
            this.me = this;
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

        public Bond createCurve() {
            Bond bond = new Bond();
            bond.setName(name);
            bond.setTicker(ticker);
            bond.setMaturity(maturity);
            bond.setIssuer(issuer);
            bond.setTenors(tenor.createTenors());
            return bond;
        }

        public String createXml() {
            StringBuilder xml = new StringBuilder("<Bond>");
            if (name != null) xml.append("<Name>").append(name).append("</Name>");
            if (ticker != null) xml.append("<Ticker>").append(ticker).append("</Ticker>");
            if (maturity != null) xml.append("<Maturity>").append(maturity).append("</Maturity>");
            if (issuer != null) xml.append("<Issuer>").append(issuer).append("</Issuer>");
            xml.append(tenor.createXml());
            xml.append("</Bond>");
            return xml.toString();
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


    }

    public static class LiborCurveBuilder extends CurveBuilder<LiborCurveBuilder> {
        private String currency = "GBP";

        public LiborCurveBuilder() {
            this.me = this;
        }

        public LiborCurveBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public LiborCurve createCurve() {
            LiborCurve curve = new LiborCurve();
            curve.setName(name);
            curve.setCurrency(currency);
            curve.setTenors(tenor.createTenors());
            return curve;
        }

        public LiborCurveRepresentation createRepresentation() {
            LiborCurveRepresentation repr = new LiborCurveRepresentation();
            repr.setName(name);
            repr.setCurrency(currency);
            for (TenorRepresentation tenorRepr : tenor.createRepresentation()) {
                repr.addTenor(tenorRepr);
            }
            return repr;
        }

        public String createXml() {
            StringBuilder xml = new StringBuilder("<LiborCurve>");
            if (name != null) xml.append("<Name>").append(name).append("</Name>");
            if (currency != null) xml.append("<Currency>").append(currency).append("</Currency>");
            xml.append(tenor.createXml());
            xml.append("</LiborCurve>");
            return xml.toString();
        }
    }
}