package Uno;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import IUno.IStack;

/**
 * Created by Luki on 18.03.2015.
 */
public class NewCardsStack implements IStack {
    private List<Card> _stack;

    private enum colors{rot,gelb,blau,gruen}
    private enum actions{plus2,change_dir,wait}


    public NewCardsStack() {
        initialize();
    }

    private void shuffleStack(){
        _stack = new ArrayList<>();
        Collections.shuffle(_stack);
    }

    private void initialize() {
        for(colors c : colors.values()){
            for(int i = 1; i<=9; i++){
                _stack.add(new NormalCard(c.toString()+i,c.toString(),i));
            }
            for(actions a : actions.values()){
                _stack.add(new ActionCard(c.toString()+a.toString(),a.toString(),c.toString()));
            }
        }

        _stack.add(new ActionCard("plus4,black","plus4","black"));
        _stack.add(new ActionCard("plus4,black","plus4","black"));
        _stack.add(new ActionCard("plus4,black","plus4","black"));
        _stack.add(new ActionCard("plus4,black","plus4","black"));

        _stack.add(new ActionCard("change_col;black","change_col","black"));
        _stack.add(new ActionCard("change_col;black","change_col","black"));
        _stack.add(new ActionCard("change_col;black","change_col","black"));
        _stack.add(new ActionCard("change_col;black","change_col","black"));
        shuffleStack();

    }

    @Override
    public Card getTopCard() {
        Card help = this._stack.get(0);
        this._stack.remove(0);
        return help;
    }
}
