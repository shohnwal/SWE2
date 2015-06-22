package at.gruppeb.uni.unoplus;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.math.*;

import bluetooth.BluetoothService;

public class Gamemanager {

    int							num_players = 0;
    int                         current_player=0;
    int							howManyCardsToTake = 0;
    boolean 					turns_clockwise = true;
    boolean 					game_ended = false;
    Deck						playdeck;
    Deck						takedeck;
    GameActivity                gameActivity;

    public 						Gamemanager(BluetoothService mBlt,GameActivity gameActivity) {
        this.num_players = mBlt.getNrOfPlayers();
        this.gameActivity=gameActivity;

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
        Collections.shuffle(takedeck.deck);
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
        System.out.print("Serverloop active...");
        for (String messagestring: this.gameActivity.stringList) {
            System.out.print("serverloop untersucht nachricht : " + messagestring);
            if(howManyCardsToTake > 0 )
                this.takeManyCards();


            int playernumber = Integer.parseInt(messagestring.substring(1, 2));
            if (playernumber == this.current_player) {
                String command = messagestring.substring(3, 6);
                switch (command)
                {
                    case "get":
                        break;
                    case "tak":
                        String sendstring = "p" + current_player + "get";
                        String cStr="";
                        if(this.takedeck.deck.get(0).color != Card.colors.BLACK){
                            cStr+=takedeck.deck.get(0).color.toString().substring(0,1);
                        }else {
                            cStr+='S';
                        }
                        int Ord=takedeck.deck.get(0).value.ordinal();
                        if(Ord<=9){
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
                        this.gameActivity.stringList.remove(messagestring);
                        this.gameActivity.sendMessage(sendstring);
                        this.gameActivity.sendMessage(this.getEndTurnString(0));
                        System.out.println("incoming message : " + sendstring);
                        this.takedeck.deck.remove(0);
                    case "ply":
                        String color = messagestring.substring(6,7);
                        String value = messagestring.substring(messagestring.length()-1);
                        this.playdeck.deck.add(new Card(color, value));
                        if (color == "S" && value == "Y"){
                            this.howManyCardsToTake += 4;
                        } else if (value == "X") {
                            this.howManyCardsToTake += 2;
                        } else if (value== "R"){
                            turns_clockwise=!turns_clockwise;
                        }
                        String playdeckString = "playdeck" + color + value;
                        this.gameActivity.sendMessage(playdeckString);
                        System.out.println(" incoming playdeck info : " + playdeckString);

                        if (value == "S") {
                            this.gameActivity.sendMessage(this.getEndTurnString(1));
                        } else {
                            this.gameActivity.sendMessage(this.getEndTurnString(0));
                        }
                        if(howManyCardsToTake > 0 )
                            this.takeManyCards();
                        this.gameActivity.stringList.remove(messagestring);
                        break;
                    case "set":
                        break;
                    case "uno":
                        int unonr = Integer.parseInt(messagestring.substring(messagestring.length() - 1));
                        if (unonr == 1) {

                        } else if (unonr == 2) {
                            String temp="gameend"+current_player;
                            this.gameActivity.sendMessage(temp);
                        } break;
                    default:break;
                }
            }
        }
    }

    public String getEndTurnString(int offset) {
        String endturnString = "";
        if (this.turns_clockwise == true) {
            this.current_player = (this.current_player + offset + 1)%this.num_players;
        } else if(this.turns_clockwise == false) {
            if (this.current_player == 0) {
                this.current_player = num_players -1 - offset;
            } else {
                this.current_player = (this.current_player - offset - 1) % this.num_players;
            }
        }
        endturnString =endturnString + this.current_player + "set";
        return endturnString;
    }

    public void dealCards(BluetoothService mBlt) {
        System.out.println("\n====================\ndealing cards...\n====================\n");
        if(this.gameActivity.player.player_id == 0){
            for(int i=0; i<7;i++){
                this.gameActivity.player.hand.add(this.takedeck.deck.get(0));
                this.takedeck.deck.remove(0);
            }
        }
        for (int i=1 ; i<this.num_players;i++){
            for (int j=0;j<7;j++){
                String sendstring = "p" + i + "get";
                String cStr="";
                if(this.takedeck.deck.get(0).color != Card.colors.BLACK){
                    cStr+=takedeck.deck.get(0).color.toString().substring(0,1);
                }else {
                    cStr+='S';
                }
                int Ord=takedeck.deck.get(0).value.ordinal();
                if(Ord<=9){
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
                this.gameActivity.sendMessage(sendstring);
                System.out.println("message sent while dealing cards : " + sendstring);
                this.takedeck.deck.remove(0);
            /*    cStr="";
                if(this.takedeck.deck.get(0).color != Card.colors.BLACK){
                    cStr+=takedeck.deck.get(0).color.toString().substring(0,1);
                }else {
                    cStr+='S';
                }
                int Ord=takedeck.deck.get(0).value.ordinal();
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
                superstring+=cStr; */
            }
        }

    }

    public void 				putFirstCardDown() {
        System.out.println("\n====================\nPutting first card on playdeck...\n====================\n");
        this.playdeck.deck.add(0, this.takedeck.deck.get(0));
        String cardcode = this.playdeck.deck.get(0).getCodedName();
        String playdeckString = "playdeck" + cardcode;
        this.gameActivity.sendMessage(playdeckString);
        this.gameActivity.player.playdeckTop = this.playdeck.deck.get(0);
        this.takedeck.deck.remove(0);
        System.out.println(this.playdeck.deck.size() + " card currently in playdeck, and that card is " + this.playdeck.deck.get(0).get_name());
    }


    public void					decksinit() {
        System.out.println("\n====================\nInitializing decks...\n====================\n");
        this.takedeck = new Deck(); 							//create takedeck deck where players take cards from
        this.playdeck = new Deck(); 							// create deck where players put cards down

    }
    public void takeManyCards(){
        System.out.println("\n====================\ntaking many cards...\n====================\n");
        String cStr="";
        while(this.howManyCardsToTake > 0){
            if (this.gameActivity.currentPlayerID == 0) {
                this.gameActivity.player.takeCard(this.gameActivity.mBltService);
                this.howManyCardsToTake--;
            }
            else {
                cStr="p"+current_player+"get";
                if(this.takedeck.deck.get(0).color != Card.colors.BLACK){
                    cStr+=takedeck.deck.get(0).color.toString().substring(0,1);
                }else {
                    cStr+='S';
                }
                int Ord=takedeck.deck.get(0).value.ordinal();
                if(Ord<=9){
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
                //superstring+=cStr;
                this.gameActivity.sendMessage(cStr);
                this.takedeck.deck.remove(0);
                this.howManyCardsToTake--;
            }
        }

        }
    }
