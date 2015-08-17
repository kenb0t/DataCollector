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

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.text.format.DateFormat;

import com.kensinclair.logger.MyLog;

import java.util.Iterator;
import java.util.List;

/**
 * Provides methods for updating the database.
 *
 * @author Ken Sinclair
 */

public class DbUpdater {
    private static final boolean LOCAL_LOGV = false;
    private static final String  TAG        = DbUpdater.class.getSimpleName();

    private static MyLog sLg = new MyLog(TAG);

    private static final CharSequence DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S z";
    private Context                   mContext;

    /**
     * Prevents the default constructor from being called.
     */
    private DbUpdater() {}

    /**
     * Constructs a <code>DbUpdater</code> with
     * a <code>Context</code> for a <code>DataDumpDbHelper</code>.
     *
     * @param c the context of the caller
     */
    public DbUpdater(Context c) { mContext = c; }

    /**
     * Updates the accounts table.
     *
     * @param accounts the new accounts data
     * @param time     the ime of data collection
     * @return         <code>true</code> if the database update
     *                 was successful; <code>false</code> otherwise
     */
    boolean updateTable(Account[] accounts, long time) {
        boolean       retVal    = true;
        ContentValues row;
        CharSequence  timestamp = createTimestamp(time);

        SQLiteDatabase db = getDb();

        for(Account account:accounts) {
            row = createRow(account, timestamp);

            if(LOCAL_LOGV) sLg.log("Inserting row: " + row);

            if(db.insert(DataDumpContract.AccountsTable.TABLE_NAME, null, row) == -1)
                retVal = false;
        }
        return retVal;
    }

    /**
     * Updates the location table.
     *
     * @param location the new location data
     * @param time     the time of data collection
     * @return         the row ID of the newly inserted row,
     *                 or -1 if an error occurred
     */
    long updateTable(Location location, long time) {
        CharSequence timestamp = createTimestamp(time);

        ContentValues row = createRow(location, timestamp);

        if(LOCAL_LOGV) sLg.log("Inserting row: " + row);

        return getDb().insert(DataDumpContract.LocationTable.TABLE_NAME, null, row);
    }

    /**
     * Updates the network table.
     *
     * @param netInfo the new network data
     * @param time    the time of data collection
     * @return        the row ID of the newly inserted row,
     *                or -1 if an error occurred
     */
    long updateTable(NetworkInfo netInfo, long time) {
        CharSequence  timestamp = createTimestamp(time);

        ContentValues row = createRow(netInfo, timestamp);

        if(LOCAL_LOGV) sLg.log("Inserting row: " + row);

        return getDb().insert(DataDumpContract.NetworkTable.TABLE_NAME, null, row);
    }

    /**
     * Updates the WiFi table.
     *
     * @param wifiInfo the new WiFi data
     * @param time     the time of data collection
     * @return         the row ID of the newly inserted row,
     *                 or -1 if an error occurred
     */
    long updateTable(WifiInfo wifiInfo, long time) {
        CharSequence timestamp = createTimestamp(time);

        ContentValues row = createRow(wifiInfo, timestamp);

        if(LOCAL_LOGV) sLg.log("Inserting row: " + row);

        return getDb().insert(DataDumpContract.WifiConnectionTable.TABLE_NAME, null, row);
    }

    /**
     * Updates the scan results table.
     * Since each <code>ScanResult</code> has
     * its own timestamp, no time parameter is necessary.
     *
     * @param scanResults the new scan results
     * @return            <code>true</code> if the database update
     *                    was successful; <code>false</code> otherwise
     */
    boolean updateTable(List<ScanResult> scanResults, long time) {
        Iterator<ScanResult> it        = scanResults.iterator();
        boolean              retVal    = true;
        ContentValues        row;
        CharSequence         timestamp = createTimestamp(time);

         SQLiteDatabase db = getDb();

        while (it.hasNext()) {
            row = createRow(it.next(), timestamp);

            if(LOCAL_LOGV) sLg.log("Inserting row: " + row);

            if (db.insert(DataDumpContract.WifiScanTable.TABLE_NAME, null, row) == -1)
                retVal = false;
        }

        return retVal;
    }

    /**
     * Helper function to create a timestamp format
     * specified by <code>TIME_FORMAT</code>.
     *
     * @param time the time to be formatted
     * @return     the formatted time
     */
    protected CharSequence createTimestamp(long time) {
        return DateFormat.format(DATE_FORMAT, time);
    }

    /**
     * Creates a row for the accounts table.
     *
     * @param account   the <code>Account</code> from which the row is created
     * @param timestamp the time of data collection
     * @return          the row created from the <code>Account</code>
     */
    private ContentValues createRow(Account account, CharSequence timestamp) {
        ContentValues accountRow = new ContentValues();

        accountRow.put(DataDumpContract.AccountsTable.COLUMN_NAME_TIME, timestamp.toString());
        accountRow.put(DataDumpContract.AccountsTable.COLUMN_NAME_NAME, account.name);
        accountRow.put(DataDumpContract.AccountsTable.COLUMN_NAME_TYPE, account.type);

        return accountRow;
    }

