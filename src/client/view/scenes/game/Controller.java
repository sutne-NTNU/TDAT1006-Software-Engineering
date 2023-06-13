package client.view.scenes.game;

import static client.model.game.Municipality.*;
import static java.util.Map.entry;

import client.controller.AudioController;
import client.controller.ChatController;
import client.controller.GameController;
import client.model.chat.ChatMessage;
import client.model.game.Municipality;
import client.model.game.Player;
import client.utils.Bot;
import client.view.scenes.Utils;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
// import java.util.Map.Entry;
// import java.util.Objects;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller implements Initializable
{
    // clang-format off
    // Current Player Info
    @FXML private VBox currentPlayerBox;
    @FXML private Text currentPlayerName;
    // Winner Info
    @FXML private VBox winnerBox;
    @FXML private Text winnerName;
    @FXML private Text winnerParty;
    // Feedback from map interaction
    @FXML private Text selectedMunicipality;
    @FXML private Text attackResult;
    // Buttons
    @FXML private Button btnEndTurn;
    // Chat (public to be accessible from ChatController)
    @FXML private ScrollPane chatWindow;
    @FXML private TextFlow chat;
    @FXML private TextField chatMessage;
    // clang-format on

    final InnerShadow unfocusedEffect
        = new InnerShadow(Double.MAX_VALUE, Color.web("rgba(0,0,0, 0.5)"));
    final InnerShadow selectedEffect = new InnerShadow(30, Color.BLACK);

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        this.winnerBox.setVisible(false);
        this.btnEndTurn.setVisible(false);
        this.currentPlayerBox.setVisible(true);
        this.selectedMunicipality.setText("");
        this.attackResult.setText("");
        this.chat.getChildren().clear();
        this.chatMessage.clear();
    }

    public void onClickSend(ActionEvent event)
    {
        if (!Utils.hasContent(this.chatMessage)) return;
        ChatMessage message = ChatController.initMessage(this.chatMessage.getText());
        this.updateChat(message);
        this.chatMessage.clear();
        for (Player player : GameController.game.remainingPlayers)
        {
            Bot.promptForMessage(player);
        }
    }

    public void onClickLeaveGame(ActionEvent event)
    {
        AudioController.play(AudioController.click);
        GameController.leaveGame();
    }

    public void onClickEndTurn(ActionEvent event)
    {
        AudioController.play(AudioController.click);
        GameController.endTurn();
    }

    public void onClickMunicipality(MouseEvent event)
    {
        GameController.onSelectMunicipality(this.getMunicipalityIndex(event));
    }

    public void onMouseEnterMunicipality(MouseEvent event)
    {
        GameController.onHoverMunicipality(this.getMunicipalityIndex(event), true);
    }

    public void onMouseLeaveMunicipality(MouseEvent event)
    {
        GameController.onHoverMunicipality(this.getMunicipalityIndex(event), false);
    }

    /**
     * Hide and shows relevant information and buttons.
     * Highlight the winning player.
     */
    public void updateWinner(Player winner)
    {
        // Hide what is no longer needed
        this.currentPlayerBox.setVisible(false);
        this.btnEndTurn.setVisible(false);
        this.selectedMunicipality.setText("");
        // Update winner info
        this.winnerBox.setVisible(true);
        this.winnerName.setText(winner.username);
        this.winnerParty.setText(winner.party.name);
        this.winnerName.setFill(winner.party.color);
        this.winnerParty.setFill(winner.party.color);
    }

    public void updateChat(ChatMessage message)
    {
        Text[] texts = ChatController.convertToTexts(message);
        this.chat.getChildren().addAll(texts);
        this.chat.autosize();
        this.chatWindow.setVvalue(1);
    }

    /**
     * Get current player from GameController and update the visual representation accordingly.
     */
    public void updateCurrentPlayer(Player player, boolean isMyTurn)
    {
        this.currentPlayerBox.setVisible(true);
        this.currentPlayerName.setText(player.username);
        this.currentPlayerName.setFill(player.party.color);
        this.btnEndTurn.setVisible(isMyTurn);
    }

    public void updateSelectedMunicipality(Municipality municipality, boolean isSelected)
    {

        if (municipality == null) return;
        this.getSVG(municipality).setEffect(isSelected ? this.selectedEffect : null);
        this.selectedMunicipality.setText(isSelected ? municipality.name : "");
    }

    /**
     * Restore map to owner colors and update number of dice.
     */
    public void updateMap(Municipality[] municipalities)
    {
        AudioController.play(AudioController.mapChanged);
        for (Municipality municipality : municipalities)
        {
            SVGPath svg = this.getSVG(municipality);
            if (municipality.owner == null)
                svg.setFill(Color.web("white"));
            else
                svg.setFill(municipality.owner.party.color);
            svg.setEffect(null);
            this.getText(municipality).setText("" + municipality.numDice);
        }
    }

    /**
     * Increase the focus on the given municipalities.
     */
    public void updateFocus(Municipality municipality, boolean isFocused)
    {
        this.getSVG(municipality).setEffect(isFocused ? null : this.unfocusedEffect);
    }

    public void updateAttackResult(Municipality attacker, Municipality defender, boolean won)
    {
        if (won)
        {
            this.updateAttackResult(attacker.name + " overvant " + defender.name);
        }
        else
        {
            this.updateAttackResult(defender.name + " motsto " + attacker.name);
        }
    }
    public void updateAttackResult(String newText)
    {
        this.attackResult.setText(newText);
    }

    /*
     *
     * Convenient access to all the municipality SVG and Text objects
     *
     */

    private int getMunicipalityIndex(MouseEvent event)
    {
        SVGPath svg = (SVGPath)event.getSource();
        return getKeyByValue(this.svgs(), svg);
    }

    private SVGPath getSVG(Municipality municipality)
    {
        return this.svgs().get(municipality.index);
    }

    private Text getText(Municipality municipality)
    {
        return this.texts().get(municipality.index);
    }

    private Map<Integer, SVGPath> svgs() // must be function as all values are null on init
    {
        return Map.ofEntries(
            entry(LIERNE, this.svgLierne),
            entry(SNAASA, this.svgSnaasa),
            entry(GRONG, this.svgGrong),
            entry(OVERHALLA, this.svgOverhalla),
            entry(NAMSOS, this.svgNamsos),
            entry(STEINKJER, this.svgSteinkjer),
            entry(INDEROEY, this.svgInderoy),
            entry(NAMDALSEID, this.svgNamdaleid),
            entry(VERDAL, this.svgVerdal),
            entry(FLATANGER, this.svgFlatanger),
            entry(OSEN, this.svgOsen),
            entry(ROAN, this.svgRoan),
            entry(AAFJORD, this.svgAfjord),
            entry(VERRAN, this.svgVerran),
            entry(RINNDAL, this.svgRinndal),
            entry(MERAAKER, this.svgMeraker),
            entry(STJOERDAL, this.svgStjordal),
            entry(LEVANGER, this.svgLevanger),
            entry(LEKSVIK, this.svgLeksvik),
            entry(RISSA, this.svgRissa),
            entry(BJUGN, this.svgBjugn),
            entry(TYDAL, this.svgTydal),
            entry(SELBU, this.svgSelbu),
            entry(MALVIK, this.svgMalvik),
            entry(TRONDHEIM, this.svgTrondheim),
            entry(KLAEBU, this.svgKlaebu),
            entry(MELHUS, this.svgMelhus),
            entry(AGDENES, this.svgAgdenes),
            entry(SKAUN, this.svgSkaun),
            entry(ORKDAL, this.svgOrkdal),
            entry(MELDAL, this.svgMeldal),
            entry(SURNADAL, this.svgSurnadal),
            entry(ROEROS, this.svgRoros),
            entry(HOLTALEN, this.svgHoltalen),
            entry(MIDTREGAULDAL, this.svgMidtregauldal),
            entry(SUNNDAL, this.svgSunndal),
            entry(RENNEBU, this.svgRennebu),
            entry(OPPDAL, this.svgOppdal),
            entry(SNILLFJORD, this.svgSnillfjord),
            entry(HEMNE, this.svgHemne),
            entry(HITRA, this.svgHitra),
            entry(AURE, this.svgAure)
        );
    }

    private Map<Integer, Text> texts()
    {
        return Map.ofEntries(
            entry(LIERNE, this.txtLierne),
            entry(SNAASA, this.txtSnaasa),
            entry(GRONG, this.txtGrong),
            entry(OVERHALLA, this.txtOverhalla),
            entry(NAMSOS, this.txtNamsos),
            entry(STEINKJER, this.txtSteinkjer),
            entry(INDEROEY, this.txtInderoy),
            entry(NAMDALSEID, this.txtNamdaleid),
            entry(VERDAL, this.txtVerdal),
            entry(FLATANGER, this.txtFlatanger),
            entry(OSEN, this.txtOsen),
            entry(ROAN, this.txtRoan),
            entry(AAFJORD, this.txtAfjord),
            entry(VERRAN, this.txtVerran),
            entry(RINNDAL, this.txtRinndal),
            entry(MERAAKER, this.txtMeraker),
            entry(STJOERDAL, this.txtStjordal),
            entry(LEVANGER, this.txtLevanger),
            entry(LEKSVIK, this.txtLeksvik),
            entry(RISSA, this.txtRissa),
            entry(BJUGN, this.txtBjugn),
            entry(TYDAL, this.txtTydal),
            entry(SELBU, this.txtSelbu),
            entry(MALVIK, this.txtMalvik),
            entry(TRONDHEIM, this.txtTrondheim),
            entry(KLAEBU, this.txtKlaebu),
            entry(MELHUS, this.txtMelhus),
            entry(AGDENES, this.txtAgdenes),
            entry(SKAUN, this.txtSkaun),
            entry(ORKDAL, this.txtOrkdal),
            entry(MELDAL, this.txtMeldal),
            entry(SURNADAL, this.txtSurnadal),
            entry(ROEROS, this.txtRoros),
            entry(HOLTALEN, this.txtHoltalen),
            entry(MIDTREGAULDAL, this.txtMidtregauldal),
            entry(SUNNDAL, this.txtSunndal),
            entry(RENNEBU, this.txtRennebu),
            entry(OPPDAL, this.txtOppdal),
            entry(SNILLFJORD, this.txtSnillfjord),
            entry(HEMNE, this.txtHemne),
            entry(HITRA, this.txtHitra),
            entry(AURE, this.txtAure)
        );
    }

    // clang-format off
    @FXML private SVGPath svgLierne;
    @FXML private SVGPath svgSnaasa;
    @FXML private SVGPath svgGrong;
    @FXML private SVGPath svgOverhalla;
    @FXML private SVGPath svgNamsos;
    @FXML private SVGPath svgSteinkjer;
    @FXML private SVGPath svgInderoy;
    @FXML private SVGPath svgNamdaleid;
    @FXML private SVGPath svgVerdal;
    @FXML private SVGPath svgFlatanger;
    @FXML private SVGPath svgOsen;
    @FXML private SVGPath svgRoan;
    @FXML private SVGPath svgAfjord;
    @FXML private SVGPath svgVerran;
    @FXML private SVGPath svgRinndal;
    @FXML private SVGPath svgMeraker;
    @FXML private SVGPath svgStjordal;
    @FXML private SVGPath svgLevanger;
    @FXML private SVGPath svgLeksvik;
    @FXML private SVGPath svgRissa;
    @FXML private SVGPath svgBjugn;
    @FXML private SVGPath svgTydal;
    @FXML private SVGPath svgSelbu;
    @FXML private SVGPath svgMalvik;
    @FXML private SVGPath svgTrondheim;
    @FXML private SVGPath svgKlaebu;
    @FXML private SVGPath svgMelhus;
    @FXML private SVGPath svgAgdenes;
    @FXML private SVGPath svgSkaun;
    @FXML private SVGPath svgOrkdal;
    @FXML private SVGPath svgMeldal;
    @FXML private SVGPath svgSurnadal;
    @FXML private SVGPath svgRoros;
    @FXML private SVGPath svgHoltalen;
    @FXML private SVGPath svgMidtregauldal;
    @FXML private SVGPath svgSunndal;
    @FXML private SVGPath svgRennebu;
    @FXML private SVGPath svgOppdal;
    @FXML private SVGPath svgSnillfjord;
    @FXML private SVGPath svgHemne;
    @FXML private SVGPath svgHitra;
    @FXML private SVGPath svgAure;

    @FXML private Text txtLierne;
    @FXML private Text txtSnaasa;
    @FXML private Text txtGrong;
    @FXML private Text txtOverhalla;
    @FXML private Text txtNamsos;
    @FXML private Text txtSteinkjer;
    @FXML private Text txtInderoy;
    @FXML private Text txtNamdaleid;
    @FXML private Text txtVerdal;
    @FXML private Text txtFlatanger;
    @FXML private Text txtOsen;
    @FXML private Text txtRoan;
    @FXML private Text txtAfjord;
    @FXML private Text txtVerran;
    @FXML private Text txtRinndal;
    @FXML private Text txtMeraker;
    @FXML private Text txtStjordal;
    @FXML private Text txtLevanger;
    @FXML private Text txtLeksvik;
    @FXML private Text txtRissa;
    @FXML private Text txtBjugn;
    @FXML private Text txtTydal;
    @FXML private Text txtSelbu;
    @FXML private Text txtMalvik;
    @FXML private Text txtTrondheim;
    @FXML private Text txtKlaebu;
    @FXML private Text txtMelhus;
    @FXML private Text txtAgdenes;
    @FXML private Text txtSkaun;
    @FXML private Text txtOrkdal;
    @FXML private Text txtMeldal;
    @FXML private Text txtSurnadal;
    @FXML private Text txtRoros;
    @FXML private Text txtHoltalen;
    @FXML private Text txtMidtregauldal;
    @FXML private Text txtSunndal;
    @FXML private Text txtRennebu;
    @FXML private Text txtOppdal;
    @FXML private Text txtSnillfjord;
    @FXML private Text txtHemne;
    @FXML private Text txtHitra;
    @FXML private Text txtAure;
    // clang-format on

    /** Allows getting the integer of a municipality from mousevents on its SVGPath */
    private static <T, E> T getKeyByValue(Map<T, E> map, E value)
    {
        for (Entry<T, E> entry : map.entrySet())
        {
            if (Objects.equals(value, entry.getValue()))
            {
                return entry.getKey();
            }
        }
        return null;
    }
}
