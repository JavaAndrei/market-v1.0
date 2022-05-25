package com.webapp.market.indicators;

public class EMA {

    private double lastEma;
    private int n;
    private double value;

    public EMA(double lastEma, int n) {
        this.lastEma = lastEma;
        this.n = n;
    }

    public void calculate(Double value) {
        double a = 2.0 / (n + 1);
        this.value = a * value + (1 - a) * lastEma;
    }

    public double getValue() {
        return value;
    }

}