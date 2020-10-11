package com.example.unamedappproject;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Compressor {

    private static final String TAG = "Compresser";
    private  String filepath = "aadl_1/compressed";
    public static  String compressedfile;

    public boolean compressfile(File inp_file, Context context){
        Log.i(TAG, "compressfile: original size:" + Integer.parseInt(String.valueOf(inp_file.length()/1024)));
        boolean anyErrors = false;
        try {
            String cmpfilename = createNewName(inp_file.getName().toString());
            //input filestream
            FileInputStream imf = new FileInputStream(inp_file);
            //output file
            File cmpop = new File(context.getExternalFilesDir(filepath), cmpfilename);
            //output filestream
            FileOutputStream cmf = new FileOutputStream(cmpop);
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            BitmapFactory.decodeStream(imf, null, o);
            imf.close();
            // The new size we want to scale to
            final int REQUIRED_SIZE=200;        // x............
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            imf = new FileInputStream(inp_file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(imf, null, o2);
            imf.close();
            selectedBitmap.compress(Bitmap.CompressFormat.WEBP, 95 , cmf);
            cmf.close();
            compressedfile = cmpop.getAbsolutePath();
            Log.i(TAG, "compressfile: cmp size:" + Integer.parseInt(String.valueOf(cmpop.length()/1024)));

        } catch (FileNotFoundException e) {
            anyErrors = true;
            e.printStackTrace();
        } catch (IOException e) {
            anyErrors = true;
            e.printStackTrace();
        } finally {
            return anyErrors;
        }
    }

    private String createNewName(String name){
        int dot = name.indexOf(".");
        String n = name.substring(0,dot).concat("cmp.webp");
        return n;
    }
}

