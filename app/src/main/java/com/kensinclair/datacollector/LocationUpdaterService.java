/*
 * Copyright (c) 2015 Ken Sinclair
 *
 * This work is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International License. To view
 * a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kensinclair.datacollector;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Monitors for location changes.
 *
 * @see com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
 * @see android.content.ComponentCallbacks
 * @see com.google.android.gms.location.LocationListener
 * @see com.kensinclair.datacollector.UpdaterService
 *
 * @author Ken Sinclair
 */

public class LocationUpdaterService extends    UpdaterService
                                    implements GoogleApiClient.ConnectionCallbacks,
                                               GoogleApiClient.OnConnectionFailedListener,
                                               LocationListener {
    private static final boolean LOCAL_LOGD = true;
    private static final boolean LOCAL_LOGV = false;
    private static final String  TAG        = LocationUpdaterService.class.getSimpleName();

    private static final long LOCATION_UPDATE_INTERVAL = 180000; // 3 minutes.
    private DbUpdater         mDbUpdater;
    private GoogleApiClient   mLocationApiClient;
    private LocationRequest   mLocationRequest;

    /**
     * Creates and connects location updater objects.
     *
     * @param intent  provided by system. Unused
     * @param flags   provided by system. Unused
     * @param startId provided by system. Unused
     * @return        <code>START_STICKY</code>
     */
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        sLg.setLevel(Log.VERBOSE);
        if(LOCAL_LOGV) sLg.log("In onStartCommand.");

        mDbUpdater       = new DbUpdater(getBaseContext());
        mLocationRequest = new LocationRequest();

        if(LOCAL_LOGV) sLg.log("Building LocationServices GoogleApiClient.");
        mLocationApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        if(LOCAL_LOGV) sLg.log("LocationServices GoogleApiClient built.");

        if(LOCAL_LOGV) sLg.log("Setting LocationRequest params.");
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        if(LOCAL_LOGV) sLg.log("LocationRequest params set.");

        sLg.setLevel(Log.DEBUG);
        if(LOCAL_LOGD) sLg.log("API Client connecting.");
        mLocationApiClient.connect();
        if(LOCAL_LOGD) sLg.log("API Client connected.");

        sLg.setLevel(Log.VERBOSE);
        if(LOCAL_LOGV) sLg.log("onStartCommand returning START_STICKY = " + START_STICKY +".");

        return START_STICKY;
    }

    /**
     * Calls method to update the database.
     *
     * @param location provided by system
     */
    @Override public void onLocationChanged(Location location) {
        long time = System.currentTimeMillis();

        mDbUpdater.updateTable(location, time);
    }

    /**
     * Begins requesting location updates.
     *
     * @param bundle provided by system. Unused
     */
    @Override public void onConnected(Bundle bundle) {
        sLg.setLevel(Log.VERBOSE);
        if(LOCAL_LOGV) sLg.log("In onConnected.");

        if(LOCAL_LOGV) sLg.log("Requesting location updates.");
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationApiClient,
                mLocationRequest, this);

        if(LOCAL_LOGV) sLg.log("Leaving onConnected.");
    }

    /**
     * Logs suspended connection.
     *
     * @param i provided by system
     */
    @Override public void onConnectionSuspended(int i) {
        sLg.setLevel(Log.VERBOSE);
        if(LOCAL_LOGV) sLg.log("Connection Suspended.");

    }

    /**
     * Logs an error when connection fails.
     * @param connectionResult provided by system. Unused
     */
    @Override public void onConnectionFailed(ConnectionResult connectionResult) {
        sLg.setLevel(Log.ERROR);
        sLg.log("Connection Failed.");
    }

    /**
     * Disconnects the <code>GoogleApiClient</code>
     * and unregisters event listener objects and callbacks.
     */
    @Override public void onDestroy() {
        super.onDestroy();
        mLocationApiClient.disconnect();
        mLocationApiClient.unregisterConnectionCallbacks(this);
        mLocationApiClient.unregisterConnectionFailedListener(this);
    }

    /**
     * Sets logger tag.
     */
    @Override public void onCreate() {
        super.onCreate();
        sLg.setTag(TAG);
    }
}
