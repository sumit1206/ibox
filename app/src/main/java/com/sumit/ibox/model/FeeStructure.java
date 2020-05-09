package com.sumit.ibox.model;


import androidx.annotation.NonNull;

public class FeeStructure {

    private String month;
    private int amount;
    private int fine;
    private int discount;
    private String lastDate;
    private String classId;
    private String monthId;
    private boolean isSelected;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getMonth() {
        return month;
    }

    public int getAmount() {
        return amount;
    }

    public int getFine() {
        return fine;
    }

    public int getDiscount() {
        return discount;
    }

    public String getLastDate() {
        return lastDate;
    }

    public String getClassId() {
        return classId;
    }

    public String getMonthId() {
        return monthId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public FeeStructure(String month, int amount, int fine, int discount, String lastDate, String classId, String monthId, boolean isSelected) {
        this.month = month;
        this.amount = amount;
        this.fine = fine;
        this.discount = discount;
        this.lastDate = lastDate;
        this.classId = classId;
        this.monthId = monthId;
        this.isSelected = isSelected;
    }

    @NonNull
    public String toString(){
        String stringBuilder = this.monthId +
                this.classId +
                this.amount +
                this.discount;
        return stringBuilder;
    }
}
