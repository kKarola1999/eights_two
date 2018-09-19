import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;

public class Rules {

    /**
     * In two-player Crazy Eights; each player gets seven cards
     */
    public static final int CARDS_TO_DEAL = 7;

    /**
     * 1 v 1 Crazy Eights uses one deck of cards.  More players require two decks.
     */
    public static final int NUMBER_OF_DECKS = 1;

    /**
     * Numeric values assigned to each card rank
     */
    private static final Map<String, Integer> CARD_VALUES = initMap();
    private static Map<String, Integer> initMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        map.put("5", 5);
        map.put("6", 6);
        map.put("7", 7);
        map.put("8", 50);
        map.put("9", 9);
        map.put("10", 10);
        map.put("Jack", 10);
        map.put("King", 10);
        map.put("Queen", 10);
        map.put("Ace", 1);

        return Collections.unmodifiableMap(map);
    }

    /**
     * Randomly select either the player or computer to go first
     *
     * @param player Player
     * @param computer Computer
     * @return Returns either the Player player object or Computer player object
     */
    public static Player randomPlayer(Player player, Player computer) {
        ArrayList<Player> players = new ArrayList<>(2);

        players.add(player);
        players.add(computer);

        Collections.shuffle(players);

        return players.get(0);
    }

    /**
     * Compare the card layed down to the up card; validate that it's possible
     *
     * @param newSuit If the computer played a crazy eight this parameter will be the new suit to be played
     * @param upCard The current up card
     * @param layedDown Card that just played
     * @return True of false; whether the move was legal or not
     */
    public static boolean checkForValidPlay(String newSuit, Card upCard, Card layedDown) {
        boolean validPlay = false;

        if (layedDown.getValue().equals("8")) {
            validPlay = true;
        }
        else if (newSuit.length() > 0) {
            if (layedDown.getType().equals(newSuit)) {
                validPlay = true;
            }
        }
        else {
            if (upCard.getType().equals(layedDown.getType())) {
                validPlay = true;
            }
            else if (upCard.getValue().equals(layedDown.getValue())) {
                validPlay = true;
            }
        }

        return validPlay;
    }

    /**
     * Compare the points of the player's remaining cards to the points of the computer's remaining cards to determine
     * who won
     *
     * @param player Player (and remaining cards on hand)
     * @param computer Computer Player (and remaining cards on hand)
     * @return The game status (how it ended)
     */
    public static Game.Status determineWinner(Player player, Player computer) {

        int playerPoints = 0;
        int computerPoints = 0;

        // Cycle through player's hand and calculate points
        for (Card card : player.getHand()) {
            playerPoints += CARD_VALUES.get(card.getValue());
        }

        // Cycle through computer's hand and calculate points
        for (Card card : computer.getHand()) {
            computerPoints += CARD_VALUES.get(card.getValue());
        }

        Game.Status status;
        if (playerPoints > computerPoints) {
            status = Game.Status.WON;
        }
        else if (playerPoints < computerPoints) {
            status = Game.Status.LOST;
        }
        else {
            status = Game.Status.TIE;
        }

        return status;
    }
}
