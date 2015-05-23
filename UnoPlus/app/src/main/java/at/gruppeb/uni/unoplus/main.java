package at.gruppeb.uni.unoplus;

import java.util.ArrayList;
import java.util.Collections;

public class main {
	boolean continue=true; // if Player.hand==null continue=false;

	public static void main(String[] args) {
		System.out.println("\n====================\nStarting game...\n====================\n");
		
		int num_players = 4;
		Gamemanager game = new Gamemanager(num_players);

		Deck takedeck = new Deck(); 							//create takedeck deck where players take cards from
		
		Deck playdeck = new Deck(); 							// create deck where players put cards down
		
		game.createCards(takedeck);								//create cards and put it in takedeck deck
				
		game.players_init();									//create players and add them to the list
		
		game.dealCards(takedeck);								// give each player 7 cards
		
		game.putFirstCardDown(takedeck, playdeck); 				// take first card from takedeck and put it as first card in the playdeck so we have an initial card
		
		gameloop(game);											// not yet implemented
	}
	
	public static void gameloop(Gamemanager game) {
		while(continue) {
			int i=0;
			while(i<game.players.size())
				continue=game.checkHand(game.players(i));// check if there is a player without card in his hand. For return=true,the player has some cards.
			

			
		}
	}

}

