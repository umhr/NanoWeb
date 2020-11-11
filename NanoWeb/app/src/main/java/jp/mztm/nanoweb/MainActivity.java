package jp.mztm.nanoweb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static String hostname = "127.0.0.1";
    public static int port = 8080;

    private NanoWebView nanoWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveFile();
        setup();
    }

    private void setup() {
        hostname = new IPAddress().getIPAddress();

        nanoWebView = new NanoWebView(findViewById(R.id.webView));
        NanoWebServer.Callback callback = new NanoWebServer.Callback() {
            @Override
            public void loadUrl(String url) {
                //System.out.println("スレッドでの処理結果は" + url + "です");
                nanoWebView.webView.post(new Runnable() {
                    @Override
                    public void run() {
                        nanoWebView.webView.loadUrl(url);
                    }
                });
            }
        };

        NanoWebServer nanoWebServer = new NanoWebServer(getApplicationContext());
        nanoWebServer.setCallback(callback);

        try {
            nanoWebServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            nanoWebView.setKeyCode(keyCode);
        }
        return true;
    }

    private void saveFile(){
        File path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS + "/html");
        //path = getExternalFilesDir("/storage/emulated/0/html");
        String filename ="date.txt";
        File file = new File(path, filename);
        write(file,new Date().toString());
    }

    private void write(File file, String str){
        try(FileOutputStream fileOutputStream =
                    new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            BufferedWriter bw =
                    new BufferedWriter(outputStreamWriter);) {
            bw.write(str);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}