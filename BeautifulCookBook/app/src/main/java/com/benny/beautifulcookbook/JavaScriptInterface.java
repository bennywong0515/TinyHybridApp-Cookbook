package com.benny.beautifulcookbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringJoiner;
public class JavaScriptInterface {
    private Activity activity;
    public JavaScriptInterface(Activity activiy) {
        this.activity = activiy;
    }
    @JavascriptInterface
    public void showToast(String showtext){
        Toast.makeText(activity, showtext,
                Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
    public void writeFile(String data, String filename) {
        Context context =
                this.activity.getApplication().getApplicationContext();
        try {
            OutputStreamWriter outputStreamWriter = new
                    OutputStreamWriter(context.openFileOutput(filename,
                    Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    @JavascriptInterface
    public String readFile(String filename) {
        Context context =
                this.activity.getApplication().getApplicationContext();
        String ret = "";
        try {
            InputStream inputStream =
                    context.openFileInput(filename);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new
                        InputStreamReader(inputStream);
                BufferedReader bufferedReader = new
                        BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ( (receiveString = bufferedReader.readLine())
                        != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " +
                    e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " +
                    e.toString());
        }
        return ret;
    }
    @JavascriptInterface
    public boolean delFile(String filename) {
        Context context =
                this.activity.getApplication().getApplicationContext();
        File dir = context.getFilesDir();
        File file = new File(dir, filename);
        boolean deleted = file.delete();
        return deleted;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @JavascriptInterface
    public String listFile() {
        Context context =
                this.activity.getApplication().getApplicationContext();
        String path = context.getFilesDir().toString();
        File directory = new File(path);
        File[] files = directory.listFiles();
        String[] names = new String[files.length];
        StringJoiner joiner = new StringJoiner("/");
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getName();
            joiner.add(names[i]);
        }
        return joiner.toString();
    }
    @JavascriptInterface
    public void openURL(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        this.activity.startActivity(browserIntent);
    }
}