    /**
     * Creates a row for the location table.
     *
     * @param location  the <code>Location</code> from which the row is created
     * @param timestamp the time of data collection
     * @return          the row created from the <code>Location</code>
     */
    private ContentValues createRow(Location location, CharSequence timestamp) {
        ContentValues locationRow = new ContentValues();

        locationRow.put(DataDumpContract.LocationTable.COLUMN_NAME_TIME,      timestamp.toString());
        locationRow.put(DataDumpContract.LocationTable.COLUMN_NAME_LAT,     location.getLatitude());
        locationRow.put(DataDumpContract.LocationTable.COLUMN_NAME_LNG,
                                                                           location.getLongitude());
        locationRow.put(DataDumpContract.LocationTable.COLUMN_NAME_BEARING,  location.getBearing());
        locationRow.put(DataDumpContract.LocationTable.COLUMN_NAME_SPEED,      location.getSpeed());
        locationRow.put(DataDumpContract.LocationTable.COLUMN_NAME_ALT,     location.getAltitude());
        locationRow.put(DataDumpContract.LocationTable.COLUMN_NAME_ACC,     location.getAccuracy());

        return locationRow;
    }

    /**
     * Creates a row for the network table.
     *
     * @param netInfo   the <code>NetworkInfo</code> from which the row is created
     * @param timestamp the time of data collection
     * @return          the row created from the <code>NetworkInfo</code>
     */
    private ContentValues createRow(NetworkInfo netInfo, CharSequence timestamp){
        ContentValues networkRow = new ContentValues();
        String reason            = "disconnection"; // If no reason is provided, assume
        String state             = "";              //                    disconnection.
        String subtype           = "";
        String type              = "";

        if(netInfo != null) {                      // If the reason for the signal is disconnection,
            type    =  netInfo.getTypeName();      //                       the NetworkInfo is null.
            subtype = netInfo.getSubtypeName();
            state   = netInfo.getDetailedState().toString();
            reason  = netInfo.getReason();
        }
        networkRow.put(DataDumpContract.NetworkTable.COLUMN_NAME_TIME,    timestamp.toString());
        networkRow.put(DataDumpContract.NetworkTable.COLUMN_NAME_TYPE,    type);
        networkRow.put(DataDumpContract.NetworkTable.COLUMN_NAME_SUBTYPE, subtype);
        networkRow.put(DataDumpContract.NetworkTable.COLUMN_NAME_STATE,   state);
        networkRow.put(DataDumpContract.NetworkTable.COLUMN_NAME_REASON,  reason);

        return networkRow;
    }

    /**
     * Creates a row for the WiFi connection table.
     *
     * @param wifiInfo  the <code>WifiInfo</code> from which the row is created
     * @param timestamp the time of data collection
     * @return          the row created from the <code>WifiInfo</code>
     */
    private ContentValues createRow(WifiInfo wifiInfo, CharSequence timestamp) {
        ContentValues wifiConRow = new ContentValues();

        wifiConRow.put(DataDumpContract.WifiConnectionTable.COLUMN_NAME_TIME,
                                                                              timestamp.toString());
        wifiConRow.put(DataDumpContract.WifiConnectionTable.COLUMN_NAME_IP,
                                                                           wifiInfo.getIpAddress());
        wifiConRow.put(DataDumpContract.WifiConnectionTable.COLUMN_NAME_MAC,
                                                                          wifiInfo.getMacAddress());
        wifiConRow.put(DataDumpContract.WifiConnectionTable.COLUMN_NAME_BSSID, wifiInfo.getBSSID());
        wifiConRow.put(DataDumpContract.WifiConnectionTable.COLUMN_NAME_SSID,   wifiInfo.getSSID());
        wifiConRow.put(DataDumpContract.WifiConnectionTable.COLUMN_NAME_HID,
                                                                          wifiInfo.getHiddenSSID());

        return wifiConRow;
    }

    /**
     * Creates a row for the WiFi scan results table.
     *
     * @param scanResult the <code>ScanResult</code> from which the row is created
     * @param timestamp the time of data collection
     * @return           the row created from the <code>ScanResults</code>
     */
    private ContentValues createRow(ScanResult scanResult, CharSequence timestamp) {
        ContentValues wifiScanRow = new ContentValues();

        wifiScanRow.put(DataDumpContract.WifiScanTable.COLUMN_NAME_TIME,  timestamp.toString());
        wifiScanRow.put(DataDumpContract.WifiScanTable.COLUMN_NAME_BSSID, scanResult.BSSID);
        wifiScanRow.put(DataDumpContract.WifiScanTable.COLUMN_NAME_SSID,  scanResult.SSID);
        wifiScanRow.put(DataDumpContract.WifiScanTable.COLUMN_NAME_CAP,   scanResult.capabilities);

        return  wifiScanRow;
    }

    /**
     * Gets a writable database.
     *
     * @return the writable database
     */
    private SQLiteDatabase getDb() {
        DataDumpDbHelper dbHelper = new DataDumpDbHelper(mContext);

        SQLiteDatabase db = dbHelper.getWritableDatabase();           //       Continue requesting a
                                                                      // writable database until the
        while(db.isReadOnly()) db = dbHelper.getWritableDatabase();   //  dbHelper is able to return
        return db;                                                    //                        one.
    }
}