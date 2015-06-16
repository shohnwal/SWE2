package at.gruppeb.uni.unoplus;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.math.*;

public class Gamemanager {

    int							num_players;
    int                         current_player;
    int							howManyCardsToTake = 0;
    boolean 					turns_clockwise = true;
    boolean 					game_ended = false;
    boolean						skipNextPlayer = false;
    Deck						playdeck;
    Deck						takedeck;

    public 						Gamemanager(BluetoothService mBlt) {
        this.num_players = mBlt.getNrOfPlayer();
        int randomplayer = Math.random(0, this.num_players-1);
        this.current_player = randomplayer;

    }

//	public boolean 				sayUNO(int id) {
//									if (hand.size() == 0) {
//										return true;
//									}
//									return false;
//								}


    public void 				createCards(){
        System.out.println("\n====================\n" + "Creating cards...\n====================\n");
        createZeroCards();
        createNormalCards();
        createSpecialCards();
        System.out.println(this.takedeck.deck.size() + " cards created and put in takedeck");
    }

    public void 				createZeroCards() {
        for (int color = 0; color < 4; color++) {						// create basic cards with number 0, 1 of each kind
            Card temp = new Card(Card.colors.values()[color], Card.values.ZERO);
            this.takedeck.deck.add(temp);
            System.out.println("Card " + temp.get_name() + " created");
        }
    }

    public void 				createNormalCards() {
        for (int color = 0; color < 4; color++) {						//create basic cards with numbers 1~9, 2 of each kind
            for (int type = 1; type<10; type++) {
                for (int amount = 0; amount < 2; amount++) {
                    Card temp = new Card(Card.colors.values()[color], Card.values.values()[type]);
                    this.takedeck.deck.add(temp);
                    System.out.println("Card " + temp.get_name() + " created");
                }
            }
        }
    }

    public void					createSpecialCards() {
        for (int color = 0; color < 4; color++) {						// create take-two cards, retour cards and skip cards of all four colors, 2 of each kind
            for (int type = 10; type < 13; type++) {				// enum values 10~12 are "SKiP", "TAKETWO" and "RETOUR"
                for (int amount = 0; amount < 2; amount++) {
                    Card temp = new Card(Card.colors.values()[color], Card.values.values()[type]);
                    this.takedeck.deck.add(temp);
                    System.out.println("Card " + temp.get_name() + " created");
                }
            }
        }
        for (int type = 13; type < 15; type++) {						// create TAKEFOUR and CHOOSECOLOR cards, four of each kind
            for (int amount = 0; amount < 4; amount++) {
                Card temp = new Card(Card.colors.BLACK, Card.values.values()[type]);
                this.takedeck.deck.add(temp);
                System.out.println("Card " + temp.get_name() + " created");
            }
        }
    }

    public void serverloop(BluetoothService mBlt) {
        for (String messagestring: mBlt.mConnThreads) {

            int playernumber =(int) messagestring.substring(1, 2);
            if (playernumber == this.current_player) {
                String command = messagestring.substring(3,6);
                switch (command)
                {
                    case get:
                        break;
                    case tak:
                        String sendstring = "p" + current_player + "get";
                        String cStr="";
                        if(this.takedeck.get(0).color != BLACK){
                            cStr+=takedeck.get(0).color.toString().substring(0,1);
                        }else {
                            cStr+='S';
                        }
                        int Ord=takedeck.get(0).value.ordinal();
                        if(Ord>=9){
                            cStr+=Ord;
                        }else if(Ord==10){
                            cStr+='S';
                        }else if(Ord==11){
                            cStr+='X';
                        }else if(Ord==12){
                            cStr+='R';
                        }else if(Ord==13){
                            cStr+='Y';
                        }else if(Ord==14){
                            cStr+='C';
                        }
                        sendstring += cStr;
                        mBlt.mConnThreads.remove(messagestring);
                        mBlt.write(sendstring);
                        mBlt.write(this.getEndTurnString(0));
                        this.takedeck.remove(0);
                    case ply:
                        String color = messagestring.substring(6,7);
                        String value = messagestring.substring(messagestring.length()-1);
                        this.playdeck.add(new Card(color, value));
                        if (color == "S" && value == "Y"){
                            this.howManyCardsToTake += 4;
                        } else if (value == "X") {
                            this.howManyCardsToTake += 2;
                        }
                        String playdeckString = "playdeck" + color + value;
                        mBlt.write(playdeckString);
                        if (value == "S") {
                            mBlt.write(this.getEndTurnString(1));
                        } else {
                            mBlt.write(this.getEndTurnString(0));
                        }
                        mBlt.mConnThreads.remove(messagestring);
                        break;
                    case set:
                        break;
                    case uno:
                        (int)unonr = messagestring.substring(messagestring.length()-1);
                        if (unonr == 1) {
                            //..
                        } else if (unonr == 2) {
                            mBlt.write("gameend");
                        } break;
                    default:break;
                }
            }
        }
    }

