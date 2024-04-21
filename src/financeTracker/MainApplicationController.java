/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package financeTracker;



import java.awt.Desktop;
import java.awt.Event;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import javafx.scene.control.Tab;


public class MainApplicationController {
    @FXML
    private TabPane mainTabPane;
    @FXML
    private TableView<Expense> expensesTable;
    @FXML private TableColumn<Expense, String> categoryColumn;
    @FXML private TableColumn<Expense, Float> amountColumn;
    @FXML private TableColumn<Expense, LocalDate> dateColumn;
    @FXML private TextField amountField;
    @FXML private TextField categoryField;
@FXML private DatePicker datePicker;
@FXML
private TableView<Budget> budgetsTable;
@FXML
private TableColumn<Budget, String> budgetCategoryColumn;
@FXML
private TableColumn<Budget, Number> allocatedAmountColumn;
@FXML
private TableColumn<Budget, Number> spentAmountColumn;

@FXML
private TextField budgetCategoryField;
@FXML
private TextField allocatedAmountField;

@FXML
private TableView<FinancialGoal> goalsTable;
@FXML
private TableColumn<FinancialGoal, String> descriptionColumn;
@FXML
private TableColumn<FinancialGoal, Number> targetAmountColumn;
@FXML
private TableColumn<FinancialGoal, Number> currentAmountColumn;

@FXML
private TextField goalDescriptionField;
@FXML
private TextField goalTargetAmountField;

@FXML
private TableView<Investment> investmentsTable;
@FXML
private TableColumn<Investment, String> nameColumn;
@FXML
private TableColumn<Investment, Number> amountInvestedColumn;
@FXML
private TableColumn<Investment, Number> currentValueColumn;
@FXML
private TableColumn<Investment, String> investmentCategoryColumn;

@FXML
private TextField investmentNameField;
@FXML
private TextField amountInvestedField;
@FXML
private TextField investmentCurrentValueField;
@FXML
private TextField investmentCategoryField;
@FXML
private PieChart assetAllocationChart;
@FXML
private LineChart<String, Number> investmentGrowthChart;
@FXML
private CategoryAxis xAxis; // Assuming the X-axis is time (e.g., months, years)
@FXML
private NumberAxis yAxis; // Assuming the Y-axis is the value of investments

@FXML
private BarChart<String, Number> investmentComparisonChart;
@FXML
private CategoryAxis categoriesAxis;
@FXML
private NumberAxis valuesAxis;

@FXML
    private Button logoutButton;
@FXML
private Button chatButton;

@FXML
private TableView<Bill> billsTable;
@FXML
private TableColumn<Bill, String> billNameColumn;
@FXML
private TableColumn<Bill, Double> amountDueColumn;
@FXML
private TableColumn<Bill, LocalDate> dueDateColumn;
@FXML
private TableColumn<Bill, Boolean> isPaidColumn;

@FXML
private TextField billNameField;
@FXML
private TextField amountDueField;
@FXML
private DatePicker dueDatePicker;
@FXML
private Button addBillButton;
@FXML
private Button deleteBillButton;
@FXML
    private Tab billsTab;  
private ObservableList<Bill> billData = FXCollections.observableArrayList();
private NotificationService notificationService;
@FXML
public void handleBillManagementTabSelected() {
    if (billsTab.isSelected()) {
        loadBills();  // This method will load the bills from the database and update the table view
    }
}
//private void initializeBillNotifications() {
    // Check for upcoming bills once during initialization or after login
    //if (hasUpcomingBills(UserSession.getInstance().getUserId())) {
       // showAlert("Bill Due", "You have upcoming bills due soon!");
    //}
//}

private List<Bill> getUpcomingBills(int userId) {
    List<Bill> upcomingBills = new ArrayList<>();
    String query = "SELECT bill_name, due_date FROM Bills WHERE user_id = ? AND due_date <= DATE_ADD(CURDATE(), INTERVAL 3 DAY) AND is_paid = false";
    try (Connection conn = DBConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, userId);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String billName = rs.getString("bill_name");
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                upcomingBills.add(new Bill(0, billName, 0, dueDate, false)); // Using placeholders for non-retrieved fields
            }
        }
    } catch (SQLException e) {
        System.out.println("Error checking for upcoming bills: " + e.getMessage());
        e.printStackTrace();
    }
    return upcomingBills;
}

