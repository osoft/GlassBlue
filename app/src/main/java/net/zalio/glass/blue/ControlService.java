package net.zalio.glass.blue;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.androidzeitgeist.ani.transmitter.Transmitter;
import com.androidzeitgeist.ani.transmitter.TransmitterException;

import java.util.ArrayList;

/**
 * Created by Henry on 1/4/14.
 */
public class ControlService extends Service {
    private static final String TAG = "BlueGlass";
    public static String EXTRA_BUNDLE = "com.twofortyfouram.locale.intent.extra.BUNDLE";
    public static String KEY_SWITCH = "net.zalio.android.easyblue.switch";
    public static String KEY_BRIGHTNESS = "net.zalio.android.easyblue.brightness";
    private static final String EXTRA_MESSAGE = "message";
    private Transmitter mTransmitter;

    private String[] WordGroup_On = {
            "on",
            "own",
            "um",
            "un",
    };

    private String[] WordGroup_Off;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return START_NOT_STICKY;
    }

    protected void onHandleIntent(Intent intent) {
        Toast.makeText(this, "onHandleIntent", Toast.LENGTH_SHORT).show();
        ArrayList<String> voiceResults = intent.getExtras()
            .getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
        if (voiceResults == null || voiceResults.size() == 0) {
            return;
        }

        mTransmitter = new Transmitter();

        String input = voiceResults.get(0);
        Intent i = new Intent();

        Toast.makeText(this, input, Toast.LENGTH_LONG).show();

        if (input.toLowerCase().equals("turn on") || input.toLowerCase().equals("on")) {
            i.putExtra(KEY_SWITCH, true);
            i.putExtra(KEY_BRIGHTNESS, 100);
        } else if (input.toLowerCase().equals("turn off") || input.toLowerCase().equals("off")
                || input.toLowerCase().equals("off")) {
            i.putExtra(KEY_SWITCH, false);
            i.putExtra(KEY_BRIGHTNESS, 0);
        }

        i.putExtra(EXTRA_MESSAGE, "" + System.currentTimeMillis() + " " + i.toUri(0));
        transmitIntent(i);
    }


    private void transmitIntent(final Intent i) {
        new Thread(){
            @Override
            public void run() {
                try {
                    mTransmitter.transmit(i);
                } catch (TransmitterException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
