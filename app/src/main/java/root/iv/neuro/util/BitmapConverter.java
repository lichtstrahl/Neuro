package root.iv.neuro.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import root.iv.neuronet.Number;

public class BitmapConverter {
    public static Number createNumber(Bitmap bitmap, int value) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pxs = new int[w*h];
        bitmap.getPixels(pxs, 0, w, 0, 0, w, h);


        int[] result = new int[pxs.length];
        for (int i = 0; i < pxs.length; i++) {
            result[i] = (Color.red(pxs[i]) == 0) ? 1 : 0;   // Если черная, то 1 и инчае 0
        }
        return new Number(result, value);
    }
}
