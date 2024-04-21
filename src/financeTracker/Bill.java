/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package financeTracker;

import java.time.LocalDate;

public class Bill {
    private int billId;
    private String billName;
    private double amountDue;
    private LocalDate dueDate;
    private boolean isPaid;

    // Constructor
    public Bill(int billId, String billName, double amountDue, LocalDate dueDate, boolean isPaid) {
        this.billId = billId;
        this.billName = billName;
        this.amountDue = amountDue;
        this.dueDate = dueDate;
        this.isPaid = isPaid;
    }

    // Getters and setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaid() {
        return isPaid;
    }
public boolean getIsPaid() {
    return isPaid;
}

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
}
