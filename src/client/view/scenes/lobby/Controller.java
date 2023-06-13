package client.view.scenes.lobby;

import client.controller.AudioController;
import client.controller.ChatController;
import client.controller.LobbyController;
import client.model.chat.ChatMessage;
import client.model.game.Player;
import client.utils.Bot;
import client.view.scenes.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;

public class Controller implements Initializable
{
    // clang-format off
    @FXML private Button btnReady;
    @FXML private Button btnChangeParty;
    @FXML private Button btnAddBot;
    @FXML private Text numPlayersReady;
    @FXML private Text numRequiredPlayersReady;
    @FXML private TableView<Player> lobby;

    @FXML private ScrollPane chatWindow;
    @FXML private TextFlow chat;
    @FXML private TextField chatMessage;
    // clang-format on

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources)
    {
        this.btnReady.setDisable(false);
        this.btnChangeParty.setDisable(false);
        this.btnAddBot.setDisable(false);
        this.chat.getChildren().clear();
        this.chatMessage.clear();
    }

    /**
     * Notify that this player is ready to start the game.
     */
    public void onClickReady(ActionEvent event)
    {
        AudioController.play(AudioController.click);
        this.btnReady.setDisable(true);
        this.btnChangeParty.setDisable(true);
        LobbyController.readyUp();
    }

    /**
     * Notify that this player would like to be assigned a new party.
     */
    public void onClickChangeParty(ActionEvent event)
    {
        AudioController.play(AudioController.click);
        LobbyController.giveMeANewParty();
    }

    public void onClickSend(ActionEvent event)
    {
        if (!Utils.hasContent(this.chatMessage)) return;
        ChatMessage message = ChatController.initMessage(this.chatMessage.getText());
        if (message == null) return;
        this.updateChat(message);
        this.chatMessage.clear();
        for (Player player : LobbyController.getPlayersInLobby())
        {
            Bot.promptForMessage(player);
        }
    }

    public void onClickAddBot(ActionEvent event)
    {
        AudioController.play(AudioController.click);
        LobbyController.addBot();
    }

    public void onClickLeaveLobby(ActionEvent event)
    {
        AudioController.play(AudioController.click);
        LobbyController.leaveLobby();
    }

    public void onLobbyFilledChange(boolean isFull)
    {
        this.btnAddBot.setDisable(isFull);
        this.btnChangeParty.setDisable(isFull);
    }

    public void updateChat(ChatMessage message)
    {
        Text[] texts = ChatController.convertToTexts(message);
        this.chat.getChildren().addAll(texts);
        this.chat.autosize();
        this.chatWindow.setVvalue(1);
    }

    public void updateReadyCount(int numReady, int numRequired)
    {
        this.numPlayersReady.setText(numReady + "");
        this.numRequiredPlayersReady.setText(numRequired + "");
    }

    @SuppressWarnings("unchecked")
    public void updatePlayerTable(Player[] players)
    {
        this.lobby.setEditable(true);
        TableColumn<Player, String> username
            = (TableColumn<Player, String>)this.lobby.getColumns().get(0);
        TableColumn<Player, String> party
            = (TableColumn<Player, String>)this.lobby.getColumns().get(1);
        // Specify which property of Player to display in each column
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        party.setCellValueFactory(new PropertyValueFactory<>("party"));
        // set table columns and data
        this.lobby.getColumns().setAll(username, party);
        this.lobby.getItems().setAll(players);
        this.addPlayerPartyColorStyleToCells(party);
        this.lobby.setEditable(false);
    }

    private void addPlayerPartyColorStyleToCells(TableColumn<Player, String> column)
    {
        column.setCellFactory(new Callback<TableColumn<Player, String>, TableCell<Player, String>>(
        ) {
            @Override
            public TableCell<Player, String> call(final TableColumn<Player, String> param)
            {
                final TableCell<Player, String> cell = new TableCell<Player, String>() {
                    @Override
                    public void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        Player player = this.getTableRow().getItem();
                        if (empty || player == null) return;
                        this.setText(item);
                        this.setStyle("-fx-text-fill: " + Utils.toCSS(player.party.color) + ";");
                    }
                };
                return cell;
            }
        });
    }
}
