package com.sumit.ibox.model;

public class FeesBreakupData {
    String name;
    String amount;

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public FeesBreakupData(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }
}
