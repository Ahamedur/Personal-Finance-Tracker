/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package financeTracker;

import java.time.LocalDate;

/**
 *
 * @author ahame
 */
public class Receipt {
    private int id;
    private String fileName;
    private String category;
    private LocalDate uploadDate;
    private String filePath; // Add this line

    public Receipt(int id, String fileName, String category, LocalDate uploadDate, String filePath) { // Update constructor
        this.id = id;
        this.fileName = fileName;
        this.category = category;
        this.uploadDate = uploadDate;
        this.filePath = filePath; // Assign file path
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getFileName() {
        return fileName;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public String getFilePath() { // Getter for filePath
        return filePath;
    }

    // Setters as needed
}
