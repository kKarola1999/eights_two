import java.util.ArrayList;
import java.util.Collections;

public class Card {

    private String type;
    private String value;
    private String symbol;

    /**
     * The four different types of cards in a standard deck
     */
    public static final String[] CARD_TYPES = {
            "heart", "spade", "diamond", "club"
    };

    /**
     * The different face values for each card
     */
    public static final String[] CARD_RANKS = {
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"
    };

    /**
     * Class constructor
     *
     * @param type Heart, spade, diamond, or club
     * @param value 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, or Ace
     */
    private Card(String type, String value) {
        this.type = type;
        this.value = value;
        this.symbol = pairTypeWithUnicode(type);
    }

    public String toString() {
        return Character.toUpperCase(this.value.charAt(0)) + this.value.substring(1) + " " + this.symbol;
    }

    /**
     * Returns the suit symbol given a string
     *
     * @param type Heart, spade, diamond, or club
     * @return The unicode symbol for parameter "type"
     */
    public static String pairTypeWithUnicode(String type) {

        String unicode = "";

        switch (type) {
            case "heart":
                unicode = "♥";
                break;
            case "spade":
                unicode = "♠";
                break;
            case "diamond":
                unicode = "♦";
                break;
            case "club":
                unicode = "♣";
        }

        return unicode;
    }

    /**
     * Create and return a shuffled ArrayList of Cards
     *
     * @return ArrayList of 52 Cards
     */
    public static ArrayList<Card> createDeck() {

        int deckSize = CARD_RANKS.length * CARD_TYPES.length * Rules.NUMBER_OF_DECKS;
        ArrayList<Card> deck = new ArrayList<>(deckSize);

        for (int d = 0; d < deckSize; d++) {
            deck.add(new Card(CARD_TYPES[d / 13], CARD_RANKS[d % 13]));
        }

        // Shuffle and return
        Collections.shuffle(deck);

        return deck;
    }

    /**
     * Returns card rank for comparisons
     *
     * @return Card's rank
     */
    public String getValue() { return Character.toUpperCase(this.value.charAt(0)) + this.value.substring(1); }

    /**
     * Returns card suit for comparisons
     *
     * @return Card's suit
     */
    public String getType() { return this.type; }
}
