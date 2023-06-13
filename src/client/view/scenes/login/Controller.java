package client.view.scenes.login;

import client.App;
import client.controller.AudioController;
import client.controller.MusicController;
import client.controller.UserController;
import client.view.scenes.Utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;

public class Controller implements Initializable
{
    // clang-format off
    @FXML public TextField username;
    @FXML public PasswordField password;
    @FXML public Text feedback;
    @FXML public MediaView backgroundVideo;
    // clang-format on
    private String video = "resources/video/intro.mp4";

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.username.clear();
        this.password.clear();
        this.feedback.setText("");
        if (this.backgroundVideo.getMediaPlayer() == null)
        {
            Media media = new Media(App.getPath(this.video));
            MediaPlayer player = new MediaPlayer(media);
            player.setAutoPlay(false);
            player.setCycleCount(100);
            this.backgroundVideo.setMediaPlayer(player);
        }
    }

    /**
     * Verify that contents of both username and password is valid.
     * If valid, move to main menu, otherwise give proper feedback.
     */
    public void onClickLogin(ActionEvent event)
    {
        if (!this.fieldsAreFilled())
        {
            AudioController.play(AudioController.error);
            this.feedback.setText("Vennligst fyll ut alle feltene");
            return;
        }
        if (!UserController.login(this.username.getText(), this.password.getText()))
        {
            AudioController.play(AudioController.error);
            this.feedback.setText("Feil brukernavn eller passord");
            this.password.clear();
            return;
        }
        // Successful login
        AudioController.play(AudioController.click);
    }

    public void playBackgroundVideo()
    {
        this.backgroundVideo.getMediaPlayer().play();
        MusicController.start(MusicController.intro);
    }

    public void stopBackgroundVideo()
    {
        this.backgroundVideo.getMediaPlayer().stop();
        MusicController.stop();
    }

    private boolean fieldsAreFilled()
    {
        if (!Utils.hasContent(this.password)) return false;
        if (!Utils.hasContent(this.username)) return false;
        return true;
    }
}
