package com.doantotnghiep.nhanambooks.enums;

public enum PaymentMethod {
    CASH("Thanh toán bằng tiền mặt"),
    PAYPAL("Paypal"),
    BANKING("Banking"),
    MOMO("Momo");

    public final String label;

    public String getLabel(){
        return label;
    }

    PaymentMethod(String label) {
        this.label = label;
    }
}
