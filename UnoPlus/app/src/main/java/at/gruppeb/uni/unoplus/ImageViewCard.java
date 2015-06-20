package at.gruppeb.uni.unoplus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by Natascha on 18/05/2015.
 */
public class ImageViewCard extends ImageView {
    private Card card;

    public ImageViewCard(Context context, Card card) {
        super(context);
        this.card = card;
        generateImageView();
    }

    private void generateImageView() {
        Drawable img = getDrawableForCard(this.card, this);
        this.setBackground(img);

    }

    //TODO look for right place in code
    public static Drawable getDrawableForCard(Card card, ImageView imageView) {
        Drawable img = imageView.getResources().getDrawable(R.drawable.back);
        if (card.color.equals(Card.colors.RED)) {

            switch (card.value) {
                case ZERO:
                    img = imageView.getResources().getDrawable(R.drawable.red0);
                    break;
                case ONE:
                    img = imageView.getResources().getDrawable(R.drawable.red1);
                    break;
                case TWO:
                    img = imageView.getResources().getDrawable(R.drawable.red2);
                    break;
                case THREE:
                    img = imageView.getResources().getDrawable(R.drawable.red3);
                    break;
                case FOUR:
                    img = imageView.getResources().getDrawable(R.drawable.red4);
                    break;
                case FIVE:
                    img = imageView.getResources().getDrawable(R.drawable.red5);
                    break;
                case SIX:
                    img = imageView.getResources().getDrawable(R.drawable.red6);
                    break;
                case SEVEN:
                    img = imageView.getResources().getDrawable(R.drawable.red7);
                    break;
                case EIGHT:
                    img = imageView.getResources().getDrawable(R.drawable.red8);
                    break;
                case NINE:
                    img = imageView.getResources().getDrawable(R.drawable.red9);
                    break;
                case TAKE_TWO:
                    img = imageView.getResources().getDrawable(R.drawable.red_plus2);
                    break;
                case RETOUR:
                    img = imageView.getResources().getDrawable(R.drawable.red_retour);
                    break;
                case SKIP:
                    img = imageView.getResources().getDrawable(R.drawable.red_skip);
                    break;
                default:
                    img = imageView.getResources().getDrawable(R.drawable.back);
                    break;
            }
        } else if (card.color.equals(Card.colors.GREEN)) {
            switch (card.value) {
                case ZERO:
                    img = imageView.getResources().getDrawable(R.drawable.green0);
                    break;
                case ONE:
                    img = imageView.getResources().getDrawable(R.drawable.green1);
                    break;
                case TWO:
                    img = imageView.getResources().getDrawable(R.drawable.green2);
                    break;
                case THREE:
                    img = imageView.getResources().getDrawable(R.drawable.green3);
                    break;
                case FOUR:
                    img = imageView.getResources().getDrawable(R.drawable.green4);
                    break;
                case FIVE:
                    img = imageView.getResources().getDrawable(R.drawable.green5);
                    break;
                case SIX:
                    img = imageView.getResources().getDrawable(R.drawable.green6);
                    break;
                case SEVEN:
                    img = imageView.getResources().getDrawable(R.drawable.green7);
                    break;
                case EIGHT:
                    img = imageView.getResources().getDrawable(R.drawable.green8);
                    break;
                case NINE:
                    img = imageView.getResources().getDrawable(R.drawable.green9);
                    break;
                case TAKE_TWO:
                    img = imageView.getResources().getDrawable(R.drawable.green_plus2);
                    break;
                case RETOUR:
                    img = imageView.getResources().getDrawable(R.drawable.green_retour);
                    break;
                case SKIP:
                    img = imageView.getResources().getDrawable(R.drawable.green_skip);
                    break;
                default:
                    img = imageView.getResources().getDrawable(R.drawable.back);
                    break;
            }
        } else if (card.color.equals(Card.colors.BLUE)) {
            switch (card.value) {
                case ZERO:
                    img = imageView.getResources().getDrawable(R.drawable.blue0);
                    break;
                case ONE:
                    img = imageView.getResources().getDrawable(R.drawable.blue1);
                    break;
                case TWO:
                    img = imageView.getResources().getDrawable(R.drawable.blue2);
                    break;
                case THREE:
                    img = imageView.getResources().getDrawable(R.drawable.blue3);
                    break;
                case FOUR:
                    img = imageView.getResources().getDrawable(R.drawable.blue4);
                    break;
                case FIVE:
                    img = imageView.getResources().getDrawable(R.drawable.blue5);
                    break;
                case SIX:
                    img = imageView.getResources().getDrawable(R.drawable.blue6);
                    break;
                case SEVEN:
                    img = imageView.getResources().getDrawable(R.drawable.blue7);
                    break;
                case EIGHT:
                    img = imageView.getResources().getDrawable(R.drawable.blue8);
                    break;
                case NINE:
                    img = imageView.getResources().getDrawable(R.drawable.blue9);
                    break;
                case TAKE_TWO:
                    img = imageView.getResources().getDrawable(R.drawable.blue_plus2);
                    break;
                case RETOUR:
                    img = imageView.getResources().getDrawable(R.drawable.blue_retour);
                    break;
                case SKIP:
                    img = imageView.getResources().getDrawable(R.drawable.blue_skip);
                    break;
                default:
                    img = imageView.getResources().getDrawable(R.drawable.back);
                    break;
            }
        } else if (card.color.equals(Card.colors.YELLOW)) {
            switch (card.value) {
                case ZERO:
                    img = imageView.getResources().getDrawable(R.drawable.yellow0);
                    break;
                case ONE:
                    img = imageView.getResources().getDrawable(R.drawable.yellow1);
                    break;
                case TWO:
                    img = imageView.getResources().getDrawable(R.drawable.yellow2);
                    break;
                case THREE:
                    img = imageView.getResources().getDrawable(R.drawable.yellow3);
                    break;
                case FOUR:
                    img = imageView.getResources().getDrawable(R.drawable.yellow4);
                    break;
                case FIVE:
                    img = imageView.getResources().getDrawable(R.drawable.yellow5);
                    break;
                case SIX:
                    img = imageView.getResources().getDrawable(R.drawable.yellow6);
                    break;
                case SEVEN:
                    img = imageView.getResources().getDrawable(R.drawable.yellow7);
                    break;
                case EIGHT:
                    img = imageView.getResources().getDrawable(R.drawable.yellow8);
                    break;
                case NINE:
                    img = imageView.getResources().getDrawable(R.drawable.yellow9);
                    break;
                case TAKE_TWO:
                    img = imageView.getResources().getDrawable(R.drawable.yellow_plus2);
                    break;
                case RETOUR:
                    img = imageView.getResources().getDrawable(R.drawable.yellow_retour);
                    break;
                case SKIP:
                    img = imageView.getResources().getDrawable(R.drawable.yellow_skip);
                    break;
                default:
                    img = imageView.getResources().getDrawable(R.drawable.back);
                    break;
            }
        } else if (card.color.equals(Card.colors.BLACK)) {
            switch (card.value) {
                case TAKE_FOUR:
                    img = imageView.getResources().getDrawable(R.drawable.black4);
                    break;
                case CHOOSE_COLOR:
                    img = imageView.getResources().getDrawable(R.drawable.blackcolor);
                    break;
                default:
                    img = imageView.getResources().getDrawable(R.drawable.back);
                    break;
            }

        }
        return img;
    }

    public Card getCard() {
        return this.card;
    }

    public String toString() {
        return this.getCard().get_name();
    }
}
