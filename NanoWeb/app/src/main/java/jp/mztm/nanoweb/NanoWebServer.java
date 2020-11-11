package jp.mztm.nanoweb;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import fi.iki.elonen.NanoHTTPD;
/**
 * 参考
 * AndroidアプリでWebサーバー(Nanohttpd)を立ち上げる
 * https://qiita.com/mktshhr/items/9effdd4a8fa9616095d6
 *
 * Javaのスレッド(Thread)を使いこなすコツを、基礎からしっかり伝授
 * https://engineer-club.jp/java-thread
 */
public class NanoWebServer extends NanoHTTPD {
    private Context context;
    public NanoWebServer(Context context) {
        super(MainActivity.port);
        this.context = context;
        //start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://" + MainActivity.hostname + ":" + MainActivity.port + " \n");
    }

    private Callback callback = null;
    void setCallback(Callback callback) {
        this.callback = callback;
    }
    static interface Callback {
        void loadUrl(String url);
    }
    private String _href = "";

    @Override
    public Response serve(IHTTPSession session) {
        if(session.getQueryParameterString() != null){
            String query = session.getQueryParameterString();
            if(query.startsWith("href=")){
                String href = query.substring("href=".length());
                if(!href.startsWith("http")){
                    href = "http://" + MainActivity.hostname + ":" + MainActivity.port + "/" + href;
                }
                if(_href != href){
                    callback.loadUrl(href);
                    _href = href;
                    return newFixedLengthResponse(Response.Status.NO_CONTENT, MIME_PLAINTEXT, "");
                }
            }
        }

        return serveFile(session.getUri());
    }

    private Response serveFile(String uri){
        // System.out.println(uri);

        if(uri.equals("/location.js")){
            String txt = "var url = \"http://" + MainActivity.hostname + ":" + MainActivity.port + "\";";
            return newFixedLengthResponse(Response.Status.OK, "application/javascript", txt);
        }

        if ("/".equals(uri)) {
            uri = "index.html";
        }

        String filename = uri;
        if (uri.substring(0, 1).equals("/")) {
            filename = filename.substring(1);
        }

        AssetManager assetManager = context.getResources().getAssets();
        InputStream fis = null;
        try{
            fis = assetManager.open("html/" + filename);
        }catch (Exception e){
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS + "/html/"), filename);
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException fileNotFoundException) {
                //fileNotFoundException.printStackTrace();
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, fileNotFoundException.getLocalizedMessage());
            }
        }
        // System.out.println(getMimeTypeForFile(uri));
        return newChunkedResponse(Response.Status.OK, getMimeTypeForFile(uri), fis);
    }
}
