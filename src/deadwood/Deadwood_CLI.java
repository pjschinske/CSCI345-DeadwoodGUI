package deadwood;

import deadwood.board.*;

import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.Scanner;

public class Deadwood_CLI {

//    public static Bank bank = new Bank(Integer.MAX_VALUE);
//
//    public static void main(String[] args) throws ParserConfigurationException {
//        Scanner scanner = new Scanner(System.in);
//
//        //Create the board
//        Board.makeBoard();
//
//        //initialize players
//        System.out.print("How many people will be playing? ");
//        int numOfPlayers = scanner.nextInt();
//        Player[] players = createPlayers(numOfPlayers);
//        if (numOfPlayers == 1)
//            System.out.println("Created 1 player:");
//        else
//            System.out.printf("Created %d players:\n", numOfPlayers);
//        for(Player player: players) {
//            System.out.printf("\t%s\n", player.getName());
//        }
//
//        int numOfDays;
//        if (numOfPlayers <= 3) {
//            numOfDays = 3;
//        } else {
//            numOfDays = 4;
//        }
//
//        for (int i = 0; i < numOfDays; i++) {
//            Board.dealCards();
//            for (Player player: players) {
//                player.setCurrentLocation(Board.trailers);
//            }
//            //loop through all players
//            for (int j = 0; Board.getNumOfCards() > 1; j++) {
//                Player currentPlayer = players[j % numOfPlayers];
//                boolean hasMoved = false;
//                boolean done = false;
//                while (!done) {
//                    System.out.print("> ");
//                    String input = scanner.next();
//                    Place currentLocation = currentPlayer.getCurrentLocation();
//                    switch (input) {
//                        case "whoami":
//                            if (currentPlayer.getCredits() == 1) {
//                                System.out.printf("The active player is %s. They have $%d, %d credit and a rank of %d.", currentPlayer.getName(), currentPlayer.getMoney(), currentPlayer.getCredits(), currentPlayer.getRank());
//                            } else {
//                                System.out.printf("The active player is %s. They have $%d, %d credits and a rank of %d.", currentPlayer.getName(), currentPlayer.getMoney(), currentPlayer.getCredits(), currentPlayer.getRank());
//                            }
//                            if (currentPlayer.getRole() != null) {
//                                System.out.printf(" They are working %s, \"%s\"", currentPlayer.getRole().getName(), currentPlayer.getRole().getLine());
//                            }
//                            System.out.println();
//                            break;
//                        case "end":
//                            done = true;
//                            break;
//                        case "where":
//                            for (int k = 0; k < numOfPlayers; k++) {
//                                Player player = players[k];
//                                System.out.printf("%s is at %s", player.getName(), player.getCurrentLocation().getName());
//                                if (player.getCurrentLocation() instanceof Set) {
//                                    if (((Set) player.getCurrentLocation()).getRole(player) != null) {
//                                        String sceneName = ((Set) player.getCurrentLocation()).getCard().getName();
//                                        int sceneNumber = ((Set) player.getCurrentLocation()).getCard().getNumber();
//                                        System.out.printf(" shooting %s scene %d", sceneName, sceneNumber);
//                                    }
//                                }
//                                System.out.println();
//                            }
//                            break;
//                        case "move":
//                            List<Place> neighbors = currentLocation.getNeighbors();
//                            if (!hasMoved) {
//                                if (scanner.hasNext()) {
//                                    String destination = scanner.nextLine().substring(1);
//                                    for (Place neighbor : neighbors) {
//                                        if (neighbor.getName().equals(destination)) {
//                                            currentPlayer.setCurrentLocation(neighbor);
//                                            hasMoved = true;
//                                            break;
//                                        }
//                                    }
//                                    if (!hasMoved) {
//                                        System.out.println("You cannot go there.");
//                                    }
//                                }
//                            } else {
//                                System.out.println("You have already moved this turn.");
//                            }
//                            break;
//                        case "scene":
//                            if (currentLocation instanceof Set) {
//                                Card currentCard = ((Set) currentLocation).getCard();
//                                System.out.printf("%s scene %d.\n", currentCard.getName(), currentCard.getNumber());
//                                List<Role> onCardRoles = currentCard.getRoles();
//                                List<Role> offCardRoles = ((Set) currentLocation).getRoles();
//                                System.out.println("On-card roles:");
//                                for (Role role: onCardRoles) {
//                                    System.out.printf("\t%s: \"%s\" (rank %d)\n", role.getName(), role.getLine(), role.getRequiredRank());
//                                }
//                                System.out.println("Off-card roles:");
//                                for (Role role: offCardRoles) {
//                                    System.out.printf("\t%s: \"%s\" (rank %d)\n", role.getName(), role.getLine(), role.getRequiredRank());
//                                }
//                            } else {
//                                System.out.println("There are no jobs here.");
//                            }
//                            break;
//                        case "work":
//                            if (scanner.hasNext()) {
//                                String role = scanner.nextLine().substring(1);
//                                if (currentLocation instanceof Set) {
//                                    Boolean success = false;
//                                    for (Role offCardRole: ((Set) currentLocation).getRoles()) {
//                                        if (offCardRole.getName().equals(role)) {
//                                            if (offCardRole.getRequiredRank() <= currentPlayer.getRank()) {
//                                                offCardRole.setPlayer(currentPlayer);
//                                                currentPlayer.setRole(offCardRole);
//                                                success = true;
//                                            }
//                                        }
//                                    }
//                                    if (!success) {
//                                        for (Role onCardRole: ((Set) currentLocation).getCard().getRoles()) {
//                                            if (onCardRole.getName().equals(role)) {
//                                                if (onCardRole.getRequiredRank() <= currentPlayer.getRank()) {
//                                                    onCardRole.setPlayer(currentPlayer);
//                                                    currentPlayer.setRole(onCardRole);
//                                                    success = true;
//                                                }
//                                            }
//                                        }
//                                    }
//                                    if (!success) {
//                                        System.out.println("Either that role doesn't exist or you don't have a high enough rank.");
//                                    }
//                                } else {
//                                    System.out.println("You cannot work here.");
//                                }
//                            }
//                            break;
//                        case "act":
//                            if (currentPlayer.getCurrentLocation() instanceof Set) {
//                                Set currentSet = (Set) currentPlayer.getCurrentLocation();
//                                if (currentPlayer.getRole() != null) {
//                                    currentSet.act(currentPlayer);
//                                } else {
//                                    System.out.println("You are not in a role right now.");
//                                }
//                            } else {
//                                System.out.println("You cannot act here.");
//                            }
//                            break;
//                        case "rehearse":
//                            if (currentLocation instanceof Set) {
//                                ((Set) currentLocation).rehearse(currentPlayer);
//                            }
//                            break;
//                        case "upgrade":
//                            if (currentLocation == Board.castingOffice) {
//                                System.out.printf("Your current level is %d.\n", currentPlayer.getRank());
//                                System.out.println("Level 2 will cost 4 dollars or 5 credits.");
//                                System.out.println("Level 3 will cost 10 dollars or 10 credits.");
//                                System.out.println("Level 4 will cost 18 dollars or 15 credits.");
//                                System.out.println("Level 5 will cost 28 dollars or 20 credits.");
//                                System.out.println("Level 6 will cost 40 dollars or 25 credits.");
//                                System.out.print("Do you want to pay with money or with credits? ");
//                                String moneyOrCredits = scanner.next();
//                                int[] rankToMoney = {0, 0, 4, 10, 18, 28, 40};
//                                int[] rankToCredits = {0, 0, 5, 10, 15, 20, 25};
//                                System.out.print("What level do you wish to pay for? ");
//                                int rank = scanner.nextInt();
//                                if (moneyOrCredits.toLowerCase().equals("money")) {
//                                    if (currentPlayer.getRank() < rank && rankToMoney[rank] <= currentPlayer.getMoney()) {
//                                        currentPlayer.setRank(rank);
//                                        currentPlayer.payBank(Deadwood_CLI.bank, rankToMoney[rank]);
//                                        System.out.printf("You paid $%d to become level %d.\n", rankToMoney[rank], currentPlayer.getRank());
//                                    } else {
//                                        System.out.println("You can't get that level.");
//                                    }
//                                } else {
//                                    if (currentPlayer.getRank() < rank && rankToCredits[rank] == currentPlayer.getCredits()) {
//                                        currentPlayer.setRank(rank);
//                                        currentPlayer.addCredits(-1 * rankToCredits[rank]);
//                                        System.out.printf("You paid %d credits to become level %d.\n", rankToCredits[rank], currentPlayer.getRank());
//                                    } else {
//                                        System.out.println("You can't get that level.");
//                                    }
//                                }
//                            } else {
//                                System.out.println("You need to be in the casting office to upgrade your level");
//                            }
//                    }
//                }
//            }
//
//            Board.reset();
//        }
//        System.out.println("That's the end of the game! And now for the scores:");
//        int winnerPoints = 0;
//        Player winner = null;
//        for (Player player: players) {
//            int points = player.getMoney() + player.getCredits() + 5 * player.getRank();
//            System.out.printf("%s got %d points.\n", player.getName(), points);
//            if (points > winnerPoints) {
//                winnerPoints = points;
//                winner = player;
//            }
//        }
//        System.out.printf("%s is the winner!", winner.getName());
//    }
//
//    /**
//     * Create and initialize array of players
//     * @param numOfPlayers number of players
//     * @return array of players
//     */
//    private static Player[] createPlayers(int numOfPlayers) {
//        Player[] players = new Player[numOfPlayers];
//        switch (numOfPlayers) {
//            case 8:
//                players[7] = new Player("Yellow", 1);
//            case 7:
//                players[6] = new Player("Violet", 1);
//            case 6:
//                players[5] = new Player("Red", 1);
//            case 5:
//                players[4] = new Player("Pink", 1);
//            case 4:
//                players[3] = new Player("Orange", 1);
//            case 3:
//                players[2] = new Player("Green", 1);
//            case 2:
//                players[1] = new Player("Cyan", 1);
//            case 1:
//                players[0] = new Player("Blue", 1);
//        }
//
//        //Alterations for different group sizes
//        switch (numOfPlayers) {
//            case 5:
//                for (Player player: players) {
//                    player.addCredits(2);
//                }
//                break;
//            case 6:
//                for (Player player: players) {
//                    player.addCredits(4);
//                }
//                break;
//            case 7:
//            case 8:
//                for (Player player: players) {
//                    player.setRank(2);
//                }
//        }
//
//        return players;
//    }
}
