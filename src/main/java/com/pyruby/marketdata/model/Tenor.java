package com.pyruby.marketdata.model;

public class Tenor {
    private String interval;
    private double bps;

    public Tenor() {}

    public Tenor(String interval, double bps) {
        this.interval = interval;
        this.bps = bps;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public double getBps() {
        return bps;
    }

    public void setBps(double bps) {
        this.bps = bps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tenor)) return false;

        Tenor tenor = (Tenor) o;

        if (Double.compare(tenor.bps, bps) != 0) return false;
        if (interval != null ? !interval.equals(tenor.interval) : tenor.interval != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = interval != null ? interval.hashCode() : 0;
        temp = bps != +0.0d ? Double.doubleToLongBits(bps) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
