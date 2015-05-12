package Connect;

import IUno.ICard;

/**
 * Created by Luki on 05.05.2015.
 */
public class Connection implements IConnection{

    Translator t = new Translator();

    @Override
    public ICard[] getHandCards(int anzahl, int id) {

        return new ICard[0];
    }

    @Override
    public boolean discardHandCard(Object card, int id) {
        return false;
    }

    @Override
    public ICard getCurrentOpenCard() {
        return null;
    }

    @Override
    public void setCurrentOpenCard() {

    }

    @Override
    public boolean sayUNO(int id) {
        return false;
    }

    @Override
    public int currentPlayer() {
        return 0;
    }

    @Override
    public void setNextPlayer(){ }

    @Override
    public int getNextPlayer(){
        return 0;
    }

    @Override
    public int changePlayDirection() {
        return 0;
    }

    @Override
    public int stepOverNextPlayer() {
        return 0;
    }

}
