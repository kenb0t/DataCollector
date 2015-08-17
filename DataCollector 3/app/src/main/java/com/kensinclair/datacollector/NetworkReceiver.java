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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.kensinclair.logger.MyLog;

/**
 * Receives network change broadcasts.
 *
 * @author Ken Sinclair
 */

public class NetworkReceiver extends BroadcastReceiver {
    private static final boolean LOCAL_LOGD = true;
    private static final String  TAG        = NetworkReceiver.class.getSimpleName();

    private static MyLog sLg = new MyLog(TAG, Log.DEBUG);
    /**
     * Calls the appropriate database updater method
     * on receipt of a network change broadcast.
     *
     * @param context provided by system
     * @param intent  provided by system
     */
    @Override public void onReceive(Context context, Intent intent) {
        long time = System.currentTimeMillis();

        DbUpdater dbUpdater= new DbUpdater(context);

        String intentAction = intent.getAction();

        if(LOCAL_LOGD) sLg.log("Received " + intentAction);
                                                         //  If the Intent is a CONNECTIVITY_ACTION,
        if(intentAction.compareTo(ConnectivityManager.CONNECTIVITY_ACTION) == 0) { //   it came from
            ConnectivityManager conManager = (ConnectivityManager)          //    the cell radio,and
                    context.getSystemService(Context.CONNECTIVITY_SERVICE); //  its data is accessed
                                                                            //           through the
            dbUpdater.updateTable(conManager.getActiveNetworkInfo(), time); //  ConnectivityManager.

        } else {                                         // Otherwise, the Intent came from the WiFi
                onReceiveWifiAction((WifiManager)        //   radio and its data is accessed through
                        context.getSystemService(        //                        the WiFi Manager.
                                Context.WIFI_SERVICE),intentAction, time, dbUpdater);
        }
    }

    /**
     * Determines what kind of WiFi action has
     * been broadcast and calls the appropriate
     * database updater method.
     *
     * @param wifiManager  the <code>WifiManager</code> provided by <code>Context</code>.
     *                     Passed from the caller to save overhead of an
     *                     extra function call
     * @param intentAction what action triggered the receiver
     * @param time         time the intent was received
     * @param dbUpdater    the database updater
     * @return             <code>true</code> if the database was successfully updated;
     *                     <code>false</code> otherwise
     */
    protected boolean onReceiveWifiAction(WifiManager wifiManager,
                                        String      intentAction,
                                        long        time,
                                        DbUpdater   dbUpdater) {
        if(LOCAL_LOGD) sLg.log("Getting Wifi info");

        switch (intentAction) {
            case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:       //     The Intent was a Wifi scan.
                return dbUpdater.updateTable(wifiManager.getScanResults(), time);

            case WifiManager.NETWORK_STATE_CHANGED_ACTION:       //  The Intent was a change of WiFi
                return                                           //                         network.
                        dbUpdater.updateTable(wifiManager.getConnectionInfo(), time) != -1;

            default: return false;                              // The intent was an unexpected WiFi
        }                                                       //                           action.
    }
}
