package deadwood;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class SelectNumOfPlayersController {

    @FXML
    private ComboBox<Integer> selectNumOfPlayers;

    @FXML
    private void initialize() {
        selectNumOfPlayers.getItems().addAll(2, 3, 4, 5, 6, 7, 8);
        selectNumOfPlayers.getSelectionModel().selectFirst();
    }

    @FXML
    private void onNumOfPlayersSelected() {
        int numOfPlayers = selectNumOfPlayers.getValue();
        Deadwood.startGame(numOfPlayers);
    }
}
