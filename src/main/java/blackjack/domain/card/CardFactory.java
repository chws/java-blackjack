package blackjack.domain.card;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardFactory {
    private final List<Card> cards;

    private CardFactory() {
        this.cards = Collections.unmodifiableList(generateAllCards());
    }

    private List<Card> generateAllCards() {
        List<Card> cards = new LinkedList<>();
        Arrays.stream(Suit.values()).forEach(suit -> Arrays.stream(Symbol.values())
                .map(symbol -> new Card(suit, symbol))
                .forEach(cards::add));
        return cards;
    }

    public static CardFactory getInstance() {
        return CardFactorySingletonHolder.instance;
    }

    public List<Card> issueNewDeck() {
        return new LinkedList<>(cards);
    }

    private static class CardFactorySingletonHolder {
        private static final CardFactory instance = new CardFactory();
    }
}