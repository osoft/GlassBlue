/*
 * Copyright (C) 2013 Sebastian Kaspari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androidzeitgeist.ani.discovery;

import java.net.InetAddress;

import android.content.Intent;

/**
 * A {@link com.androidzeitgeist.ani.discovery.DiscoveryListener} receives notifications from a {@link com.androidzeitgeist.ani.discovery.Discovery}.
 * Notifications indicate lifecycle related events as well as successfully received
 * {@link android.content.Intent}s.
 *
 * @author Sebastian Kaspari <s.kaspari@gmail.com>
 */
public interface DiscoveryListener {
    /**
     * The {@link com.androidzeitgeist.ani.discovery.Discovery} has been started and is now waiting for incoming
     * {@link android.content.Intent}s.
     */
    public void onDiscoveryStarted();

    /**
     * The {@link com.androidzeitgeist.ani.discovery.Discovery} has been stopped.
     */
    public void onDiscoveryStopped();

    /**
     * An unrecoverable error occured. The {@link com.androidzeitgeist.ani.discovery.Discovery} is going to be stopped.
     *
     * @param exception Actual exception that occured in the background thread
     */
    public void onDiscoveryError(Exception exception);

    /**
     * Called when the {@link com.androidzeitgeist.ani.discovery.Discovery} has successfully received an {@link android.content.Intent}.
     *
     * @param address The IP address of the sender of the {@link android.content.Intent}.
     * @param intent The received {@link android.content.Intent}.
     */
    public void onIntentDiscovered(InetAddress address, Intent intent);
}