    public String getEndTurnString(int offset) {
        String endturnString = "P";
        if (this.turns_clockwise == true) {
            this.current_player = (this.current_player + offset + 1)%this.num_players;
        } else if(this.turns_clockwise == false) {
            if (this.current_player == 0) {
                this.current_player = num_players -1 - offset;
            } else {
                this.current_player = (this.current_player - offset - 1) % this.num_players;
            }
        }
        endturnString += this.current_player + "set";
        return endturnstring;
    }

    public void dealCards(BluetoothService mBlt) {
        for (int i=1 ; i<this.num_players-1;i++){
            String superstring= "p"+mBlt.getPlayerId();
            String cStr="";
            for (int j=0;j<7;j++){
                cStr="";
                if(this.takedeck.get(0).color != BLACK){
                    cStr+=takedeck.get(0).color.toString().substring(0,1);
                }else {
                    cStr+='S';
                }
                int Ord=takedeck.get(0).value.ordinal();
                if(Ord>=9){
                    cStr+=Ord;
                }else if(Ord==10){
                    cStr+='S';
                }else if(Ord==11){
                    cStr+='X';
                }else if(Ord==12){
                    cStr+='R';
                }else if(Ord==13){
                    cStr+='Y';
                }else if(Ord==14){
                    cStr+='C';
                }
                superstring+=cStr;
                this.takedeck.remove(0);
            }
            mBlt.write(superstring);
        }

    }

    public void 				putFirstCardDown() {
        System.out.println("\n====================\nPutting first card on playdeck...\n====================\n");
        this.playdeck.deck.add(0,this.takedeck.deck.get(0));
        this.takedeck.deck.remove(0);
        System.out.println(this.playdeck.deck.size() + " card currently in playdeck, and that card is " + this.playdeck.deck.get(0).get_name());
    }


    public void					decks_init() {
        this.takedeck = new Deck(); 							//create takedeck deck where players take cards from
        this.playdeck = new Deck(); 							// create deck where players put cards down

    }

    public void 				endTurn() {
        this.getCurrentPlayer().setIsMyTurn(false);
        this.current_player = ( this.current_player + 1 ) % (this.num_players);
        this.getCurrentPlayer().setIsMyTurn(true);
        System.out.println("next player....");
        this.turn_ended = false;
    }
    public void 				fillEmptyTakeDeck(){
        while(this.playdeck.deck.size()>1){// The loop will remove cards from playdeck and putting it in takedeck until only one card remains in playdeck
            this.takedeck.deck.add(this.playdeck.deck.size()-1);//Taking the last card from playdeck
            this.playdeck.deck.remove(this.playdeck.deck.size()-1);//Removing the card from playdeck
        }
        if(this.playdeck.deck.checkEmptyDeck()){ // this is not necessary,it's like an exception
            this.playdeck.deck.add(this.takedeck.deck(0));
            this.takedeck.deck.remove(0);
        }

        Collections.shuffle(this.takedeck.deck);//shuffle the deck
    }
}
