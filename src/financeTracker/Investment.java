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

public class Investment {
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty amountInvested;
    private final SimpleDoubleProperty currentValue;
    private final SimpleStringProperty category; // Optional, depending on your tracking needs

    public Investment(String name, double amountInvested, double currentValue, String category) {
        this.name = new SimpleStringProperty(name);
        this.amountInvested = new SimpleDoubleProperty(amountInvested);
        this.currentValue = new SimpleDoubleProperty(currentValue);
        this.category = new SimpleStringProperty(category);
    }

    // Getters and setters
    public String getName() { return name.get(); }
    public double getAmountInvested() { return amountInvested.get(); }
    public double getCurrentValue() { return currentValue.get(); }
    public String getCategory() { return category.get(); }

    // Property accessors for TableView binding
    public SimpleStringProperty nameProperty() { return name; }
    public SimpleDoubleProperty amountInvestedProperty() { return amountInvested; }
    public SimpleDoubleProperty currentValueProperty() { return currentValue; }
    public SimpleStringProperty categoryProperty() { return category; }
}
