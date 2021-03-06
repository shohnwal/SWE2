package at.gruppeb.uni.unoplus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

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
        }
    }

    public void createNormalCards() {
        for (int color = 0; color < 4; color++) {                        //create basic cards with numbers 1~9, 2 of each kind
            for (int type = 1; type < 10; type++) {
                for (int amount = 0; amount < 2; amount++) {
                    Card temp = new Card(Card.colors.values()[color], Card.values.values()[type]);
                    this.takedeck.deck.add(temp);
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
                }
                }
        }
        for (int type = 13; type < 15; type++) {                        // create TAKEFOUR and CHOOSECOLOR cards, four of each kind
            for (int amount = 0; amount < 4; amount++) {
                Card temp = new Card(Card.colors.BLACK, Card.values.values()[type]);
                this.takedeck.deck.add(temp);
            }
        }
    }

    public void serverloop(BluetoothService mBlt) {
        if(gameActivity.gameObject != null){
            if(gameActivity.gameObject.isChanged()){
                if (gameActivity.gameObject.getTakedeck().deck.size() == 0) {
                   this.fillEmptyTakeDeck();
                }
                gameActivity.gameObject.setChanged(false);
                gameActivity.sendMessage(gameActivity.gameObject);
            }
        }
    }

    public void fillEmptyTakeDeck() {
        for (int i = this.gameActivity.gameObject.getPlaydeck().deck.size()-1; i > 1 ; i--) {
            this.gameActivity.gameObject.getTakedeck().deck.add(this.gameActivity.gameObject.getPlaydeck().deck.get(i));
            this.gameActivity.gameObject.getPlaydeck().deck.remove(i);
        }
        Collections.shuffle(this.gameActivity.gameObject.getTakedeck().deck);
    }



    public Vector<ArrayList<Card>> dealCards(BluetoothService mBlt) {
        Vector<ArrayList<Card>> hands = new Vector<>();
        ArrayList<Card> hand = new ArrayList<>();
        Card c = null;
        for(int i = 0; i<mBlt.getNrOfPlayers();i++){
            for(int j = 0; j<7;j++){
                hand.add(this.takedeck.takeTopCard());
            }
            hands.add(hand);
            hand = new ArrayList<>();
        }

        return hands;

    }

    public void putFirstCardDown() {
        this.playdeck.deck.add(0, this.takedeck.deck.get(0));
        if (this.playdeck.deck.get(0).color == Card.colors.BLACK) {
            this.playdeck.deck.get(0).color = Card.colors.getRandom();
        }
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