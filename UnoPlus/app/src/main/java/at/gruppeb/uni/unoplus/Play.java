package classes;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import interfaces.ICard;

/**
 * Created by Luki on 05.04.2015.
 */
public class Play {
    private NewCardsStack cardsStack;
    List<Card> playStack;

    public Play(){
        cardsStack = new NewCardsStack();
        playStack = new ArrayList<>();
    }


}
