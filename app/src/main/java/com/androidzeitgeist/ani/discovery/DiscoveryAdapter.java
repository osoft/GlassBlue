package com.androidzeitgeist.ani.discovery;

import java.net.InetAddress;

import android.content.Intent;

/**
 * This adapter class provides empty implementations of the methods from
 * {@link DiscoveryListener}.
 *
 * Any custom listener that cares only about a subset of the methods of this listener
 * can simply subclass this adapter class instead of implementing the interface
 * directly.
 *
 * @author Sebastian Kaspari <s.kaspari@gmail.com>
 */
public abstract class DiscoveryAdapter implements DiscoveryListener {
    /**
     * Called when the {@link com.androidzeitgeist.ani.discovery.Discovery} has successfully received an {@link android.content.Intent}.
     *
     * @param address The IP address of the sender of the {@link android.content.Intent}.
     * @param intent The received {@link android.content.Intent}.
     */
    @Override
    public abstract void onIntentDiscovered(InetAddress address, Intent intent);

    /**
     * The {@link com.androidzeitgeist.ani.discovery.Discovery} has been started and is now waiting for incoming
     * {@link android.content.Intent}s.
     *
     * Empty default implementation.
     */
    @Override
    public void onDiscoveryStarted() {
        // Empty default implementation
    }

    /**
     * The {@link com.androidzeitgeist.ani.discovery.Discovery} has been stopped.
     *
     * Empty default implementation.
     */
    @Override
    public void onDiscoveryStopped() {
        // Empty default implementation
    }

    /**
     * An unrecoverable error occured. The {@link com.androidzeitgeist.ani.discovery.Discovery} is going to be stopped.
     *
     * Empty default implementation.
     *
     * @param exception Actual exception that occured in the background thread
     */
    @Override
    public void onDiscoveryError(Exception exception) {
        // Empty default implementation
    }
}
