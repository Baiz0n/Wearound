package com.nothing.timing.wearound.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ImageConverter {

    public static Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();

            return BitmapFactory.decodeStream(input);

        } catch (IOException e) {
            Log.e("Image Converter Says", "IOException caught => " + e);

            return null;
        }
    }

    static public String getByteFromBitmap(Bitmap image) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);


        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    static public Bitmap getBitmapFromByte(String base64Image) {

//        Bitmap bmp = Bitmap.createBitmap(3,3, Bitmap.Config.ARGB_8888);
//        ByteBuffer buffer = ByteBuffer.wrap(image);
//        //buffer.rewind();
//        bmp.copyPixelsFromBuffer(buffer);
//
//        return bmp;

        byte[] data = Base64.decode(base64Image, Base64.DEFAULT);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        return BitmapFactory.decodeByteArray(data, 0, data.length, opt);
    }
}
