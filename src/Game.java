import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;

public class Game {

    /**
     * Enum type to represent the game status
     */
    public enum Status {CONTINUE, WON, LOST, TIE}

    public static void main(String[] args) {

        // Create two players; one computer and one human
        Player you = new Player("player");
        Player player1 = new Player("player1");

        // Create a deck of 52 unique, shuffled cards
        ArrayList<Card> deck = Card.createDeck();

        // Initialize the game status
        Status gameStatus = Status.CONTINUE;
        String reasonGameOver = "";
        String newSuit = ""; // When an "8" is played

        // Create Scanner to obtain inputs
        Scanner input = new Scanner(System.in);

        // Prompt user to begin
        System.out.println("Press the Enter key to play a crazy game of Crazy Eights ...");
        try {
            System.in.read();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Randomly select who goes first- Player or Computer
        Player firstCardPlayer = Rules.randomPlayer(you, player1);
        Player secondCardPlayer = (firstCardPlayer == you) ? player1 : you;

        // Deal cards
        for (int c = 0; c < Rules.CARDS_TO_DEAL; c++) {
            firstCardPlayer.takeCardFromTopOfDeck(deck);
            secondCardPlayer.takeCardFromTopOfDeck(deck);
        }

        System.out.println(you + " and " + player1 + " were dealt " + Rules.CARDS_TO_DEAL + " cards each.");

        // Initialize the up card
        String topCardFaceValue;
        Card topCard;

        do {
            topCard = deck.get(0);
            deck.remove(0);

            topCardFaceValue = topCard.getValue();

            // Up card cannot be an "8". Throw it back in the deck somewhere.
            if (topCardFaceValue.equals("8")) {

                // Randomly place somewhere back in the deck
                Random rand = new Random();
                deck.add(rand.nextInt(deck.size()), topCard);
            }
        }
        while (topCardFaceValue.equals("8"));

        // Randomly select who goes first- Player or Computer
        Player nextUp = secondCardPlayer;

        System.out.println("Up card to start: " + topCard);
        System.out.println(nextUp + " goes first!\n");

        while (gameStatus == Status.CONTINUE) {

            if (nextUp == you) {
                boolean needALineBreak = false;

                // Reset every turn; reserved for scenarios where there are no cards to draw and no cards to play
                //you.setSkipStatus(false);

                // If you cannot play a card, you have to take one from the top
                while (!you.canPlayCardThisTopCard(newSuit, topCard)) {
                    needALineBreak = true;

                    if (deck.size() != 0) {
                        you.takeCardFromTopOfDeck(deck);
                        System.out.println("No cards to play. Player draws a: " + you.getHand().get(you.numberOfCardsInHand() - 1));
                    }
                    else {
                        you.setSkipStatus(true);
                        System.out.println("No cards to play. No cards to draw from deck. Skipping " + you + "'s turn.");
                        break;
                    }
                }

                // For formatting in the terminal
                if (needALineBreak) System.out.println();

                // Update game status for Player
                if (!you.skipped()) {
                    System.out.println("Cards left ..... " + deck.size());
                    System.out.println((newSuit.length() == 0)
                            ? "Top card ....... " + topCard
                            : "New suit ....... " + Card.pairTypeWithUnicode(newSuit));
                    System.out.println(you.getHandAndChoices());
                    System.out.print("\nYour's decision: ");
                    nextUp=player1;
                    newSuit="";
                }

                // Make sure you can lay down the card
                boolean validPlay = false;
                while (!validPlay ) {
                    try {
                        // Sentinel-controlled iteration
                        Card cardChoiceObject = you.playCard(input.nextInt());

                        if (Rules.checkForValidPlay(newSuit, topCard, cardChoiceObject)) {
                            topCard = cardChoiceObject;
                            you.getHand().remove(cardChoiceObject);

                            // Playing a wild card allows the player to select a new suit
                            if (topCard.getValue().equals("8") && you.numberOfCardsInHand() != 0) {
                                System.out.println("\n" + you.getSuitsAndChoices());
                                System.out.print("\nYour's decision: ");

                                boolean validSuit = false;
                                do {
                                    try {
                                        newSuit = Card.CARD_TYPES[input.nextInt() -1];
                                        validSuit = true;
                                        validPlay = true;
                                        nextUp=player1;
                                    }
                                    catch (IndexOutOfBoundsException e) {
                                        handleException("Invalid selection. Please try again.");
                                    }
                                    catch (InputMismatchException e) {
                                        input.next();
                                        handleException("Invalid selection. Please try again.");
                                    }
                                }
                                while (!validSuit);
                            }
                            else {
                                validPlay = true;
                            }
                        }
                        else {
                            handleException("You cannot play that card. Please try again.");
                        }
                    }
                    catch (IndexOutOfBoundsException e) {
                        handleException("Invalid selection. Please try again.");
                    }
                    catch (InputMismatchException e) {
                        input.next();
                        handleException("Invalid selection. Please try again.");
                    }

                    System.out.println();
                }
            }
            else {
                boolean needALineBreak = false; // For formatting in the terminal


                // Reset every turn; reserved for scenarios where there are no cards to draw and no cards to play
                //you.setSkipStatus(false);

                // If you cannot play a card, you have to take one from the top
                while (!player1.canPlayCardThisTopCard(newSuit, topCard)) {
                    needALineBreak = true;

                    if (deck.size() != 0) {
                        player1.takeCardFromTopOfDeck(deck);
                        System.out.println("No cards to play. Player draws a: " + player1.getHand().get(player1.numberOfCardsInHand() - 1));
                    }
                    else {
                        player1.setSkipStatus(true);
                        System.out.println("No cards to play. No cards to draw from deck. Skipping " + player1 + "'s turn.");
                        break;
                    }
                }

                // For formatting in the terminal
                if (needALineBreak) System.out.println();

                // Update game status for Player
                if (!player1.skipped()) {
                System.out.println("Cards left ..... " + deck.size());
                System.out.println((newSuit.length() == 0)
                        ? "Top card ....... " + topCard
                        : "New suit ....... " + Card.pairTypeWithUnicode(newSuit));
                System.out.println(player1.getHandAndChoices());
                System.out.print("\nPlayer's 1 decision: ");
                    nextUp=you;
                    newSuit="";
                }

                // Make sure you can lay down the card
                boolean validPlay = false;
                while (!validPlay ) {
                    try {
                        // Sentinel-controlled iteration
                        Card cardChoiceObject = player1.playCard(input.nextInt());

                        if (Rules.checkForValidPlay(newSuit, topCard, cardChoiceObject)) {
                            topCard = cardChoiceObject;
                            player1.getHand().remove(cardChoiceObject);

                            // Playing a wild card allows the player to select a new suit
                            if (topCard.getValue().equals("8") && player1.numberOfCardsInHand() != 0) {
                                System.out.println("\n" + player1.getSuitsAndChoices());
                                System.out.print("\nPlayer's 1 decision: ");

                                boolean validSuit = false;

                                do {
                                    try {
                                        newSuit = Card.CARD_TYPES[input.nextInt() -1];
                                        validSuit = true;
                                        validPlay = true;
                                        nextUp=you;
                                    }
                                    catch (IndexOutOfBoundsException e) {
                                        handleException("Invalid selection. Please try again.");
                                    }
                                    catch (InputMismatchException e) {
                                        input.next();
                                        handleException("Invalid selection. Please try again.");
                                    }
                                }
                                while (!validSuit);
                            }
                            else {
                                validPlay = true;
                            }
                        }
                        else {
                            handleException("You cannot play that card. Please try again.");
                        }
                    }
                    catch (IndexOutOfBoundsException e) {
                        handleException("Invalid selection. Please try again.");
                    }
                    catch (InputMismatchException e) {
                        input.next();
                        handleException("Invalid selection. Please try again.");
                    }

                    System.out.println();
                }
            }
                }


            // See if there is a winner
            if (you.numberOfCardsInHand() == 0) {
                gameStatus = Status.WON;
                reasonGameOver = "Player wins- they were able to get rid of their cards first!";
            }
            else if (player1.numberOfCardsInHand() == 0) {
                gameStatus = Status.LOST;
                reasonGameOver = "Computer wins- they were able to get rid of their cards first!";
            }
            else if (deck.size() == 0 ) {
                gameStatus = Rules.determineWinner(you, player1);

                if (gameStatus.equals(Status.WON)) {
                    reasonGameOver = "Player wins!- their card total is less than the Computer's card total.";
                }
                else if (gameStatus.equals(Status.LOST)) {
                    reasonGameOver = "Computer wins!- their card total is less than the Player's card total.";
                }
                else {
                    gameStatus = Status.TIE;
                    reasonGameOver = "Tie!- Player's card total and Computer's card total are the same.";
                }
            }
            else {

                // Reset this variable once a card has been played after a crazy eight
                if (!topCard.getValue().equals("8") && newSuit.length() > 0) {
                    newSuit = "";
                }

                // Swap turns
                nextUp = (nextUp == firstCardPlayer) ? secondCardPlayer : firstCardPlayer;
            }
        }

        // Explain what happened. The game is over.
        //System.out.println(reasonGameOver);


    /**
     * Helper method to reduce code duplication
     *
     * @param message The message printed to the console
     */
    private static void handleException(String message) {
        System.out.println("\n" + message);
        System.out.print("\nPlayer decision: ");
    }
}