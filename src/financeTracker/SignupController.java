/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package financeTracker;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

public class SignupController {

    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private Button registerButton;
    
    @FXML
private Hyperlink loginLink;

    @FXML
    private void registerUser(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();

        // Database code to register user
        if (!username.isEmpty() && !password.isEmpty() && !email.isEmpty()) {
            insertUserIntoDB(username, password, email);
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter username, password, and email");
        }
    }
    
    @FXML
private void handleLoginLinkAction() {
    try {
        // Load the login form
        Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml")); // Ensure this path is correct
        Scene scene = new Scene(loginRoot);

        // Get current stage and set the new scene
        Stage stage = (Stage) loginLink.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    
@FXML
    private void handleSignInAction(ActionEvent event) throws Exception {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Scene scene = new Scene(loginRoot);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.setTitle("Login");
        window.show();
    }
    
    private void insertUserIntoDB(String username, String password, String email) {
        String sql = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            
           int result = pstmt.executeUpdate();

            // Check if the user was added successfully
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "No user was added to the database.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error registering user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