private void initializeBillNotifications() {
    List<Bill> bills = getUpcomingBills(UserSession.getInstance().getUserId());
    if (!bills.isEmpty()) {
        StringBuilder message = new StringBuilder("You have upcoming bills due soon:\n");
        for (Bill bill : bills) {
            message.append(bill.getBillName()).append(" due on ").append(bill.getDueDate().toString()).append("\n");
        }
        showAlert("Bill Due", message.toString());
    }
}


    private boolean hasUpcomingBills(int userId) {
    String query = "SELECT COUNT(*) FROM Bills WHERE user_id = ? AND due_date <= DATE_ADD(CURDATE(), INTERVAL 3 DAY) AND is_paid = false";
    try (Connection conn = DBConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, userId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if there is at least one upcoming bill
            }
        }
    } catch (SQLException e) {
        System.out.println("Error checking for upcoming bills: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}


    public void closeApplication() {
        notificationService.stop(); // Ensure to stop the scheduler when the app is closed
    }
private void loadBills() {
    ObservableList<Bill> bills = FXCollections.observableArrayList();
    // Database query to fetch bills
    String query = "SELECT * FROM Bills WHERE user_id = ?";
    try (Connection conn = DBConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, UserSession.getInstance().getUserId()); // Assuming UserSession holds the logged-in user ID
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            bills.add(new Bill(
                rs.getInt("bill_id"),
                rs.getString("bill_name"),
                rs.getDouble("amount_due"),
                rs.getDate("due_date").toLocalDate(),
                rs.getBoolean("is_paid")
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    billsTable.setItems(bills);
}
@FXML
private void handleAddBill() {
    String insertQuery = "INSERT INTO Bills (user_id, bill_name, amount_due, due_date, is_paid) VALUES (?, ?, ?, ?, false)";
    try (Connection conn = DBConnection.connect();
         PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
        pstmt.setInt(1, UserSession.getInstance().getUserId());
        pstmt.setString(2, billNameField.getText());
        pstmt.setDouble(3, Double.parseDouble(amountDueField.getText()));
        pstmt.setDate(4, Date.valueOf(dueDatePicker.getValue()));
        pstmt.executeUpdate();
        loadBills();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

@FXML
private void handleDeleteBill() {
    Bill selected = billsTable.getSelectionModel().getSelectedItem();
    if (selected != null) {
        String deleteQuery = "DELETE FROM Bills WHERE bill_id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, selected.getBillId());
            pstmt.executeUpdate();
            billsTable.getItems().remove(selected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


@FXML
    private void handleLogout() {
        // Close the current stage
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
        UserSession.getInstance().cleanUserSession();
        // Open the login window again
        //openLoginStage();
    }
    //private void openLoginStage() {
        //try {
             //Load the FXML document for the login window
           //FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml")); // Adjust the path as necessary
            //Parent root = loader.load();

            // Create a new stage for the login window
            //Stage loginStage = new Stage();
            //loginStage.setTitle("Login");
            //loginStage.setScene(new Scene(root));
            //loginStage.show();

        //} catch (IOException e) {
           // e.printStackTrace();
            // Handle exceptions (e.g., FXML file not found)
        //}}
    
    
    
    @FXML
private void handleAddExpense() {
    // Parse and validate input values
    LocalDate date = datePicker.getValue();
    String category = categoryField.getText();
    float amount = 0;
    try {
        amount = Float.parseFloat(amountField.getText());
    } catch (NumberFormatException e) {
        showAlert("Input Error", "Please enter a valid amount.");
        return;
    }

    // Create a new Expense object and add it to the TableView
    Expense newExpense = new Expense(category, amount, date);
    expensesTable.getItems().add(newExpense);

    // Optionally, clear the input fields after adding
    updateBudgets(newExpense.getCategory(), newExpense.getAmount());
    categoryField.clear();
    amountField.clear();
    datePicker.setValue(null);
}

@FXML
    private void handleLinkToBank() {
        try {
            // Replace this URL with the user's bank URL or a retrieved URL from user settings
            URI bankUri = new URI("http://www.bofa.com");
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(bankUri);
            } else {
                System.out.println("Desktop is not supported");
            }
        } catch (Exception e) {
            System.out.println("Failed to open link: " + e.getMessage());
        }
    }

@FXML
private void handleRemoveExpense() {
    // Get the selected expense
    Expense selectedExpense = expensesTable.getSelectionModel().getSelectedItem();

    // Check if an expense is actually selected
    if (selectedExpense != null) {
        // Remove the selected expense from the TableView
        // Subtract the removed expense's amount from the corresponding budget's spent amount
        String category = selectedExpense.getCategory();
        expensesTable.getItems().remove(selectedExpense);
        updateBudgetsOnExpenseRemoval(category); // Adjusted to only pass category
        //updateBudgetsOnExpenseRemoval(selectedExpense.getCategory(), selectedExpense.getAmount());
        //expensesTable.getItems().remove(selectedExpense);
    } else {
        // Optionally, alert the user that no expense was selected to remove
        showAlert("No Selection", "No Expense Selected. Please select an expense in the table.");
    }
}

@FXML
private void handleAddBudget() {
    String category = budgetCategoryField.getText();
    double allocatedAmount = Double.parseDouble(allocatedAmountField.getText());

    Budget newBudget = new Budget(category, allocatedAmount, 0.0); // Assuming 0 spent at creation
    budgetsTable.getItems().add(newBudget);

    // Clear the input fields
    budgetCategoryField.clear();
    allocatedAmountField.clear();
}
private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}
//private void updateBudgets(String category, double amountSpent) {
   // for (Budget budget : budgetsTable.getItems()) {
       // if (budget.getCategory().equals(category)) {
        //    double newSpentAmount = budget.getSpentAmount() + amountSpent;
       //     budget.setSpentAmount(newSpentAmount);
       //     break; // Assuming category names are unique, stop once found
       // }
    //}
    // Refresh the budgets table to show updated data
   // budgetsTable.refresh();
//}

private void updateBudgets(String category, double amountSpent) {
    String trimmedCategory = category.trim();

    for (Budget budget : budgetsTable.getItems()) {
        if (budget.getCategory().trim().equalsIgnoreCase(trimmedCategory)) {
            double newSpentAmount = budget.getSpentAmount() + amountSpent;
            budget.setSpentAmount(newSpentAmount);

            // Check if the new spent amount exceeds the allocated budget amount
            if (newSpentAmount > budget.getAllocatedAmount()) {
                // Show alert if the spent amount exceeds the allocated budget
                showAlert("Budget Exceeded", "The spent amount for '" + trimmedCategory + "' has exceeded the allocated budget.");
            }
            
            budgetsTable.refresh(); // Refresh the table to show the updated data
            return;
        }
    }

    // Optionally, handle the case where no matching budget category is found
    showAlert("Category not found in budgets", "Category: " + trimmedCategory + " not found.");
}

private void updateBudgetsOnExpenseRemoval(String category) {
    double totalSpent = 0.0;
    // Calculate the total spent for the category after removing the expense
    for (Expense expense : expensesTable.getItems()) {
        if (expense.getCategory().trim().equalsIgnoreCase(category.trim())) {
            totalSpent += expense.getAmount();
        }
    }

    // Find the matching budget and update its spent amount
    for (Budget budget : budgetsTable.getItems()) {
        if (budget.getCategory().trim().equalsIgnoreCase(category.trim())) {
            budget.setSpentAmount(totalSpent);
            budgetsTable.refresh();
            return;
        }
    }

    // Refresh the table to show the updated budget
    
    
   
}
@FXML
private void handleAddGoal() {
    String description = goalDescriptionField.getText();
    double targetAmount = Double.parseDouble(goalTargetAmountField.getText());

    FinancialGoal newGoal = new FinancialGoal(description, targetAmount, 0.0); // Starting with 0 progress
    goalsTable.getItems().add(newGoal);

    // Clear the input fields
    goalDescriptionField.clear();
    goalTargetAmountField.clear();
}

@FXML
private void handleAddContributionToGoal() {
    FinancialGoal selectedGoal = goalsTable.getSelectionModel().getSelectedItem();
    if (selectedGoal != null) {
        // Create and configure the input dialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Contribution");
        dialog.setHeaderText("Add Contribution to Goal");
        dialog.setContentText("Please enter the additional amount:");

        // Show the dialog and capture the optional result
        Optional<String> result = dialog.showAndWait();
        
        // Process the result
        result.ifPresent(amountString -> {
            try {
                double additionalAmount = Double.parseDouble(amountString);
                double newCurrentAmount = selectedGoal.getCurrentAmount() + additionalAmount;
                selectedGoal.setCurrentAmount(newCurrentAmount);
                goalsTable.refresh(); // Refresh the table to show the updated data
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number for the amount.");
            }
        });
    } else {
        showAlert("No Goal Selected", "Please select a goal to update.");
    }
}

@FXML
private void handleAddInvestment() {
    String name = investmentNameField.getText();
    double amountInvested = Double.parseDouble(amountInvestedField.getText());
    double currentValue = Double.parseDouble(investmentCurrentValueField.getText());
    String category = investmentCategoryField.getText();

    Investment newInvestment = new Investment(name, amountInvested, currentValue, category);
    investmentsTable.getItems().add(newInvestment);

    // Clear the input fields
    investmentNameField.clear();
    amountInvestedField.clear();
    investmentCurrentValueField.clear();
    investmentCategoryField.clear();
    //updateAssetAllocationChart(); // Make sure this is called after adding an investment
}
@FXML
private void handleShowChart() {
    
    // Create a new Stage for the popup
    //Stage popupStage = new Stage();
   // popupStage.setTitle("Asset Allocation Chart");

    // Prepare your pie chart data here (if not already prepared)
    //updateAssetAllocationChart(); // Assuming this method prepares the `assetAllocationChart` data

    // Create a new Scene with the pie chart and set it on the popup stage
   // Scene scene = new Scene(assetAllocationChart);
    //popupStage.setScene(scene);

    // Show the popup stage
   // popupStage.show();
   
   // Aggregate investments by category directly here for simplicity
    Map<String, Double> investmentByCategory = new HashMap<>();
    for (Investment investment : investmentsTable.getItems()) {
        investmentByCategory.merge(investment.getCategory(), investment.getAmountInvested(), Double::sum);
    }

    // Prepare pie chart data
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    investmentByCategory.forEach((category, sum) -> pieChartData.add(new PieChart.Data(category, sum)));

    // Update the @FXML injected PieChart's data or create a new instance if you prefer
    //assetAllocationChart.setData(pieChartData);
PieChart pieChart = new PieChart(pieChartData); // Creating a new instance for clarity
    pieChart.setTitle("Asset Allocation");

    // Explicitly set the preferred size of the pie chart
    pieChart.setPrefSize(400, 400); // Adjust these values as needed
    // Now, assetAllocationChart is up-to-date with the latest data
    // Create a new Scene with the updated assetAllocationChart and set it on the popup stage
    VBox layout = new VBox(pieChart); // Use the updated assetAllocationChart here
    layout.setAlignment(Pos.CENTER);
    Scene scene = new Scene(layout, 800, 600); // Adjust size as needed
    
    Stage popupStage = new Stage();
    popupStage.setTitle("Asset Allocation Chart");
    popupStage.setScene(scene);
    popupStage.show();
}
@FXML
private void handleShowGraph(){
CategoryAxis xAxis = new CategoryAxis();
NumberAxis yAxis = new NumberAxis();
xAxis.setLabel("Financial Goal");
yAxis.setLabel("Amount");
BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
barChart.setTitle("Financial Goal Progress");

// Assuming you have a method or means to get your financial goals
ObservableList<FinancialGoal> financialGoals = goalsTable.getItems();

// Create two series for the target and current amounts
XYChart.Series<String, Number> targetSeries = new XYChart.Series<>();
targetSeries.setName("Target Amount");
XYChart.Series<String, Number> currentSeries = new XYChart.Series<>();
currentSeries.setName("Current Amount");

for (FinancialGoal goal : financialGoals) {
    targetSeries.getData().add(new XYChart.Data<>(goal.getDescription(), goal.getTargetAmount()));
    currentSeries.getData().add(new XYChart.Data<>(goal.getDescription(), goal.getCurrentAmount()));
}

barChart.getData().addAll(targetSeries, currentSeries);
barChart.setLegendVisible(true);
VBox layout = new VBox(barChart);
layout.setAlignment(Pos.CENTER);
Scene scene = new Scene(layout, 800, 600); // Adjust the size as needed

Stage popupStage = new Stage();
popupStage.setTitle("Financial Goal Progress");
popupStage.setScene(scene);
popupStage.show();


}

@FXML
private void handleShowBudgetChart() {
    ObservableList<Budget> budgetData = budgetsTable.getItems(); // Assuming budgetsTable is your TableView

    double totalAllocatedAmount = budgetData.stream()
        .mapToDouble(Budget::getAllocatedAmount)
        .sum();
    double totalSpentAmount = budgetData.stream()
        .mapToDouble(Budget::getSpentAmount)
        .sum();
    
    // Calculate the remaining budget
    double remainingAmount = totalAllocatedAmount - totalSpentAmount;

    // Prepare pie chart data
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Spent Amount", totalSpentAmount),
            new PieChart.Data("Remaining Amount", remainingAmount));

    PieChart pieChart = new PieChart(pieChartData);
    pieChart.setTitle("Budget Overview");

    // Enhance pie chart visualization
    pieChart.setLabelsVisible(true);
    pieChart.setStartAngle(90);

    // Scene and stage setup
    VBox layout = new VBox(pieChart);
    layout.setAlignment(Pos.CENTER);
    Scene scene = new Scene(layout, 800, 600); // Adjust size as necessary

    Stage popupStage = new Stage();
    popupStage.setTitle("Budget Overview");
    popupStage.setScene(scene);
    popupStage.show();
}
@FXML
private void handleChatButton() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatWindow.fxml"));
        Parent root = loader.load();

        // Create a new stage for the chat window
        Stage chatStage = new Stage();
        chatStage.setTitle("Chat with Me");
        chatStage.setScene(new Scene(root, 300, 300)); // Set the size of the chat window

        // Set the chat window properties
        chatStage.initModality(Modality.NONE); // This makes the window non-modal, allowing interaction with the main window
        chatStage.initOwner(chatButton.getScene().getWindow()); // Set the owner if needed

        chatStage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}




@FXML
private void handleShowChart1(){
CategoryAxis xAxis = new CategoryAxis();
NumberAxis yAxis = new NumberAxis();
xAxis.setLabel("Time Period");
yAxis.setLabel("Amount Spent");
LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
lineChart.setTitle("Expense Tracking Over Time");
Map<String, Double> expensesByMonth = new HashMap<>();
for (Expense expense : expensesTable.getItems()) {
    // Assuming Expense has a getDate() method returning LocalDate and getAmount() method
    String monthKey = expense.getDate().getMonth() + " " + expense.getDate().getYear();
    expensesByMonth.merge(monthKey, (double) expense.getAmount(), Double::sum);

}

XYChart.Series<String, Number> series = new XYChart.Series<>();
series.setName("Monthly Expenses");

// Sorting the months might be necessary if you want the chart to display them in order
List<String> sortedMonths = new ArrayList<>(expensesByMonth.keySet());
// Sort or order 'sortedMonths' as needed, especially if not using a sortable key format

for (String month : sortedMonths) {
    series.getData().add(new XYChart.Data<>(month, expensesByMonth.get(month)));
}

lineChart.getData().add(series);

VBox layout = new VBox(lineChart);
layout.setAlignment(Pos.CENTER);
Scene scene = new Scene(layout, 800, 600); // Adjust size as needed

Stage popupStage = new Stage();
popupStage.setTitle("Expense Tracking");
popupStage.setScene(scene);
popupStage.show();

}





public void updateAssetAllocationChart() {
    //System.out.println("Updating Asset Allocation Chart...");
    //ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    
    // Example: Summarize investment by category
    //Map<String, Double> categorySums = new HashMap<>();
    //for (Investment investment : investmentsTable.getItems()) {
     //   categorySums.merge(investment.getCategory(), investment.getAmountInvested(), Double::sum);
   // }
    //    String category = investment.getCategory();
    //    Double amount = categorySums.getOrDefault(category, 0.0);
     //   categorySums.put(category, amount + investment.getAmountInvested());
   // }
    
    //categorySums.forEach((category, sum) -> {
    //    pieChartData.add(new PieChart.Data(category, sum));
    //});
    
    //assetAllocationChart.setData(pieChartData);
    //ObservableList<PieChart.Data> pieChartData =
           // FXCollections.observableArrayList(
               // new PieChart.Data("Stocks", 30),
               // new PieChart.Data("Bonds", 20),
               // new PieChart.Data("Real Estate", 50));
    //assetAllocationChart.setData(pieChartData);
   // Map<String, Double> investmentByCategory = new HashMap<>();
        // Aggregate investments by category
        
        //ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        // Convert the aggregated data into PieChart.Data objects and add them to pieChartData
        
        //assetAllocationChart.setData(pieChartData);
        
        Map<String, Double> investmentByCategory = new HashMap<>();
    for (Investment investment : investmentsTable.getItems()) {
        investmentByCategory.merge(investment.getCategory(), investment.getAmountInvested(), Double::sum);
    }

    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    
    investmentByCategory.forEach((category, sum) -> {
        
        pieChartData.add(new PieChart.Data(category, sum));
    });

   //assetAllocationChart.setData(pieChartData);
   // Create a new PieChart with the data
    PieChart pieChart = new PieChart(pieChartData);

    // Setting up the Stage and Scene for the popup
    //Stage popupStage = new Stage();
    //popupStage.setTitle("Asset Allocation Chart");

    // Configuring the PieChart display properties if needed
    pieChart.setPrefSize(400, 300); // Adjust size as needed

    // Creating a layout for the PieChart
   // VBox layout = new VBox(pieChart);
   // layout.setAlignment(Pos.CENTER);

   // Scene scene = new Scene(layout);
    //popupStage.setScene(scene);

    // Show the popup stage
   // popupStage.show();
   pieChart.setLegendVisible(true); // Optionally display the legend
    pieChart.setTitle("Asset Allocation"); // Optionally set a title

    // Prepare and show the popup
    Stage popupStage = new Stage();
    popupStage.setTitle("Asset Allocation Chart");
    VBox layout = new VBox(pieChart); // Use VBox for layout
    layout.setAlignment(Pos.CENTER); // Center the PieChart
    Scene scene = new Scene(layout); // Create a scene with the layout
    popupStage.setScene(scene); // Set the scene to the stage
    popupStage.show(); //
}
private void setupTableViews() {
        // Initialize and bind your TableView columns here.
        // Example:
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        // Do this for all TableView columns.
        
        // Initialize your TableViews with empty ObservableLists to avoid NullPointerExceptions.
        expensesTable.setItems(FXCollections.observableArrayList());
        budgetsTable.setItems(FXCollections.observableArrayList());
        investmentsTable.setItems(FXCollections.observableArrayList());
    }

    // Initialize method if needed
    public void initialize() {
        // Initialization code can go here
        // Initialize the TableView columns and set up data binding here
        // Example for one column:
       categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    expensesTable.setItems(FXCollections.observableArrayList()); // Initialize your TableView with an empty list
    
    budgetCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    allocatedAmountColumn.setCellValueFactory(new PropertyValueFactory<>("allocatedAmount"));
    spentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("spentAmount"));

    budgetsTable.setItems(FXCollections.observableArrayList()); // Initialize with 
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    targetAmountColumn.setCellValueFactory(new PropertyValueFactory<>("targetAmount"));
    currentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("currentAmount"));

    goalsTable.setItems(FXCollections.observableArrayList()); // Initialize with an empty list
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    amountInvestedColumn.setCellValueFactory(new PropertyValueFactory<>("amountInvested"));
    currentValueColumn.setCellValueFactory(new PropertyValueFactory<>("currentValue"));
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    
    // Assuming you're initializing a TableView named investmentsTable
    investmentsTable.setItems(FXCollections.observableArrayList()); // Initialize with an empty list or your data
    billNameColumn.setCellValueFactory(new PropertyValueFactory<>("billName"));
    amountDueColumn.setCellValueFactory(new PropertyValueFactory<>("amountDue"));
    dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    isPaidColumn.setCellValueFactory(new PropertyValueFactory<>("isPaid"));

    //billsTable.setItems(billData);
    //notificationService = new NotificationService();
       // notificationService.startScheduledCheck();
        
       setupTableViews();  // Set up your tables
    initializeBillNotifications(); 
  //  ObservableList<PieChart.Data> pieChartData =
           // FXCollections.observableArrayList(
               // new PieChart.Data("Stocks", 30),
               // new PieChart.Data("Bonds", 20),
               // new PieChart.Data("Real Estate", 50));
   // assetAllocationChart.setData(pieChartData);
    //assetAllocationChart.setTitle("Asset Allocation");
   // XYChart.Series<String, Number> series = new XYChart.Series<>();
   // series.getData().add(new XYChart.Data<>("Jan", 10000));
   // series.getData().add(new XYChart.Data<>("Feb", 10500));
    //series.getData().add(new XYChart.Data<>("Mar", 11000));
    // Add more data as needed

  //  investmentGrowthChart.getData().add(series);
   // investmentGrowthChart.setTitle("Investment Growth Over Time");
    
    //XYChart.Series<String, Number> series1 = new XYChart.Series<>();
   // series1.setName("Initial Amount");
   // series1.getData().add(new XYChart.Data<>("Stocks", 7000));
    //series1.getData().add(new XYChart.Data<>("Bonds", 3000));
    
   // XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    //series2.setName("Current Value");
   // series2.getData().add(new XYChart.Data<>("Stocks", 10000));
   // series2.getData().add(new XYChart.Data<>("Bonds", 3500));

  // investmentComparisonChart.getData().addAll(series1, series2);
    //investmentComparisonChart.setTitle("Investment Value Comparison");
    setupTableViews();
//updateAssetAllocationChart(); // Call this method upon initialization
//ObservableList<PieChart.Data> pieChartData =
        //FXCollections.observableArrayList(
           // new PieChart.Data("Sample 1", 30),
           // new PieChart.Data("Sample 2", 70));
   // assetAllocationChart.setData(pieChartData);

    }
    private void populateExpensesTable() {
        // Populate your table with data from your data source (e.g., in-memory list, database)
    }
    
    
}

