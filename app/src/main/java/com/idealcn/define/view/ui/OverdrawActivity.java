package com.idealcn.define.view.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.idealcn.define.view.R;
import com.idealcn.define.view.view.card.MultiCardsView;
import com.idealcn.define.view.view.card.SingleCard;

/**
 * 过度绘制
 */
public class OverdrawActivity extends AppCompatActivity {
    private int cardResId[] = {R.mipmap.test3,
            R.mipmap.test3,
            R.mipmap.test3,
            R.mipmap.test3};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_draw);
        final MultiCardsView multiCardView =  findViewById(R.id.multiCardView);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        SingleCard singleCard = null;
        int cardWidth = width /2;
        int cardHeight = height /2;
        int yOffset = 40;
        int xOffset = 40;
        for (int i = 0; i < cardResId.length; i++) {
            singleCard = new SingleCard(new RectF(xOffset, yOffset, xOffset + cardWidth, yOffset + cardHeight));
            Bitmap bitmap = loadImageResource(cardResId[i], cardWidth, cardHeight);
            singleCard.setBitmap(bitmap);
            multiCardView.addCards(singleCard);
            xOffset += cardWidth / 3;
        }


        findViewById(R.id.overDrawTrue).setOnClickListener(view -> {
            multiCardView.enableOverdrawOpt(true);
        });

        findViewById(R.id.overDrawFalse).setOnClickListener(view -> {
            multiCardView.enableOverdrawOpt(false);
        });
    }


    public Bitmap loadImageResource(int imageResId, int cardWidth, int cardHeight) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inDensity = DisplayMetrics.DENSITY_DEFAULT;
            options.inTargetDensity = DisplayMetrics.DENSITY_DEFAULT;
            options.inScreenDensity = DisplayMetrics.DENSITY_DEFAULT;
            BitmapFactory.decodeResource(getResources(), imageResId, options);

            // ����decode
            options.inJustDecodeBounds = false;
            options.inSampleSize = findBestSampleSize(options.outWidth, options.outHeight, cardWidth, cardHeight);
            bitmap = BitmapFactory.decodeResource(getResources(), imageResId, options);
        } catch (OutOfMemoryError exception) {

        }
        return bitmap;
    }

    /**
     * Returns the largest power-of-two divisor for use in downscaling a bitmap
     * that will not result in the scaling past the desired dimensions.
     *
     * @param actualWidth   Actual width of the bitmap
     * @param actualHeight  Actual height of the bitmap
     * @param desiredWidth  Desired width of the bitmap
     * @param desiredHeight Desired height of the bitmap
     */
    public static int findBestSampleSize(
            int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }
}
