package org.example.langjavafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController {
    @FXML
    private Label selectLanguage;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label firstName;
    @FXML
    private Label lastName;
    @FXML
    private Label email;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;

    private ResourceBundle bundle;
    private Locale locale;

    private String selectedLanguage;


    public void initialize() {
        languageComboBox.getItems().addAll("English", "Farsi", "Japanese");
        languageComboBox.setValue("English");

        Font font = Font.font("Arial", 14); // Change "Arial" to your desired font and 20 to your desired size

        selectLanguage.setFont(font);
        firstName.setFont(font);
        lastName.setFont(font);
        email.setFont(font);
        firstNameTextField.setFont(font);
        lastNameTextField.setFont(font);
        emailTextField.setFont(font);
    }

    public Connection getDatabaseConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/localization";
        String username = "root";
        String password = "blinov123";
        return DriverManager.getConnection(url, username, password);
    }

    public void languageBundle(String language, String country) {
        locale = new Locale(language, country);
        bundle = ResourceBundle.getBundle("messages", locale);

        selectLanguage.setText(bundle.getString("selectLanguage"));
        firstName.setText(bundle.getString("firstName"));
        lastName.setText(bundle.getString("lastName"));
        email.setText(bundle.getString("email"));
    }

    public void languageComboBoxOnAction(ActionEvent event) {
        String language = "en";
        String country = "US";
        switch (languageComboBox.getValue()) {
            case "Farsi":
                language = "fa";
                country = "IR";
                break;
            case "Japanese":
                language = "jp";
                country = "JP";
                break;
            default:
                break;
        }
        languageBundle(language, country);
    }

    public void saveButtonAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Database Save Options");
        alert.setHeaderText("Choose a save option:");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Field Localization");
        ButtonType buttonTypeTwo = new ButtonType("Row Localization");
        ButtonType buttonTypeThree = new ButtonType("Table-Based Localization");

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // handle field localization
            handleFieldLocalization();
        } else if (result.get() == buttonTypeTwo) {
            // handle row localization
            handleRowLocalization();
        } else if (result.get() == buttonTypeThree) {
            // handle table-based localization
            handleTableBasedLocalization();
        } else {
            // user closed the dialog
            alert.close();
        }
    }

    public void handleFieldLocalization() {
        selectedLanguage = languageComboBox.getValue();
        String languageId;

        switch (selectedLanguage) {
            case "Farsi":
                languageId = "fa";
                break;
            case "Japanese":
                languageId = "jp";
                break;
            default:
                languageId = "en";
                break;
        }

        try {
            Connection conn = getDatabaseConnection();

            String sql = "INSERT INTO employee_field (first_name_" + languageId + ", last_name_" + languageId + ", email_" + languageId + ") VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, firstNameTextField.getText()); // Set name
            pstmt.setString(2, lastNameTextField.getText()); // Set last name
            pstmt.setString(3, emailTextField.getText()); // Set email


            // Execute the query
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");

            // Close the resources
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        };
    }

    public void handleRowLocalization() {
        selectedLanguage = languageComboBox.getValue();
        String languageId;

        switch (selectedLanguage) {
            case "Farsi":
                languageId = "fa";
                break;
            case "Japanese":
                languageId = "jp";
                break;
            default:
                languageId = "en";
                break;
        }

        try {
            Connection conn = getDatabaseConnection();

            String sql = "INSERT INTO employee_row (lang_id, first_name, last_name, email) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, languageId); // Set language id
            pstmt.setString(2, firstNameTextField.getText()); // Set name
            pstmt.setString(3, lastNameTextField.getText()); // Set last name
            pstmt.setString(4, emailTextField.getText()); // Set email


            // Execute the query
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");

            // Close the resources
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        };
    }

    public void handleTableBasedLocalization() {
        selectedLanguage = languageComboBox.getValue();
        String tableName;

        switch (selectedLanguage) {
            case "Farsi":
                tableName = "employee_fa";
                break;
            case "Japanese":
                tableName = "employee_ja";
                break;
            default:
                tableName = "employee_en";
                break;
        }

        try {
            Connection conn = getDatabaseConnection();

            String sql = "INSERT INTO " + tableName + " (first_name, last_name, email) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, firstNameTextField.getText()); // Set name
            pstmt.setString(2, lastNameTextField.getText()); // Set last name
            pstmt.setString(3, emailTextField.getText()); // Set email

            // Execute the query
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) inserted.");

            // Close the resources
            pstmt.close();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        };
    }

}