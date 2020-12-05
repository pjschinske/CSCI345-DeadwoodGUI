package deadwood.board;

import deadwood.xml.ParseXML;
import javafx.scene.shape.Rectangle;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Board {

    public static Place trailers = new Place("Trailers");
    public static Set mainStreet = new Set("Main Street", 3, 969, 28);
    public static Set saloon = new Set("Saloon", 2, 632, 280);
    public static Place castingOffice = new Place("Casting Office");
    public static Set ranch = new Set("Ranch", 2, 252, 478);
    public static Set secretHideout = new Set("Secret Hideout", 3, 27, 732);
    public static Set bank = new Set("Bank", 1, 623, 475);
    public static Set church = new Set("Church", 2, 623, 734);
    public static Set hotel = new Set("Hotel", 3, 969, 740);
    public static Set trainStation = new Set("Train Station", 3, 21, 69);
    public static Set jail = new Set("Jail", 1, 281, 27);
    public static Set generalStore = new Set("General Store", 2, 370, 282);

    public static List<Set> sets = new ArrayList<>();

    private static List<Card> cards;
    static {
        try {
            cards = ParseXML.readCardData(ParseXML.getDocFromFile("cards.xml"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static int numOfScenes;

    public static void makeBoard() {
        sets.add(mainStreet);
        sets.add(saloon);
        sets.add(ranch);
        sets.add(secretHideout);
        sets.add(bank);
        sets.add(church);
        sets.add(hotel);
        sets.add(trainStation);
        sets.add(jail);
        sets.add(generalStore);

        //create intra-board connections
        connect(trailers, mainStreet);
        connect(trailers, saloon);
        connect(mainStreet, saloon);

        connect(castingOffice, ranch);
        connect(castingOffice, secretHideout);
        connect(secretHideout, ranch);

        connect(bank, hotel);
        connect(bank, church);
        connect(church, hotel);

        connect(jail, generalStore);
        connect(jail, trainStation);
        connect(trainStation, generalStore);

        //create inter-board connections
        connect(saloon, bank);
        connect(trailers, hotel);

        connect(bank, ranch);
        connect(church, secretHideout);

        connect(ranch, generalStore);
        connect(castingOffice, trainStation);

        connect(generalStore, saloon);
        connect(jail, mainStreet);

        try {
            ParseXML.readBoardData(ParseXML.getDocFromFile("board.xml"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method for makeBoard() to ease the creation of links between places
     */
    private static void connect(Place p1, Place p2) {
        p1.addNeighbor(p2);
        p2.addNeighbor(p1);
    }

//    public static Card[] getCards() {
//        return cards;
//    }

    public static List<Set> getSets() {
        return sets;
    }

    /**
     * Randomly allocates scenes among the sets.
     */
    public static void dealCards() {
        List<Card> cardsTemp = new ArrayList<>(Board.cards.size());
        cardsTemp.addAll(cards);
        System.out.println("# of cards: " + cardsTemp.size());
        Random rand = new Random();
        for (Set set: sets) {
            int cardIndex = rand.nextInt(cardsTemp.size());
            Card card = cardsTemp.get(cardIndex);
            System.out.println(card.getName());
            set.setCard(cardsTemp.get(cardIndex));
            cardsTemp.remove(cardsTemp.get(cardIndex));
        }
        numOfScenes = sets.size();
    }

    public static int getNumOfCards() {
        return numOfScenes;
    }

    public static int finishScene() {
       return --numOfScenes;
    }

    public static void reset() {
        for (Set set: sets) {
            set.reset();
        }
    }
}
