package test.com.javafx.registrationform;

import com.javafx.registrationform.RegistrationApp;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.testng.annotations.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;

public class RegistrationAppUITest {

    private static boolean javafxInitialized = false;

    private RegistrationApp app;
    private Stage stage;

    @BeforeClass
    public void initJavaFX() throws Exception {
        if (!javafxInitialized) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown); // Initialize JavaFX
            latch.await(5, TimeUnit.SECONDS);
            javafxInitialized = true;
        }
    }

    @BeforeMethod
    public void setup() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                app = new RegistrationApp();
                stage = new Stage();
                app.start(stage); // Start the JavaFX app
            } catch (Exception e) {
                fail("Failed to start app: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS); // Wait for JavaFX thread
    }

    @AfterMethod
    public void teardown() throws Exception {
        if (stage != null) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                stage.close();
                latch.countDown();
            });
            latch.await(2, TimeUnit.SECONDS);
        }
    }

    @Test
    public void testSubmitValidForm() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            TextField txtFullName = (TextField) stage.getScene().lookup("#txtFullName");
            TextField txtEmail = (TextField) stage.getScene().lookup("#txtEmail");
            PasswordField txtPassword = (PasswordField) stage.getScene().lookup("#txtPassword");
            ComboBox<String> cbCountry = (ComboBox<String>) stage.getScene().lookup("#cbCountry");
            RadioButton rbFemale = (RadioButton) stage.getScene().lookup("#rbFemale");
            CheckBox chkSubscribe = (CheckBox) stage.getScene().lookup("#chkSubscribe");
            Button btnSubmit = (Button) stage.getScene().lookup("#btnSubmit");
            Label lblErrors = (Label) stage.getScene().lookup("#lblErrors");

            // Fill form
            txtFullName.setText("Sakshi Narute");
            txtEmail.setText("test@example.com");
            txtPassword.setText("pass123");
            cbCountry.getSelectionModel().select("India");
            rbFemale.setSelected(true);
            chkSubscribe.setSelected(true);

            btnSubmit.fire(); 

            assertEquals(lblErrors.getText(), ""); 
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testClearForm() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            TextField txtFullName = (TextField) stage.getScene().lookup("#txtFullName");
            TextField txtEmail = (TextField) stage.getScene().lookup("#txtEmail");
            PasswordField txtPassword = (PasswordField) stage.getScene().lookup("#txtPassword");
            ComboBox<String> cbCountry = (ComboBox<String>) stage.getScene().lookup("#cbCountry");
            CheckBox chkSubscribe = (CheckBox) stage.getScene().lookup("#chkSubscribe");
            Button btnClear = (Button) stage.getScene().lookup("#btnClear");

            // Fill form
            txtFullName.setText("Hello");
            txtEmail.setText("a@b.com");
            txtPassword.setText("123456");
            cbCountry.getSelectionModel().select("India");
            chkSubscribe.setSelected(true);

            btnClear.fire(); // Trigger Clear button

            // Verify cleared
            assertEquals(txtFullName.getText(), "");
            assertEquals(txtEmail.getText(), "");
            assertEquals(txtPassword.getText(), "");
            assertNull(cbCountry.getValue());
            assertFalse(chkSubscribe.isSelected());
            latch.countDown();
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}
