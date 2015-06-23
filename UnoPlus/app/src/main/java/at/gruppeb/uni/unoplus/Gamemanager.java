package at.gruppeb.uni.unoplus;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.math.*;

import bluetooth.BluetoothService;

public class Gamemanager {

    int num_players = 0;
    int current_player = 0;
    int howManyCardsToTake = 0;
    boolean turns_clockwise = true;
    boolean game_ended = false;
    Deck playdeck;
    Deck takedeck;
    GameActivity gameActivity;

    public Gamemanager(BluetoothService mBlt, GameActivity gameActivity) {
        this.num_players = mBlt.getNrOfPlayers();
        this.gameActivity = gameActivity;

    }

//	public boolean 				sayUNO(int id) {
//									if (hand.size() == 0) {
//										return true;
//									}
//									return false;
//								}


    public void createCards() {
        System.out.println("\n====================\n" + "Creating cards...\n====================\n");
        createZeroCards();
        createNormalCards();
        createSpecialCards();
        Collections.shuffle(takedeck.deck);
        System.out.println(this.takedeck.deck.size() + " cards created and put in takedeck");
    }

    public void createZeroCards() {
        for (int color = 0; color < 4; color++) {                        // create basic cards with number 0, 1 of each kind
            Card temp = new Card(Card.colors.values()[color], Card.values.ZERO);
            this.takedeck.deck.add(temp);
            System.out.println("Card " + temp.get_name() + " created");
        }
    }

    public void createNormalCards() {
        for (int color = 0; color < 4; color++) {                        //create basic cards with numbers 1~9, 2 of each kind
            for (int type = 1; type < 10; type++) {
                for (int amount = 0; amount < 2; amount++) {
                    Card temp = new Card(Card.colors.values()[color], Card.values.values()[type]);
                    this.takedeck.deck.add(temp);
                    System.out.println("Card " + temp.get_name() + " created");
                }
            }
        }
    }

    public void createSpecialCards() {
        for (int color = 0; color < 4; color++) {                        // create take-two cards, retour cards and skip cards of all four colors, 2 of each kind
            for (int type = 10; type < 13; type++) {                // enum values 10~12 are "SKiP", "TAKETWO" and "RETOUR"
                for (int amount = 0; amount < 2; amount++) {
                    Card temp = new Card(Card.colors.values()[color], Card.values.values()[type]);
                    this.takedeck.deck.add(temp);
                    System.out.println("Card " + temp.get_name() + " created");
                }
            }
        }
        for (int type = 13; type < 15; type++) {                        // create TAKEFOUR and CHOOSECOLOR cards, four of each kind
            for (int amount = 0; amount < 4; amount++) {
                Card temp = new Card(Card.colors.BLACK, Card.values.values()[type]);
                this.takedeck.deck.add(temp);
                System.out.println("Card " + temp.get_name() + " created");
            }
        }
    }

    public void serverloop(BluetoothService mBlt) {

    }

    public void endTurn(int offset) {
        if (this.turns_clockwise == true) {
            this.current_player = (this.current_player + offset + 1) % this.num_players;
        } else if (this.turns_clockwise == false) {
            if (this.current_player == 0) {
                this.current_player = num_players - 1 - offset;
            } else {
                this.current_player = (this.current_player - offset - 1) % this.num_players;
            }
        }
    }

    public void dealCards(BluetoothService mBlt) {

    }

    public void putFirstCardDown() {
        this.playdeck.deck.add(0, this.takedeck.deck.get(0));
        String cardcode = this.playdeck.deck.get(0).getCodedName();
        String playdeckString = "playdeck" + cardcode;
        this.gameActivity.sendMessage(playdeckString);
        this.gameActivity.player.playdeckTop = this.playdeck.deck.get(0);
        this.takedeck.deck.remove(0);
        System.out.println(this.playdeck.deck.size() + " card currently in playdeck, and that card is " + this.playdeck.deck.get(0).get_name());
    }


    public void decksinit() {
        this.takedeck = new Deck();                            //create takedeck deck where players take cards from
        this.playdeck = new Deck();                            // create deck where players put cards down

    }

    public void takeManyCards() {


    }
}