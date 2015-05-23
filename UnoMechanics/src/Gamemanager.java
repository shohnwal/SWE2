import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Gamemanager {
	
	List<Player>	 			players; 			//create list of players
	int							num_players;

	public 						Gamemanager(int num_players) {
									players = new ArrayList<Player>();
									this.num_players = num_players;
								}
	
//	public boolean 				sayUNO(int id) {
//									if (hand.size() == 0) {
//										return true; 
//									}
//									return false;
//								}
				
	
	
	public void 				createCards(Deck takedeck){
									System.out.println("\n====================\n" + "Creating cards...\n====================\n");
									createZeroCards(takedeck);
									createNormalCards(takedeck);
									createSpecialCards(takedeck); 
									System.out.println(takedeck.deck.size() + " cards created and put in takedeck");
								}	

	public void 				createZeroCards(Deck takedeck) {
									for (int color = 0; color < 4; color++) {						// create basic cards with number 0, 1 of each kind
										Card temp = new Card(Card.colors.values()[color], Card.values.ZERO);
										takedeck.deck.add(temp);
										System.out.println("Card " + temp.get_name() + " created"); 
									}
								}

	public void 				createNormalCards(Deck takedeck) {
									for (int color = 0; color < 4; color++) {						//create basic cards with numbers 1~9, 2 of each kind						
										for (int type = 1; type<10; type++) {
											for (int amount = 0; amount < 2; amount++) {
												Card temp = new Card(Card.colors.values()[color], Card.values.values()[type]);
												takedeck.deck.add(temp);
												System.out.println("Card " + temp.get_name() + " created");
											}
										}
									}
								}

	public void					createSpecialCards(Deck takedeck) {
									for (int color = 0; color < 4; color++) {						// create take-two cards, retour cards and skip cards of all four colors, 2 of each kind
										for (int type = 10; type < 13; type++) {				// enum values 10~12 are "SKiP", "TAKE_TWO" and "RETOUR"
											for (int amount = 0; amount < 2; amount++) {
												Card temp = new Card(Card.colors.values()[color], Card.values.values()[type]);
												takedeck.deck.add(temp);
												System.out.println("Card " + temp.get_name() + " created");
											}
										}
									}
									for (int type = 13; type < 15; type++) {						// create TAKE_FOUR and CHOOSE_COLOR cards, four of each kind
										for (int amount = 0; amount < 4; amount++) {
											Card temp = new Card(Card.colors.BLACK, Card.values.values()[type]);
											takedeck.deck.add(temp);
											System.out.println("Card " + temp.get_name() + " created");
										}
									}
								}

	public void 				dealCards(Deck takedeck) {
									System.out.println("\n====================\nShuffeling cards in takedeck...\n====================\n");
									Collections.shuffle(takedeck.deck);
									System.out.println("\n====================\nDealing cards...\n====================\n");
									for (Player player : this.players) {
										for (int j = 0; j < 7; j++) {
											System.out.println("Top card of takedeck is " + takedeck.deck.get(0).get_name());
											player.hand.add(takedeck.deck.get(0));
											takedeck.deck.remove(0);
											System.out.println("Player " + player.player_id + " got card " + player.hand.get(player.hand.size()-1).get_name());
										}
									}
									System.out.println("\n====================\ncards dealt to all " + players.size() + " players\n====================\n");
								}
	
	public void 				putFirstCardDown(Deck takedeck, Deck playdeck) {
									System.out.println("\n====================\nPutting first card on playdeck...\n====================\n");
									playdeck.deck.add(0,takedeck.deck.get(0));
									takedeck.deck.remove(0);
									System.out.println(playdeck.deck.size() + " card currently in playdeck, and that card is " + playdeck.deck.get(0).get_name());
								}
	
	public void 				players_init() {
									System.out.println("\n====================\nCreating players...\n====================\n");
									for (int i = 0; i < this.num_players; i++) {
										Player player = new Player();
										this.players.add(player);
										System.out.println("Player# " + player.player_id + " created, playerlist has now " + this.players.size() + " players");
									}
									System.out.println(players.size() + " players play the game");
								}
}
