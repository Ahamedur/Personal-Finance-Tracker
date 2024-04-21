/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package financeTracker;

/**
 *
 * @author ahame
 */
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationService {

    private ScheduledExecutorService scheduler;

    public void startScheduledCheck() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::checkForUpcomingBills, 0, 1, TimeUnit.HOURS); // Check every hour
    }

    private void checkForUpcomingBills() {
        // Example method that checks for bills
        Platform.runLater(() -> {
            if (hasUpcomingBills()) {
                showAlert("You have upcoming bills due soon!");
            }
        });
    }

    private boolean hasUpcomingBills() {
        // Actual database check should be implemented here
        // Return true if there are bills due within the next few days
        return true; // This is just a placeholder
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bill Reminder");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }
}

