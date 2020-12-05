package deadwood;

import deadwood.board.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Deadwood extends Application {

    private static Stage stage;
    private static GameController gameController;

    private static Player[] players;
    private static int currentPlayer;
    public static boolean hasMoved;

    private static int numOfDaysLeft;

    private static Random rand = new Random();

    private static Bank bank = new Bank(Integer.MAX_VALUE);

    private static final int[] RANK_COST_MONEY = {0, 0, 4, 10, 18, 28, 40};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/deadwood.fxml"));
        primaryStage.setTitle("Welcome | Deadwood");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;
    }

    /**
     * Start game up after the number of players has been selected.
     * @param numOfPlayers # of players
     */
    public static void startGame(int numOfPlayers) {
        if (numOfPlayers <= 3) {
            numOfDaysLeft = 3;
        } else {
            numOfDaysLeft = 4;
        }
        Board.makeBoard();

        createPlayers(numOfPlayers);
        currentPlayer = 0;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Deadwood.class.getResource("fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            gameController = fxmlLoader.getController();
            stage.setTitle("Deadwood");
            stage.setX(200);
            stage.setY(50);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameController.setPlayer(players[currentPlayer]);
        startDay();
    }

    /**
     * Create and initialize array of players
     * @param numOfPlayers number of players
     * @return array of players
     */
    private static void createPlayers(int numOfPlayers) {
        players = new Player[numOfPlayers];
        switch (numOfPlayers) {
            case 8:
                players[7] = new Player("Yellow", 1);
            case 7:
                players[6] = new Player("Violet", 1);
            case 6:
                players[5] = new Player("Red", 1);
            case 5:
                players[4] = new Player("Pink", 1);
            case 4:
                players[3] = new Player("Orange", 1);
            case 3:
                players[2] = new Player("Green", 1);
            case 2:
                players[1] = new Player("Cyan", 1);
            case 1:
                players[0] = new Player("Blue", 1);
        }

        //Alterations for different group sizes
        switch (numOfPlayers) {
            case 5:
                for (Player player: players) {
                    player.addCredits(2);
                }
                break;
            case 6:
                for (Player player: players) {
                    player.addCredits(4);
                }
                break;
            case 7:
            case 8:
                for (Player player: players) {
                    player.setRank(2);
                }
        }
    }

    /**
     * Called when a day is started
     */
    private static void startDay() {
        currentPlayer = 0;
        Board.reset();
        Board.dealCards();
        gameController.setCards();
        gameController.resetPlayerLocation();
        gameController.resetTakes();
        hasMoved = false;
    }

    public static Player[] getPlayers() {
        return players;
    }

    public static Player getCurrentPlayer() {
        return players[currentPlayer % players.length];
    }

    /**
     * Get the roles in the current player's location as well as in neighboring locations.
     * @return a list of accessible roles.
     */
    public static List<Role> getCloseRoles() {
        List<Role> roles = new ArrayList<>();
        Place currentLocation = getCurrentPlayer().getCurrentLocation();
        if (currentLocation instanceof Set) {
            roles.addAll(((Set) currentLocation).getRoles());
            if (((Set) currentLocation).hasCard()) {
                Card currentCard = ((Set) currentLocation).getCard();
                //card should always be shown if we're checking the location a player is at
                if (currentCard.isShown()) {
                    roles.addAll(currentCard.getRoles());
                } else {
                    System.out.println("Warning: current player is at a scene, but the card is not shown.\n" +
                            "\tThis isn't supposed to happen.");
                }
            }
        }

        for (Place neighbor: currentLocation.getNeighbors()) {
            if (neighbor instanceof Set) {
                roles.addAll(((Set) neighbor).getRoles());
                if (((Set) neighbor).hasCard()) {
                    Card currentCard = ((Set) neighbor).getCard();
                    if (currentCard.isShown()) {
                        roles.addAll(currentCard.getRoles());
                    }
                }
            }
        }
        return roles;
    }

    /**
     * Called when player takes a role.
     */
    public static void takeRole(Role role) {
        getCurrentPlayer().setRole(role);
        hasMoved = true;
    }

    /**
     * Called when player wishes to rehearse.
     * @return
     */
    public static boolean rehearse() {
        boolean success = getCurrentPlayer().rehearse();
        if (success) {
            gameController.setPlayer(getCurrentPlayer());
        }
        return success;
    }

    /**
     * Called when player wishes to act.
     * @return number of shots left after acting, or -1 if player is not on a set.
     */
    public static int act() {
        Place currentLocation = getCurrentPlayer().getCurrentLocation();
        if (currentLocation instanceof Set) {
            int budget = ((Set) currentLocation).getCard().getBudget();
            int roll = rand.nextInt(6) + 1;
            boolean onCard = ((Set) currentLocation).getCard().getRoles().contains(getCurrentPlayer().getRole());
            if (roll + getCurrentPlayer().getPracticeChips() >= budget) {
                ((Set) currentLocation).completeShot();
                gameController.updateTakes(((Set) currentLocation));
                if (onCard) {
                    getCurrentPlayer().addCredits(2);
                } else {
                    getCurrentPlayer().addCredits(1);
                    bank.payMoney(getCurrentPlayer(), 1);
                }
            } else {
                if (!onCard) {
                    bank.payMoney(getCurrentPlayer(), 1);
                }
            }
            gameController.setPlayer(getCurrentPlayer());
            return ((Set) currentLocation).getShotsLeft();
        } else {
            return -1;
        }
    }

    /**
     * Wrap the scene. Called when all scenes have been successfully acted.
     */
    public static void wrap() {
        Place currentLocation = getCurrentPlayer().getCurrentLocation();
        if (currentLocation instanceof Set) {
            List<Role> onCardRoles = ((Set) currentLocation).getCard().getRoles();
            List<Role> offCardRoles = ((Set) currentLocation).getRoles();
            for (Role onCardRole: onCardRoles) {
                if (onCardRole.hasPlayer()) {
                    int[] rolls = new int[((Set) currentLocation).getCard().getBudget()];
                    for (int i = 0; i < rolls.length; i++) {
                        rolls[i] = rand.nextInt(6) + 1;
                    }
                    Arrays.sort(rolls);
                    int[] payouts = new int[onCardRoles.size()];
                    int budget = ((Set) currentLocation).getCard().getBudget();
                    for (int i = budget - 1; i >= 0; i++) {
                        payouts[((budget - i) % onCardRoles.size()) - 1] = rolls[i];
                    }
                    for (int i = 0; i < onCardRoles.size(); i++) {
                        if (onCardRoles.get(i).hasPlayer()) {
                            Player player = onCardRoles.get(i).getPlayer();
                            player.clearPracticeChips();
                            bank.payMoney(player, payouts[i]);
                        }
                    }
                    for (int i = 0; i < offCardRoles.size(); i++) {
                        if (offCardRoles.get(i).hasPlayer()) {
                            Player player = offCardRoles.get(i).getPlayer();
                            player.clearPracticeChips();
                            bank.payMoney(player, offCardRoles.get(i).getRequiredRank());
                        }
                    }
                    break;
                }
            }
            for (Role onCardRole: onCardRoles) {
                if (onCardRole.hasPlayer()) {
                    onCardRole.getPlayer().setRole(null);
                    onCardRole.setPlayer(null);
                }
            }
            for (Role offCardRole: offCardRoles) {
                if (offCardRole.hasPlayer()) {
                    offCardRole.getPlayer().setRole(null);
                    offCardRole.setPlayer(null);
                }
            }
            gameController.deleteCard(((Set) currentLocation).getCard());
            ((Set) currentLocation).setCard(null);;
            Board.finishScene();
            if (Board.getNumOfCards() <= 1) {
                numOfDaysLeft--;
                if (numOfDaysLeft <= 0) {
                    //end of game

                } else {
                    //start another day
                    startDay();
                }
            }
        }
    }

    /**
     * Upgrade the current player and pay with money.
     * @param requestedRank
     * @return
     */
    public static boolean upgradeWithMoney(int requestedRank) {
        Player currentPlayer = getCurrentPlayer();
        int currentMoney = currentPlayer.getMoney();
        boolean success = false;
        success = currentMoney >= RANK_COST_MONEY[requestedRank];
        if (success) {
            currentPlayer.setRank(requestedRank);
            currentPlayer.payBank(bank, RANK_COST_MONEY[requestedRank]);
            gameController.setPlayer(currentPlayer);
        }
        return success;
    }

    /**
     * Upgrade the current player and pay with money.
     * @param requestedRank
     * @return
     */
    public static boolean upgradeWithCredits(int requestedRank) {
        Player currentPlayer = getCurrentPlayer();
        int currentCredits = currentPlayer.getCredits();
        boolean success = false;
        success = currentCredits >= (requestedRank - 1) * 5;
        if (success) {
            currentPlayer.setRank(requestedRank);
            currentPlayer.addCredits((requestedRank - 1) * -5);
            gameController.setPlayer(currentPlayer);
        }
        return success;
    }

    /**
     * End a plaayer's turn and start another turn.
     */
    public static void endTurn() {
        currentPlayer++;
        gameController.setPlayer(getCurrentPlayer());
        hasMoved = false;
    }
}
