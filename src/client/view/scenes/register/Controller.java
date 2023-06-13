package client.view.scenes.register;

import client.controller.AudioController;
import client.controller.UserController;
import client.view.scenes.Utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Controller implements Initializable
{
    // clang-format off
    @FXML public TextField username;
    // @FXML public TextField email;
    @FXML public PasswordField password;
    @FXML public PasswordField passwordVerification;
    @FXML public Text feedback;
    // clang-format on

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.username.clear();
        // this.email.clear();
        this.password.clear();
        this.passwordVerification.clear();
        this.feedback.setText("");
    }

    /**
     * Registers a user if textfields corresponding to username, password,
     * verify password and email are filled in correctly.
     */
    public void onClickRegister(ActionEvent event)
    {
        if (!this.fieldsAreFilled())
        {
            AudioController.play(AudioController.error);
            this.feedback.setText("Vennligst fyll ut alle feltene");
            return;
        }
        // if (!this.emailIsValid())
        // {
        //     AudioController.play(AudioController.error);
        //     this.feedback.setText("Oppgi en gyldig Epost addresse");
        //     return;
        // }
        if (!this.passwordIsValid())
        {
            AudioController.play(AudioController.error);
            this.feedback.setText("Passord må være lenger enn 3 tegn");
            this.password.clear();
            this.passwordVerification.clear();
            return;
        }
        if (!this.passwordVerificationIsValid())
        {
            AudioController.play(AudioController.error);
            this.feedback.setText("Passordene var ikke like. Prøv igjen");
            this.password.clear();
            this.passwordVerification.clear();
            return;
        }

        // Registration is valid
        AudioController.play(AudioController.click);
        UserController.register(
            this.username.getText(), this.password.getText() /*, this.email.getText()*/
        );
    }

    private boolean fieldsAreFilled()
    {
        if (!Utils.hasContent(this.username)) return false;
        // if (this.isEmpty(this.email)) return false;
        if (!Utils.hasContent(this.password)) return false;
        if (!Utils.hasContent(this.passwordVerification)) return false;
        return true;
    }

    // private boolean emailIsValid()
    // {
    //     String text = this.email.getText();
    //     if (!text.contains("@")) return false;
    //     if (!text.contains(".")) return false;
    //     if (text.length() < 5) return false;
    //     return true;
    // }

    private boolean passwordIsValid()
    {
        String text = this.password.getText();
        if (text.length() <= 3) return false;
        return true;
    }

    private boolean passwordVerificationIsValid()
    {
        String password = this.password.getText();
        String verification = this.passwordVerification.getText();
        return verification.equals(password);
    }
}
