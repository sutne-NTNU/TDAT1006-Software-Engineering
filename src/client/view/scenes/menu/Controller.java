package client.view.scenes.menu;

import client.controller.AudioController;
import client.controller.LobbyController;
import client.controller.UserController;
import javafx.event.ActionEvent;

public class Controller
{
    public void onClickJoinLobby(ActionEvent event)
    {
        AudioController.play(AudioController.click);
        LobbyController.joinLobby();
    }

    public void onClickGoToStats(ActionEvent event)
    {
        // App.setScene(App.stats);
    }

    public void onClickSignOut(ActionEvent event)
    {
        AudioController.play(AudioController.click);
        UserController.signOut();
    }
}
