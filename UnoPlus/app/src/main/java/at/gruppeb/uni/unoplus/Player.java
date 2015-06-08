

import java.util.ArrayList;
import java.util.List;


public class Player {

    static int 					static_id = 0;
    List<Card>		 			hand;
    int 						player_id = 0;
    private boolean				isMyTurn = false;
    Gamemanager					game;

    public 						Player(Gamemanager game) {
        this.player_id = Player.static_id;
        this.hand = new ArrayList<Card>();
        this.game = game;
        static_id++;
    }


    public List<Card>			getHand() {
        return this.hand;
    }

    public Card					layCard() {
        Card help = this.hand.get(0);
        this.hand.remove(0);
        return help;
    }

    public boolean				getIsMyTurn() {
        return this.isMyTurn;
    }

    public void					setIsMyTurn(boolean myturn) {
        this.isMyTurn = myturn;
    }

    public void					takeCard (Deck takedeck) {				// take card from takedeck
        this.hand.add(0, takedeck.deck.get(0));
        //send_data(this.hand.get(0).get_name())
        takedeck.deck.remove(0);
    }

    public void					playCard (Card card) {
        Player player = this.game.getCurrentPlayer();
        if (card.color == this.game.playdeck.deck.get(0).color) {					// if color matches
            this.game.playdeck.deck.add(0, card);
            removeCardFromPlayer(card, player);
            this.game.turn_ended = true;
        } else if (card.value== this.game.playdeck.deck.get(0).value) {			// if amount matches
            this.game.playdeck.deck.add(0, card);
            removeCardFromPlayer(card, player);
            this.game.turn_ended = true;
        } else if (this.game.playdeck.deck.get(0).color == Card.colors.BLACK) {
            this.game.playdeck.deck.add(0, card);
            removeCardFromPlayer(card, player);
            this.game.turn_ended = true;
        } else if (card.isActionCard()) {
            switch (card.value) {
                case TAKEFOUR: {
                    this.game.howManyCardsToTake += 4;
                    this.game.playdeck.deck.add(0, card);
                    removeCardFromPlayer(card, player);
                    this.game.turn_ended = true;
                    break;
                }
                case TAKETWO: {
                    this.game.howManyCardsToTake += 2;
                    this.game.playdeck.deck.add(0, card);
                    removeCardFromPlayer(card, player);
                    this.game.turn_ended = true;
                    break;
                }
                case SKIP: {
                    this.game.playdeck.deck.add(0, card);
                    removeCardFromPlayer(card, player);
                    this.game.skipNextPlayer = true;
                    this.game.turn_ended = true;
                    break;
                }
                case CHOOSECOLOR: {
                    this.game.playdeck.deck.add(0, card);
                    removeCardFromPlayer(card, player);
                    this.game.turn_ended = true;
                    // nothing else yet
                    break;
                }
                case RETOUR: {
                    if (this.game.turns_clockwise) {
                        this.game.turns_clockwise = false;
                    } else {
                        this.game.turns_clockwise = true;
                    }
                    this.game.playdeck.deck.add(0, card);
                    removeCardFromPlayer(card, player);
                    this.game.turn_ended = true;
                }
                default:  {
                    this.game.turn_ended = true;
                }

            }
        } else
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!That's not possible!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void					removeCardFromPlayer(Card card, Player player) {
        for (Card i : this.hand) {
            if ((i.color == card.color) && (i.value == card.value)) {
                player.hand.remove(player.hand.indexOf(i));
                break;
            }
        }
    }

}
