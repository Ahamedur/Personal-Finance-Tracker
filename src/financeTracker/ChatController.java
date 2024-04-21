/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package financeTracker;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ahame
 */
public class ChatController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField chatInput;

    @FXML
    private void handleChatInput() {
        String message = chatInput.getText().trim();
        if (!message.isEmpty()) {
            chatArea.appendText("You: " + message + "\n");

            // Generate and display the response from the chatbot
            String response = processInput(message);
            chatArea.appendText("ChatBot: " + response + "\n");

            chatInput.clear();
        }
    }
    
    private String processInput(String input) {
    input = input.toLowerCase();
    if (input.contains("how")) {
        return "To add a transaction, go to the 'Add Transaction' tab.";
    } else if (input.contains("problem")) {
        return "Please describe your problem, and I'll try to help.";
    } else if (input.contains("budget")) {
        return "You can set your budget in the 'Budgets' section.";
    } else {
        return "I'm not sure how to help with that. Can you try asking differently?";
    }
}
}
