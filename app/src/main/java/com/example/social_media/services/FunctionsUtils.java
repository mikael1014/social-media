package com.example.social_media.services;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FunctionsUtils {

    public static File getFileFromUri(Context context, Uri selectedImage) throws IOException {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};//"_data"

        InputStream inputStream = context.getContentResolver().openInputStream(selectedImage);
        Cursor cursor = context.getContentResolver().query(selectedImage, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String filename = cursor.getString(nameIndex);
        Log.i("filename", filename);
        File file = new File(context.getFilesDir().getPath() + "/" + filename);
        FileUtils.copyInputStreamToFile(inputStream, file);
        Log.i("file", file.getName());
        Log.i("file", file.length() + "");
        return file;

    }

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();

    }

    public static String capitalizeFullName(String s1, String s2) {
        return capitalize(s1) + " " + capitalize(s2);
    }
}
