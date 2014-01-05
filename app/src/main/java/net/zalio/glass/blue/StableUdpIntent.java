package net.zalio.glass.blue;

import android.content.Intent;
import android.util.Log;

import com.androidzeitgeist.ani.discovery.Discovery;
import com.androidzeitgeist.ani.discovery.DiscoveryException;
import com.androidzeitgeist.ani.discovery.DiscoveryListener;
import com.androidzeitgeist.ani.transmitter.Transmitter;
import com.androidzeitgeist.ani.transmitter.TransmitterException;

import java.net.InetAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Henry on 1/4/14.
 */
public class StableUdpIntent implements DiscoveryListener {
    private static final String TAG = "sui";
    private static String INTERNAL_ID = "_id";
    private static String INTERNAL_ACK = "_ack";
    private static String INTERNAL_IP = "_ip";
    private final Discovery mDiscovery;
    private final Transmitter mTransmitter;
    private StableUdpIntentListener mListener;
    private long mCurrentId = -1;
    private ArrayBlockingQueue<Long> mQueue;
    private volatile boolean isSending = false;

    public StableUdpIntent(Discovery d, Transmitter t) {
        mDiscovery = d;
        mTransmitter = t;
    }

    synchronized public void send(Intent intent) {
        Log.i(TAG, "send start...");
        isSending = true;
        mCurrentId = System.currentTimeMillis();
        intent.putExtra(INTERNAL_ID, mCurrentId);
        mQueue = new ArrayBlockingQueue<Long>(1);
        mDiscovery.setDisoveryListener(this);
        try {
            mTransmitter.transmit(intent);
        } catch (TransmitterException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            mDiscovery.enable();
        } catch (DiscoveryException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                Long retId = mQueue.poll(500, TimeUnit.MILLISECONDS);
                if (retId == null) {
                    mTransmitter.transmit(intent);
                    Log.i(TAG, "Sent again...");
                } else {
                    Log.i(TAG, "Got ACK!, breaking");
                    mCurrentId = -1;
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TransmitterException e) {
                e.printStackTrace();
            }
        }
        mDiscovery.disable();
        isSending = false;
        Log.i(TAG, "send end...");
    }

    public void startListening(StableUdpIntentListener listener){
        mListener = listener;
        mDiscovery.setDisoveryListener(this);
        try {
            mDiscovery.enable();
        } catch (DiscoveryException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDiscoveryStarted() {

    }

    @Override
    public void onDiscoveryStopped() {

    }

    @Override
    public void onDiscoveryError(Exception exception) {

    }

    long previousId = -1;

    @Override
    public void onIntentDiscovered(InetAddress address, Intent intent) {
        long id = intent.getLongExtra(INTERNAL_ID, 0);
        boolean ack = intent.getBooleanExtra(INTERNAL_ACK, false);
        if (address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isLinkLocalAddress()) {
            Log.i(TAG, "local address");
            return;
        }
        if (isSending && ack && (id == mCurrentId)) {
            if (mQueue != null) {
                mQueue.offer(id);
            }
        } else if (!isSending && !ack) {
            intent.putExtra(INTERNAL_ACK, true);
            try {
                mTransmitter.transmit(intent);
                Log.i(TAG, "ACK Sent!");
            } catch (TransmitterException e) {
                e.printStackTrace();
            }
            if (id != previousId && mListener != null ) {
                previousId = id;
                Log.i(TAG, "Calling onDiscovered!");
                mListener.onDiscovered(address, intent);
            }
        } else {
            Log.i(TAG, "intent omitted");
        }
    }

    private void isLocalAddress(InetAddress addr) {

    }

    public static interface StableUdpIntentListener {
        void onDiscovered(InetAddress address, Intent i);
    }
}

