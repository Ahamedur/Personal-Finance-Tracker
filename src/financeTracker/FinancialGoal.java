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

public class FinancialGoal {
     private final SimpleStringProperty description;
    private final SimpleDoubleProperty targetAmount;
    private final SimpleDoubleProperty currentAmount;
    
    public FinancialGoal(String description, double targetAmount, double currentAmount) {
        this.description = new SimpleStringProperty(description);
        this.targetAmount = new SimpleDoubleProperty(targetAmount);
        this.currentAmount = new SimpleDoubleProperty(currentAmount);
    
}
    // Getters
    public String getDescription() { return description.get(); }
    public double getTargetAmount() { return targetAmount.get(); }
    public double getCurrentAmount() { return currentAmount.get(); }
    
    // Setters
    public void setDescription(String value) { description.set(value); }
    public void setTargetAmount(double value) { targetAmount.set(value); }
    public void setCurrentAmount(double value) { currentAmount.set(value); }
    
    // Property accessors
    public SimpleStringProperty descriptionProperty() { return description; }
    public SimpleDoubleProperty targetAmountProperty() { return targetAmount; }
    public SimpleDoubleProperty currentAmountProperty() { return currentAmount; }
}