package deadwood;

import deadwood.board.*;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.shape.Polygon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    @FXML private ImageView playerBlue;
    @FXML private ImageView playerCyan;
    @FXML private ImageView playerGreen;
    @FXML private ImageView playerOrange;
    @FXML private ImageView playerPink;
    @FXML private ImageView playerRed;
    @FXML private ImageView playerViolet;
    @FXML private ImageView playerYellow;

    @FXML private ImageView cardTrainStation;
    @FXML private ImageView cardJail;
    @FXML private ImageView cardMainStreet;
    @FXML private ImageView cardGeneralStore;
    @FXML private ImageView cardSaloon;
    @FXML private ImageView cardRanch;
    @FXML private ImageView cardBank;
    @FXML private ImageView cardSecretHideout;
    @FXML private ImageView cardChurch;
    @FXML private ImageView cardHotel;

    @FXML private ImageView trainStationTake1;
    @FXML private ImageView trainStationTake2;
    @FXML private ImageView trainStationTake3;
    @FXML private ImageView jailTake1;
    @FXML private ImageView mainStreetTake1;
    @FXML private ImageView mainStreetTake2;
    @FXML private ImageView mainStreetTake3;
    @FXML private ImageView generalStoreTake1;
    @FXML private ImageView generalStoreTake2;
    @FXML private ImageView saloonTake1;
    @FXML private ImageView saloonTake2;
    @FXML private ImageView ranchTake1;
    @FXML private ImageView ranchTake2;
    @FXML private ImageView bankTake1;
    @FXML private ImageView secretHideoutTake1;
    @FXML private ImageView secretHideoutTake2;
    @FXML private ImageView secretHideoutTake3;
    @FXML private ImageView churchTake1;
    @FXML private ImageView churchTake2;
    @FXML private ImageView hotelTake1;
    @FXML private ImageView hotelTake2;
    @FXML private ImageView hotelTake3;

    private ImageView[] trainStationTakes;
    private ImageView[] jailTakes;
    private ImageView[] mainStreetTakes;
    private ImageView[] generalStoreTakes;
    private ImageView[] saloonTakes;
    private ImageView[] ranchTakes;
    private ImageView[] bankTakes;
    private ImageView[] secretHideoutTakes;
    private ImageView[] churchTakes;
    private ImageView[] hotelTakes;
    private ImageView[][] takes;

    @FXML private Polygon trainStationPolygon;
    @FXML private Polygon jailPolygon;
    @FXML private Polygon mainStreetPolygon;
    @FXML private Polygon generalStorePolygon;
    @FXML private Polygon saloonPolygon;
    @FXML private Polygon trailersPolygon;
    @FXML private Polygon bankPolygon;
    @FXML private Polygon ranchPolygon;
    @FXML private Polygon castingOfficePolygon;
    @FXML private Polygon secretHideoutPolygon;
    @FXML private Polygon churchPolygon;
    @FXML private Polygon hotelPolygon;

    @FXML private ImageView currentPlayerImage;
    @FXML private Label rank;
    @FXML private Label money;
    @FXML private Label credits;
    @FXML private Label practiceChips;

    @FXML private Button takeRoleButton;
    @FXML private Button rehearseButton;
    @FXML private Button actButton;
    @FXML private Button upgradeWithMoneyButton;
    @FXML private Button upgradeWithCreditsButton;
    @FXML private ChoiceBox upgradeLevel;
    @FXML private Button endTurnButton;

    private int numOfPlayers;
    private Map<Player, ImageView> players;
    private Map<Card, ImageView> cards;
    private Map<Polygon, Place> polygons;

    private List<Role> possibleRoles;

    private boolean canDrag;
    private double originalX;
    private double originalY;
    private Place originalPlace;
    private double dragX;
    private double dragY;

    private Role currentRole; //Role that the player has landed on

    public GameController() {
        polygons = new HashMap<>();
        numOfPlayers = Deadwood.getPlayers().length;
        dragX = 0;
        dragY = 0;
        canDrag = false;
    }

    @FXML private void initialize() {
        upgradeLevel.setVisible(false);

        trainStationTakes = new ImageView[]{trainStationTake1, trainStationTake2, trainStationTake3};
        jailTakes = new ImageView[]{jailTake1};
        mainStreetTakes = new ImageView[]{mainStreetTake1, mainStreetTake2, mainStreetTake3};
        generalStoreTakes = new ImageView[]{generalStoreTake1, generalStoreTake2};
        saloonTakes = new ImageView[]{saloonTake1, saloonTake2};
        ranchTakes = new ImageView[]{ranchTake1, ranchTake2};
        bankTakes = new ImageView[]{bankTake1};
        secretHideoutTakes = new ImageView[]{secretHideoutTake1, secretHideoutTake2, secretHideoutTake3};
        churchTakes = new ImageView[]{churchTake1, churchTake2};
        hotelTakes = new ImageView[]{hotelTake1, hotelTake2, hotelTake3};
        takes = new ImageView[][]{trainStationTakes, jailTakes, mainStreetTakes, generalStoreTakes,
                saloonTakes, ranchTakes, bankTakes, secretHideoutTakes, churchTakes, hotelTakes};

        Player[] playerData = Deadwood.getPlayers();
        players = new HashMap<>(numOfPlayers);
        //create player map
        switch (numOfPlayers) {
            case 8:
                players.put(playerData[7], playerYellow);
            case 7:
                players.put(playerData[6], playerViolet);
            case 6:
                players.put(playerData[5], playerRed);
            case 5:
                players.put(playerData[4], playerPink);
            case 4:
                players.put(playerData[3], playerOrange);
            case 3:
                players.put(playerData[2], playerGreen);
            case 2:
                players.put(playerData[1], playerCyan);
            case 1:
                players.put(playerData[0], playerBlue);
        }

        //create card map
        cards = new HashMap<Card, ImageView>(10);

        //create polygon map
        polygons.put(trainStationPolygon, Board.trainStation);
        polygons.put(jailPolygon, Board.jail);
        polygons.put(mainStreetPolygon, Board.mainStreet);
        polygons.put(generalStorePolygon, Board.generalStore);
        polygons.put(saloonPolygon, Board.saloon);
        polygons.put(trailersPolygon, Board.trailers);
        polygons.put(castingOfficePolygon, Board.castingOffice);
        polygons.put(ranchPolygon, Board.ranch);
        polygons.put(secretHideoutPolygon, Board.secretHideout);
        polygons.put(bankPolygon, Board.bank);
        polygons.put(churchPolygon, Board.church);
        polygons.put(hotelPolygon, Board.hotel);

        //Hide unused players
        switch (numOfPlayers) {
            case 1:
                playerCyan.setVisible(false);
            case 2:
                playerGreen.setVisible(false);
            case 3:
                playerOrange.setVisible(false);
            case 4:
                playerPink.setVisible(false);
            case 5:
                playerRed.setVisible(false);
            case 6:
                playerViolet.setVisible(false);
            case 7:
                playerYellow.setVisible(false);
        }
        for (Player player: playerData) {
            updateImage(player);
            player.setCurrentLocation(Board.trailers);
        }
    }

    void resetPlayerLocation() {
        playerBlue.setLayoutX(1000);
        playerBlue.setLayoutY(310);
        playerCyan.setLayoutX(1000);
        playerCyan.setLayoutY(335);
        playerGreen.setLayoutX(1000);
        playerGreen.setLayoutY(360);
        playerOrange.setLayoutX(1000);
        playerOrange.setLayoutY(385);
        playerPink.setLayoutX(1060);
        playerPink.setLayoutY(310);
        playerRed.setLayoutX(1060);
        playerRed.setLayoutY(335);
        playerViolet.setLayoutX(1060);
        playerViolet.setLayoutY(360);
        playerYellow.setLayoutX(1060);
        playerYellow.setLayoutY(385);
    }

    @FXML private void onMousePressed(MouseEvent event) {
        ImageView currentPlayer = players.get(Deadwood.getCurrentPlayer());
        double deltaX = event.getSceneX() - currentPlayer.getLayoutX();
        double deltaY = event.getSceneY() - currentPlayer.getLayoutY();
        if (deltaX >= 0 && deltaX <= currentPlayer.getFitWidth() &&
                deltaY >= 0 && deltaY <= currentPlayer.getFitWidth()) {
            canDrag = true;
            originalX = currentPlayer.getLayoutX();
            originalY = currentPlayer.getLayoutY();
            dragX = currentPlayer.getLayoutX() - event.getSceneX();
            dragY = currentPlayer.getLayoutY() - event.getSceneY();
            currentPlayer.setCursor(Cursor.MOVE);
            if (!Deadwood.hasMoved) {
                originalPlace = Deadwood.getCurrentPlayer().getCurrentLocation();
            }
        }
    }

    @FXML private void onMouseReleased(MouseEvent event) {
        canDrag = false;
        Player currentPlayerData = Deadwood.getCurrentPlayer();
        ImageView currentPlayer = players.get(currentPlayerData);
        Place oldPlayerLocation = currentPlayerData.getCurrentLocation();
        boolean foundLocation = false, foundRole = false;
        List<Role> closeRoles = Deadwood.getCloseRoles();

        //check that player has moved to a legal place
        for (Map.Entry<Polygon, Place> entry : polygons.entrySet()) {
            Polygon k = entry.getKey();
            Place v = entry.getValue();
            Point2D mouseLocation = k.sceneToLocal(event.getSceneX(), event.getSceneY());
            if (k.contains(mouseLocation)) {
                if (originalPlace.getNeighbors().contains(v) ||
                        originalPlace == v) {
                    currentPlayerData.setCurrentLocation(v);
                    foundLocation = true;
                    takeRoleButton.setDisable(true);
                    Deadwood.hasMoved = true;
                    break;
                }
            }
        }
        if (foundLocation) {
            //check all roles in the current location and neighboring locations
            for (Role role: closeRoles) {
                double deltaX = event.getSceneX() - role.getSceneX();
                double deltaY = event.getSceneY() - role.getSceneY();
                if (deltaX >= 0 && deltaX <= role.getWidth() &&
                        deltaY >= 0 && deltaY <= role.getHeight()) {
                    foundRole = true;
                    if (role.getRequiredRank() <= currentPlayerData.getRank()) {
                        //player has moved to a legal role
                        currentPlayer.setLayoutX(role.getSceneX());
                        currentPlayer.setLayoutY(role.getSceneY());
                        takeRoleButton.setDisable(false);
                        currentRole = role;
                        if (currentPlayerData.getCurrentLocation() instanceof Set) {
                            viewCard(((Set) currentPlayerData.getCurrentLocation()).getCard());
                        }
                    } else {
                        //player has not moved to a legal Role
                        currentPlayer.setLayoutX(originalX);
                        currentPlayer.setLayoutY(originalY);
                        Deadwood.hasMoved = false;
                        currentPlayerData.setCurrentLocation(oldPlayerLocation);
                    }
                    break;
                }
            }
            if (!foundRole) {
                if (currentPlayerData.getCurrentLocation() instanceof Set) {
                    viewCard(((Set) currentPlayerData.getCurrentLocation()).getCard());
                }
                if (currentPlayerData.getRole() != null) {
                    currentPlayerData.getRole().setPlayer(null);
                }
                currentPlayerData.setRole(null);
            }
        } else {
            //player has not moved to a legal Place
            currentPlayer.setLayoutX(originalX);
            currentPlayer.setLayoutY(originalY);
            currentPlayerData.setCurrentLocation(oldPlayerLocation);
        }
        //Setup upgrade menu
        if (currentPlayerData.getCurrentLocation().getName().equals("Casting Office")) {
            switch (currentPlayerData.getRank()) {
                case 1:
                    upgradeLevel.getItems().add(2);
                case 2:
                    upgradeLevel.getItems().add(3);
                case 3:
                    upgradeLevel.getItems().add(4);
                case 4:
                    upgradeLevel.getItems().add(5);
                case 5:
                    upgradeLevel.getItems().add(6);
            }
            upgradeLevel.setVisible(true);
            upgradeWithMoneyButton.setDisable(false);
            upgradeWithCreditsButton.setDisable(false);
        }
        currentPlayer.setCursor(Cursor.HAND);
    }

    @FXML private void onMouseDragged(MouseEvent event) {
        if (canDrag) {
            ImageView currentPlayer = players.get(Deadwood.getCurrentPlayer());
            currentPlayer.setLayoutX(event.getSceneX() + dragX);
            currentPlayer.setLayoutY(event.getSceneY() + dragY);
        }
    }

    @FXML private void onMouseEntered(MouseEvent event) {
        ImageView currentPlayer = players.get(Deadwood.getCurrentPlayer());
        double deltaX = event.getSceneX() - currentPlayer.getLayoutX();
        double deltaY = event.getSceneY() - currentPlayer.getLayoutY();
        if (deltaX >= 0 && deltaX <= currentPlayer.getFitWidth() &&
                deltaY >= 0 && deltaY <= currentPlayer.getFitWidth()) {
            currentPlayer.setCursor(Cursor.HAND);
        }
    }

    void updateImage(Player player) {
        ImageView playerImage = players.get(player);
        playerImage.setImage(new Image(player.getImage()));
    }

    void setPlayer(Player player) {
        setCurrentPlayerImage(player);
        setPlayerRank(player.getRank());
        setPlayerMoney(player.getMoney());
        setPlayerCredits(player.getCredits());
        setPracticeChips(player.getPracticeChips());
        if (player.getRole() != null) {
            takeRoleButton.setDisable(true);
            rehearseButton.setDisable(false);
            actButton.setDisable(false);
        } else {
            rehearseButton.setDisable(true);
            actButton.setDisable(true);

        }
    }

    void setCurrentPlayerImage(Player player) {
        ImageView currentPlayer = players.get(player);
        currentPlayerImage.setImage(currentPlayer.getImage());
    }

    void setPlayerRank(int rank) {
        this.rank.setText("" + rank);
    }

    void setPlayerMoney(int money) {
        this.money.setText("$" + money);
    }

    void setPlayerCredits(int credits) {
        this.credits.setText("" + credits);
    }

    void setPracticeChips(int practiceChips) {
        this.practiceChips.setText("" + practiceChips);
    }

    void setCards() {
        List<Set> sets = Board.getSets();
        for (Set set: sets) {
            switch (set.getName()) {
                case "Train Station":
                    cards.put(set.getCard(), cardTrainStation);
                    break;
                case "Jail":
                    cards.put(set.getCard(), cardJail);
                    break;
                case "Main Street":
                    cards.put(set.getCard(), cardMainStreet);
                    break;
                case "General Store":
                    cards.put(set.getCard(), cardGeneralStore);
                    break;
                case "Saloon":
                    cards.put(set.getCard(), cardSaloon);
                    break;
                case "Ranch":
                    cards.put(set.getCard(), cardRanch);
                    break;
                case "Bank":
                    cards.put(set.getCard(), cardBank);
                    break;
                case "Secret Hideout":
                    cards.put(set.getCard(), cardSecretHideout);
                    break;
                case "Church":
                    cards.put(set.getCard(), cardChurch);
                    break;
                case "Hotel":
                    cards.put(set.getCard(), cardHotel);
                    break;
            }
        }
        cards.forEach((k, v) -> {
            v.setImage(new Image(k.hideImage()));
        });
    }

    void viewCard(Card card) {
        cards.get(card).setImage(new Image(card.showImage()));
    }

    void hideCard(Card card) {
        cards.get(card).setImage(new Image(card.hideImage()));
    }

    void deleteCard(Card card) {
        cards.get(card).setImage(null);
    }

    void resetTakes() {
        for (ImageView[] placeTakes: takes)
            for (ImageView take: placeTakes)
                take.setVisible(false);
    }

    void updateTakes(Set set) {
        ImageView[] currentTakes = null;
        switch (set.getName()) {
            case "Train Station":
                currentTakes = takes[0];
                break;
            case "Jail":
                currentTakes = takes[1];
                break;
            case "Main Street":
                currentTakes = takes[2];
                break;
            case "General Store":
                currentTakes = takes[3];
                break;
            case "Saloon":
                currentTakes = takes[4];
                break;
            case "Ranch":
                currentTakes = takes[5];
                break;
            case "Bank":
                currentTakes = takes[6];
                break;
            case "Secret Hideout":
                currentTakes = takes[7];
                break;
            case "Church":
                currentTakes = takes[8];
                break;
            case "Hotel":
                currentTakes = takes[9];
        }
        if (currentTakes != null) {
            for (int i = 0; i < set.getShotsCompleted(); i++) {
                currentTakes[i].setVisible(true);
            }
        }
    }

    @FXML private void takeRole() {
        Deadwood.takeRole(currentRole);
        takeRoleButton.setDisable(true);
        rehearseButton.setDisable(false);
        actButton.setDisable(false);
    }

    @FXML private void rehearse() {
        boolean success = Deadwood.rehearse();
        if (success) {
            rehearseButton.setDisable(true);
            actButton.setDisable(true);
        }
    }

    @FXML private void act() {
        int shotsLeft = Deadwood.act();
        rehearseButton.setDisable(true);
        actButton.setDisable(true);
        if (shotsLeft <= 0) {
            Deadwood.wrap();
        }
    }

    @FXML private void upgradeWithMoney() {
        int level = (int) upgradeLevel.getValue();
        Deadwood.upgradeWithMoney(level);
    }

    @FXML private void upgradeWithCredits() {
        int level = (int) upgradeLevel.getValue();
        Deadwood.upgradeWithCredits(level);
    }

    @FXML private void endTurn() {
        takeRoleButton.setDisable(true);
        rehearseButton.setDisable(true);
        actButton.setDisable(true);
        upgradeWithMoneyButton.setDisable(true);
        upgradeWithCreditsButton.setDisable(true);
        upgradeLevel.setVisible(false);
        Deadwood.endTurn();
    }
}
