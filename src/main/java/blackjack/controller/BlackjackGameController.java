package blackjack.controller;

import blackjack.domain.card.CardFactory;
import blackjack.domain.card.Deck;
import blackjack.domain.game.BlackjackGame;
import blackjack.domain.game.TotalResult;
import blackjack.domain.user.*;
import blackjack.utils.InputHandler;
import blackjack.view.InputView;
import blackjack.view.OutputView;

import java.util.List;

public class BlackjackGameController {
    public static void run() {
        List<Player> players = enrollPlayers();
        askBettingMoney(players);
        BlackjackGame game = startBlackjackGame(players);
        game.distributeInitialCards();
        OutputView.printInitialCardDistribution(game);
        drawMoreCard(game);
        generateFinalResults(game);
        game.updateUserMoney();
    }

    private static void askBettingMoney(List<Player> players) {
        players.forEach(player -> {
            String playerName = player.getName();
            player.addMoney(InputView.inputBettingMoney(playerName));
        });
    }

    private static List<Player> enrollPlayers() {
        return PlayerFactory.generatePlayers(
                InputHandler.parseName(InputView.inputPlayerName())
        );
    }

    private static BlackjackGame startBlackjackGame(List<Player> players) {
        return new BlackjackGame(
                    players,
                    new Deck(CardFactory.getInstance().issueNewDeck()));
    }

    private static void drawMoreCard(BlackjackGame game) {
        if(!game.isDealerBlackJack()) {
            hitMoreCard(game.getPlayers(), game);
            dealerDrawsAdditionalCard(game);
        }
    }

    private static void hitMoreCard(List<Player> players, BlackjackGame game) {
        players.forEach(player -> askForHit(player, game));
    }

    private static void askForHit(Player player, BlackjackGame game) {
        while (isPlayerHitCard(player)) {
            game.hitCard(player);
            OutputView.printUserStatus(player);
        }
        showBusted(player);
    }

    private static boolean isPlayerHitCard(Player player) {
        return !player.isBusted() && InputView.askForHit(player.getName());
    }

    private static void showBusted(Player player) {
        if(player.isBusted()) {
            OutputView.printBusted(player.getName());
        }
    }

    private static void dealerDrawsAdditionalCard(BlackjackGame game) {
        while(game.isDealerUnderThreshold()) {
            game.dealerHitsAdditionalCard();
            OutputView.printDealerHitMoreCard();
        }
    }

    private static void generateFinalResults(BlackjackGame game) {
        OutputView.printFinalCardScore(game);
        TotalResult totalResult = game.calculateResultsPerPlayer();
        Money dealerProfit = totalResult.calculateDealerProfit();
        OutputView.printFinalResult(totalResult, dealerProfit);
    }
}
