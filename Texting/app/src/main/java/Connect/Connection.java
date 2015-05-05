package Connect;

/**
 * Created by Luki on 05.05.2015.
 */
public class Connection implements IConnection{

    Translator t = new Translator();

    @Override
    public Object[] getHandCards(int anzahl, int id) {
        t.translate("receive;getHandCards;null");

        return new Object[0];
    }

    @Override
    public boolean discardHandCard(Object card, int id) {
        return false;
    }

    @Override
    public Object getCurrentOpenCard() {
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
    public void setNextPlayer(){

    }

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
