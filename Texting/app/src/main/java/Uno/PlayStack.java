package Uno;

import java.util.ArrayList;
import java.util.List;
import IUno.IStack;


/**
 * Created by Luki on 18.03.2015.
 */
public class PlayStack implements IStack {
    private List<Card> _stack;

    public PlayStack(){
        this._stack = new ArrayList<Card>();
    }

    @Override
    public Card getTopCard() {
        return this._stack.get(_stack.size()-1);
    }

    public boolean setTopCard(Card card){

        if(_stack.isEmpty()) {
            this._stack.add(card);
            return true;
        }
        if(card.get_color().equals("black")){
            this._stack.add(card);
            return true;
        }
        if(card.isActionCard()){
            if(_stack.get(_stack.size()-1).get_color().equals(card.get_color())){
                this._stack.add(card);
                return true;
            }
            else {
                return false;
            }
        }else{

        }


        return false;
    }
}
