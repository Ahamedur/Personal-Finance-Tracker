/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package financeTracker;

/**
 *
 * @author ahame
 */


import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.Desktop;

import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;


public class ReceiptsController {
    @FXML
    private TableView<Receipt> receiptsTable;
    @FXML
    private TableColumn<Receipt, String> fileNameColumn;
    @FXML
    private TableColumn<Receipt, String> categoryColumn;
    @FXML
    private TableColumn<Receipt, Date> uploadDateColumn;

    public void initialize() {
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        uploadDateColumn.setCellValueFactory(new PropertyValueFactory<>("uploadDate"));
        loadReceipts();
    }

    private int getCurrentUserId() {
    UserSession session = UserSession.getInstance();
    if (session != null) {
        return session.getUserId();
    } else {
        return -1;  // or handle this case as you see fit (maybe throw an exception or handle an error state)
    }
}

   @FXML
private void handleUpload() {
    FileChooser fileChooser = new FileChooser();
    // Set extension filters, for example:
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
    fileChooser.getExtensionFilters().add(filter);

    File file = fileChooser.showOpenDialog(null); // Make sure the window is correctly parented in your app
    if (file != null) {
        String fileName = file.getName();
        String filePath = file.getAbsolutePath();
        String category = "Uncategorized"; // Default category, could be prompted to user
        LocalDate uploadDate = LocalDate.now(); // Current date
        int currentUserId = getCurrentUserId();
        insertReceipt(fileName, category, Date.valueOf(uploadDate), filePath, currentUserId);
        loadReceipts();
    }
    else {
        showAlert("Upload Failed", "No file was selected.");
    }
}

private void showAlert(String title, String content) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null); // No header
    alert.setContentText(content);
    alert.showAndWait();
}

    private void insertReceipt(String fileName, String category, Date uploadDate, String filePath, int userId) {
    String sql = "INSERT INTO Receipts (fileName, category, uploadDate, filePath, userId) VALUES (?, ?, ?, ?, ?)";
    System.out.println("Executing SQL: " + sql);
    System.out.println("With values: " + fileName + ", " + category + ", " + uploadDate + ", " + filePath + ", " + userId);
    
    try (Connection conn = DBConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, fileName);
        pstmt.setString(2, category);
        pstmt.setDate(3, uploadDate);
        pstmt.setString(4, filePath);
        pstmt.setInt(5, userId);
        
        int result = pstmt.executeUpdate();
        if (result > 0) {
            System.out.println("Receipt uploaded successfully");
        } else {
            System.out.println("Failed to upload receipt");
        }
    } catch (Exception e) {
        System.err.println("SQL error occurred: " + e.getMessage());
        e.printStackTrace();
        showAlert("Database Error", "Failed to insert the receipt into the database: " + e.getMessage());
    }
}



    private void loadReceipts() {
    int userId = UserSession.getInstance().getUserId();  // Ensure this method correctly retrieves the current user's ID
    ObservableList<Receipt> receipts = FXCollections.observableArrayList();
    String sql = "SELECT id, fileName, category, uploadDate, filePath FROM Receipts WHERE userId = ?";

    try (Connection conn = DBConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            receipts.add(new Receipt(
                rs.getInt("id"),
                rs.getString("fileName"),
                rs.getString("category"),
                rs.getDate("uploadDate").toLocalDate(),
                rs.getString("filePath")
            ));
        }
        receiptsTable.setItems(receipts);
    } catch (Exception e) {
        e.printStackTrace();
        showAlert("Database Error", "Failed to load receipts: " + e.getMessage());
    }
}




    @FXML
private void handleView() {
    Receipt selected = receiptsTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
        if (Desktop.isDesktopSupported()) {
            try {
                String filePath = selected.getFilePath();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (file.exists()) {
                        Desktop.getDesktop().open(file);
                    } else {
                        showAlert("File Error", "File does not exist at path: " + filePath);
                    }
                } else {
                    showAlert("File Error", "No file path available for the selected record.");
                }
            } catch (IOException e) {
                showAlert("Error Opening File", "An error occurred while opening the file: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Desktop operations not supported on this platform.");
        }
    } else {
        showAlert("No Selection", "No file selected.");
    }
}

    @FXML
private void handleEdit() {
    Receipt selected = receiptsTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
        // Open a dialog to edit categories or other metadata
        TextInputDialog dialog = new TextInputDialog(selected.getCategory());
        dialog.setTitle("Edit Receipt");
        dialog.setHeaderText("Edit the category of the receipt");
        dialog.setContentText("Enter new category:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(category -> {
            updateReceiptCategory(selected, category);
            loadReceipts();  // Reload data from the database to reflect changes
        });
    } else {
        showAlert("No Selection", "No receipt selected. Please select a receipt to edit.");
    }
}

private void updateReceiptCategory(Receipt receipt, String newCategory) {
    String sql = "UPDATE Receipts SET category = ? WHERE id = ?";
    try (Connection conn = DBConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, newCategory);
        pstmt.setInt(2, receipt.getId());
        pstmt.executeUpdate();
    } catch (Exception e) {
        showAlert("Database Error", "Failed to update the receipt: " + e.getMessage());
    }
}


    @FXML
    private void handleDelete() {
        Receipt selected = receiptsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String sql = "DELETE FROM Receipts WHERE id = ?";
            try (Connection conn = DBConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, selected.getId());
                pstmt.executeUpdate();
                loadReceipts(); // Reload data
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
