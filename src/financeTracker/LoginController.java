package financeTracker;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField visiblePasswordField;
    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Button loginButton;
    @FXML
    private Label messageLabel;
@FXML
private TextField emailField;

@FXML
private Hyperlink signupLink;  // This links to the Hyperlink in the FXML

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Optional: Any initialization code here
    }

    @FXML
private void handleSignupLinkAction() {
    try {
        // Load the signup form
        Parent signupRoot = FXMLLoader.load(getClass().getResource("signupform.fxml"));
        Scene scene = new Scene(signupRoot);

        // Get current stage and set the new scene
        Stage stage = (Stage) signupLink.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    
   @FXML
private void handleShowPasswordAction() {
    if (showPasswordCheckBox.isSelected()) {
        visiblePasswordField.setText(passwordField.getText());
        visiblePasswordField.setVisible(true);
        visiblePasswordField.setManaged(true);
        passwordField.setVisible(false);
        passwordField.setManaged(false);
    } else {
        passwordField.setText(visiblePasswordField.getText());
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);
    }
} 
    
    @FXML
private void handleRetrieveCredentials() {
    String email = emailField.getText().trim();
    if (email.isEmpty()) {
        showAlert("Error", "Please enter your email address.");
        return;
    }
    retrieveCredentials(email);
}

private void retrieveCredentials(String email) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/PersonalFinanceDB?serverTimezone=UTC", "root", "Nuzhat2023");
        String sql = "SELECT username, password FROM Users WHERE email = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, email);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password"); // Consider only sending a reset link or hint instead of the actual password for security.
            showAlert("Credentials Retrieved", "Username: " + username + "\nPassword: " + password);
        } else {
            showAlert("Error", "No account found with that email address.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Database Error", "Failed to retrieve credentials: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
    
   @FXML
    private void handleLoginAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (validateCredentials(username, password)) {
            int userId = retrieveUserId(username);  // You need to implement this method to fetch the user ID from the database.
        UserSession.getInstance(userId, username); // Initialize the session with user details
            try {
                // Load the main application window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainApplication.fxml"));
                Parent root = loader.load();
                
                // Get current stage and set the new scene
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Personal Finance Tracker");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while loading the application window.");
            }
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }
    
    private int retrieveUserId(String username) {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/PersonalFinanceDB?serverTimezone=UTC", "root", "Nuzhat2023")) {
        String sql = "SELECT user_id FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }
    } catch (SQLException e) {
        showAlert("Database Error", "An error occurred while fetching user ID: " + e.getMessage());
        e.printStackTrace();
    }
    return -1; // Return an invalid ID if something goes wrong
}

    private boolean validateCredentials(String username, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/PersonalFinanceDB?serverTimezone=UTC", "root", "Nuzhat2023");
            String sql = "SELECT password FROM Users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String retrievedPassword = rs.getString("password");
                return retrievedPassword.equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while verifying credentials: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}