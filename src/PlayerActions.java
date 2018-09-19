import java.util.ArrayList;

public interface PlayerActions {

    void takeCardFromTopOfDeck(ArrayList deck);

    Card playCard(int cardChoice);

    Card computerAi(String newSuit, Card topCard);

    String computerSelectNewSuit();
}
