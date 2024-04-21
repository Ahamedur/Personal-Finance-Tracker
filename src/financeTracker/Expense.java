/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package financeTracker;

import java.time.LocalDate;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {
    private final SimpleStringProperty category;
    private final SimpleFloatProperty amount;
    private final SimpleObjectProperty<LocalDate> date;

    public Expense(String category, float amount, LocalDate date) {
        this.category = new SimpleStringProperty(category);
        this.amount = new SimpleFloatProperty(amount);
        this.date = new SimpleObjectProperty<>(date);
    }

    // Getters
    public String getCategory() { 
        return category.get(); 
    }
    
    public float getAmount() { 
        return amount.get(); 
    }
    
    public LocalDate getDate() { 
        return date.get(); 
    }

    // Setters
    public void setCategory(String value) { 
        category.set(value); 
    }
    
    public void setAmount(float value) { 
        amount.set(value); 
    }
    
    public void setDate(LocalDate value) { 
        date.set(value); 
    }

    // Property accessors
    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public SimpleFloatProperty amountProperty() {
        return amount;
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }
}

