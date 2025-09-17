package com.javafx.registrationform;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.regex.Pattern;

public class RegistrationController {

    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnSubmit;
    @FXML private Button btnClear;
    @FXML private ComboBox<String> cbCountry;
    @FXML private CheckBox chkSubscribe;
    @FXML private Label lblStatus;
    @FXML private Label lblLivePreview;
    @FXML private RadioButton rbMale;
    @FXML private RadioButton rbFemale;
    @FXML private RadioButton rbOther;
    @FXML private GridPane formGrid;
    @FXML private Label lblErrors;
    @FXML private MenuItem menuExit;
    @FXML private MenuItem menuAbout;

    private ToggleGroup genderGroup = new ToggleGroup();
    private Pattern emailPattern = Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private Pattern pwdPattern = Pattern.compile("^(?=.*\\d).{6,}$");

    @FXML
    public void initialize() {
        // --- ComboBox items ---
        cbCountry.getItems().addAll("India", "United States", "United Kingdom", "Australia");

        // --- ToggleGroup for gender ---
        rbMale.setToggleGroup(genderGroup);
        rbFemale.setToggleGroup(genderGroup);
        rbOther.setToggleGroup(genderGroup);

        // --- Live preview binding ---
        lblLivePreview.textProperty().bind(txtFullName.textProperty());

        // --- Form validation ---
        BooleanBinding formInvalid = txtFullName.textProperty().isEmpty()
                .or(txtFullName.textProperty().length().lessThan(3))
                .or(txtEmail.textProperty().isEmpty())
                .or(txtPassword.textProperty().isEmpty())
                .or(cbCountry.valueProperty().isNull())
                .or(genderGroup.selectedToggleProperty().isNull());

        btnSubmit.disableProperty().bind(formInvalid);

        // --- Real-time validation ---
        txtFullName.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.length() > 0 && newVal.length() < 3) {
                lblErrors.setText("❌ Full Name must be at least 3 characters");
            } else {
                lblErrors.setText("");
            }
        });

        txtEmail.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !emailPattern.matcher(newVal).matches()) {
                lblErrors.setText("❌ Invalid email format");
            } else {
                lblErrors.setText("");
            }
        });

        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !pwdPattern.matcher(newVal).matches()) {
                lblErrors.setText("❌ Password must be ≥6 chars & contain a number");
            } else {
                lblErrors.setText("");
            }
        });

        // --- Event bubbling demo ---
        formGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, e ->
                lblStatus.setText("Form clicked. Target: " + e.getTarget().getClass().getSimpleName())
        );
    }

    @FXML
    private void handleSubmit() {
        String name = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String pwd = txtPassword.getText();
        String country = cbCountry.getValue();
        String gender = genderGroup.getSelectedToggle() != null
                ? ((RadioButton) genderGroup.getSelectedToggle()).getText()
                : "Unspecified";
        String subs = chkSubscribe.isSelected() ? "Yes" : "No";

        // final validation
        if (!emailPattern.matcher(email).matches()) {
            lblErrors.setText("❌ Invalid email format");
            return;
        }
        if (!pwdPattern.matcher(pwd).matches()) {
            lblErrors.setText("❌ Weak password");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Submitted");
        alert.setHeaderText("Submitted details");
        alert.setContentText(String.format(
                "Name: %s%nEmail: %s%nPassword: %s%nCountry: %s%nGender: %s%nSubscribed: %s",
                name, email, pwd, country, gender, subs
        ));
        alert.showAndWait();

        lblStatus.setText("Submitted: " + name);
        lblErrors.setText("");
    }

    @FXML
    private void handleClear() {
        txtFullName.clear();
        txtEmail.clear();
        txtPassword.clear();
        cbCountry.getSelectionModel().clearSelection();
        chkSubscribe.setSelected(false);
        genderGroup.selectToggle(null);
        lblStatus.setText("Cleared");
        lblErrors.setText("");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Simple Registration demo\nNow with Validation, Bindings, Events, CSS.");
        alert.setHeaderText("About");
        alert.showAndWait();
    }
}
