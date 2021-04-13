package com.mentit.dartify.HelperClasses;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class WhiteBorderTransformation extends BitmapTransformation {

    private static final String ID = "WhiteBorderTransformation";
    private static final byte[] ID_BYTES = ID.getBytes(Charset.forName("UTF-8"));

    int color;

    public WhiteBorderTransformation(int col) {
        super();
        color = col;
    }

    @Override
    public Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Paint paint = new Paint();
        Bitmap output = Bitmap.createScaledBitmap(toTransform, outWidth, outHeight, /*filter=*/ true);
        if (toTransform.getWidth() >= toTransform.getHeight()) {
            output = Bitmap.createBitmap(toTransform, toTransform.getWidth() / 2 - toTransform.getHeight() / 2, 0, toTransform.getHeight(), toTransform.getHeight());
        } else {
            output = Bitmap.createBitmap(toTransform, 0, toTransform.getHeight() / 2 - toTransform.getWidth() / 2, toTransform.getWidth(), toTransform.getWidth());
        }

        Canvas canvas = new Canvas(output);

        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        canvas.drawCircle(output.getWidth() / 2, output.getHeight() / 2, output.getHeight() / 2, paint);

        return output;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof WhiteBorderTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
