/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package financeTracker;

/**
 *
 * @author ahame
 */

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Budget {
    private final SimpleStringProperty category;
    private final SimpleDoubleProperty allocatedAmount;
    private final SimpleDoubleProperty spentAmount;

    public Budget(String category, double allocatedAmount, double spentAmount) {
        this.category = new SimpleStringProperty(category);
        this.allocatedAmount = new SimpleDoubleProperty(allocatedAmount);
        this.spentAmount = new SimpleDoubleProperty(spentAmount);
    }

    // Getters
    public String getCategory() { return category.get(); }
    public double getAllocatedAmount() { return allocatedAmount.get(); }
    public double getSpentAmount() { return spentAmount.get(); }

    // Setters
    public void setCategory(String value) { category.set(value); }
    public void setAllocatedAmount(double value) { allocatedAmount.set(value); }
    public void setSpentAmount(double value) { spentAmount.set(value); }

    // Property accessors
    public SimpleStringProperty categoryProperty() { return category; }
    public SimpleDoubleProperty allocatedAmountProperty() { return allocatedAmount; }
    public SimpleDoubleProperty spentAmountProperty() { return spentAmount; }
}
