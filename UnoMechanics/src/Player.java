import java.util.ArrayList;
import java.util.List;


public class Player {

	static int 					static_id = 0;
	List<Card>		 			hand;
	int 						player_id = 0;
	
	public 						Player() {
									this.player_id = Player.static_id;
									this.hand = new ArrayList<Card>();
									static_id++;
								}
	

	public List<Card>			getHand() {
									return this.hand;
								}
	
	public Card					layCard() {
									return this.hand.get(0);				// needs interface implement later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								}
	
	public void					takeCard (Deck takedeck) {				// take card from takedeck
									this.hand.add(0, takedeck.deck.get(0));
									takedeck.deck.remove(0);
								}
	
	public void					playCard (Card card, Deck playdeck) {
									if (card.color == playdeck.deck.get(0).color) {					// if color matches
										playdeck.deck.add(0, card);
										removeCardFromPlayer(card);
									} else if (card.value == playdeck.deck.get(0).value) {			// if amount matches
										playdeck.deck.add(0, card);
										removeCardFromPlayer(card);
									} 																// not finished yet
								}
	
	public void					removeCardFromPlayer(Card card) {
									for (Card i : this.hand) {
										if ((i.color == card.color) && (i.value == card.value)) {
											this.hand.remove(i);
										}
									}
								}
	
}
