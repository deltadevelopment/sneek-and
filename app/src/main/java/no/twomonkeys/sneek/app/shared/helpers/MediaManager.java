package no.twomonkeys.sneek.app.shared.helpers;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import no.twomonkeys.sneek.app.shared.SimpleCallback2;

/**
 * Created by simenlie on 20.10.2016.
 */

public class MediaManager {

    private static int totalRead;
    private boolean started;


    public static void downloadVideoAsync(final String urlStr, final File file, final SimpleCallback2 scb) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadUsingStream(urlStr, file, scb);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
    }

    public static void downloadUsingStream(String urlStr, File file, SimpleCallback2 scb) throws IOException {


        URL url = new URL(urlStr);
        Log.v("STart", "is Starting");
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = bis.read(buffer, 0, 1024)) != -1) {
            Log.v("Reading buffer", "Buffering");
            totalRead += count;
            fis.write(buffer, 0, count);
        }

        fis.close();
        bis.close();
        totalRead = 0;
        scb.callbackCall();
    }

}
