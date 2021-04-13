package com.mentit.dartify.HelperClasses;

public class CircleCropTransformation  {

    private int strokeColor ;

    public CircleCropTransformation(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    /*@Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        result = getCircledCrop(result, this.strokeColor);

        if (result != source) source.recycle();

        return result;
    }

    private static Bitmap getCircledCrop(Bitmap bitmap, int strokeColor) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        //PINTAR BORDE BLANCO
        paint.setColor(strokeColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, 100, paint);

        return output;
    }

    @Override
    public String key() {
        return "circle()";
    }*/
}
