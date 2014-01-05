package net.zalio.glass.blue;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.androidzeitgeist.ani.discovery.Discovery;
import com.androidzeitgeist.ani.transmitter.Transmitter;
import com.androidzeitgeist.ani.transmitter.TransmitterException;

/**
 * Created by Henry on 1/4/14.
 */
public class ControlServiceOff extends Service {
    private static final String TAG = "BlueGlass";
    public static String KEY_SWITCH = "net.zalio.android.easyblue.switch";
    public static String KEY_BRIGHTNESS = "net.zalio.android.easyblue.brightness";
    private static final String EXTRA_MESSAGE = "message";
    private Transmitter mTransmitter;

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
        mTransmitter = new Transmitter();

        Intent i = new Intent();

        Toast.makeText(this, R.string.glass_voice_trigger_turn_off, Toast.LENGTH_LONG).show();
        i.putExtra(KEY_SWITCH, false);
        i.putExtra(KEY_BRIGHTNESS, 0);
        i.putExtra(EXTRA_MESSAGE, "" + System.currentTimeMillis() + " " + i.toUri(0));
        transmitIntent(i);
    }


    private void transmitIntent(final Intent i) {
        new Thread(){
            @Override
            public void run() {
                //try {
                //    mTransmitter.transmit(i);
                //} catch (TransmitterException e) {
                //    e.printStackTrace();
                //}
                StableUdpIntent sui = new StableUdpIntent(new Discovery(), mTransmitter);
                sui.send(i);
            }
        }.start();
    }
}
