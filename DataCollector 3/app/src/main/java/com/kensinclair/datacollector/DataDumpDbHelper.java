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

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kensinclair.logger.MyLog;

/**
 * Performs database operations.
 *
 * @author Ken Sinclair
 */

public class DataDumpDbHelper extends SQLiteOpenHelper {
    private static final boolean LOCAL_LOGD = true;
    private static final String  TAG = DataDumpDbHelper.class.getSimpleName();

    static final String     DATABASE_NAME    = "DataHarvest.db";
    public static final int DATABASE_VERSION = 3;

    private static final String BOOLEAN_TYPE = " INTEGER";
    private static final String CT           = "CREATE TABLE ";
    private static final String DATE_TYPE    = " TEXT";
    private static final String DOUBLE_TYPE  = " REAL";
    private static final String FLOAT_TYPE   = " REAL";
    private static final String INT_TYPE     = " INTEGER";
    private static final String PK           = " INTEGER PRIMARY KEY AUTOINCREMENT ";
    private static final String STRING_TYPE  = " TEXT";
    private static final String SEP          = ",";

    private static final String SQL_CREATE_ACCOUNTS =
            CT + DataDumpContract.AccountsTable.TABLE_NAME + " (" +
                    DataDumpContract.AccountsTable._ID              + PK          + SEP +
                    DataDumpContract.AccountsTable.COLUMN_NAME_TIME + DATE_TYPE   + SEP +
                    DataDumpContract.AccountsTable.COLUMN_NAME_NAME + STRING_TYPE + SEP +
                    DataDumpContract.AccountsTable.COLUMN_NAME_TYPE + STRING_TYPE +
            " )";
    private static final String SQL_CREATE_LOC =
            CT + DataDumpContract.LocationTable.TABLE_NAME + " (" +
                    DataDumpContract.LocationTable._ID                 + PK          + SEP +
                    DataDumpContract.LocationTable.COLUMN_NAME_TIME    + DATE_TYPE   + SEP +
                    DataDumpContract.LocationTable.COLUMN_NAME_LAT     + DOUBLE_TYPE + SEP +
                    DataDumpContract.LocationTable.COLUMN_NAME_LNG     + DOUBLE_TYPE + SEP +
                    DataDumpContract.LocationTable.COLUMN_NAME_BEARING + FLOAT_TYPE  + SEP +
                    DataDumpContract.LocationTable.COLUMN_NAME_SPEED   + FLOAT_TYPE  + SEP +
                    DataDumpContract.LocationTable.COLUMN_NAME_ALT     + DOUBLE_TYPE + SEP +
                    DataDumpContract.LocationTable.COLUMN_NAME_ACC     + FLOAT_TYPE  +
            " )";
    private static final String SQL_CREATE_NET =
            CT + DataDumpContract.NetworkTable.TABLE_NAME + " (" +
                    DataDumpContract.NetworkTable._ID                 + PK          + SEP +
                    DataDumpContract.NetworkTable.COLUMN_NAME_TIME    + DATE_TYPE   + SEP +
                    DataDumpContract.NetworkTable.COLUMN_NAME_TYPE    + STRING_TYPE + SEP +
                    DataDumpContract.NetworkTable.COLUMN_NAME_SUBTYPE + STRING_TYPE + SEP +
                    DataDumpContract.NetworkTable.COLUMN_NAME_STATE   + STRING_TYPE + SEP +
                    DataDumpContract.NetworkTable.COLUMN_NAME_REASON  + STRING_TYPE +
            " )";
    private static final String SQL_CREATE_WIFI_CON =
            CT + DataDumpContract.WifiConnectionTable.TABLE_NAME + " (" +
                    DataDumpContract.WifiConnectionTable._ID               + PK           + SEP +
                    DataDumpContract.WifiConnectionTable.COLUMN_NAME_TIME  + DATE_TYPE    + SEP +
                    DataDumpContract.WifiConnectionTable.COLUMN_NAME_IP    + INT_TYPE     + SEP +
                    DataDumpContract.WifiConnectionTable.COLUMN_NAME_MAC   + STRING_TYPE  + SEP +
                    DataDumpContract.WifiConnectionTable.COLUMN_NAME_BSSID + STRING_TYPE  + SEP +
                    DataDumpContract.WifiConnectionTable.COLUMN_NAME_SSID  + STRING_TYPE  + SEP +
                    DataDumpContract.WifiConnectionTable.COLUMN_NAME_HID   + BOOLEAN_TYPE +

            " )";
    private static final String SQL_CREATE_WIFI_SCAN =
            CT + DataDumpContract.WifiScanTable.TABLE_NAME + " (" +
                    DataDumpContract.WifiScanTable._ID               + PK          + SEP +
                    DataDumpContract.WifiScanTable.COLUMN_NAME_TIME  + DATE_TYPE   + SEP +
                    DataDumpContract.WifiScanTable.COLUMN_NAME_BSSID + STRING_TYPE + SEP +
                    DataDumpContract.WifiScanTable.COLUMN_NAME_SSID  + STRING_TYPE + SEP +
                    DataDumpContract.WifiScanTable.COLUMN_NAME_CAP   + STRING_TYPE +
            " )";

    /**
     * Creates a <code>SQLiteOpenHelper</code> with
     * write-ahead logging enabled.
     *
     * @param context provided by system
     */
    DataDumpDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setWriteAheadLoggingEnabled(true);

    }

    /**
     * Creates the tables.
     *
     * @param db provided by system
     */
    @Override public void onCreate(SQLiteDatabase db) {
        MyLog lg = new MyLog(TAG);
        if(LOCAL_LOGD) lg.log("Creating database");

        lg.setLevel(Log.ERROR);

        try {
            db.execSQL(SQL_CREATE_ACCOUNTS);
        } catch (SQLException e) {
            lg.log(e.getMessage() + " when creating table "
                    + DataDumpContract.AccountsTable.TABLE_NAME);
        }
        try {
            db.execSQL(SQL_CREATE_LOC);
        } catch (SQLException e) {
            lg.log(e.getMessage() + " when creating table "
                    + DataDumpContract.LocationTable.TABLE_NAME);
        }
        try {
            db.execSQL(SQL_CREATE_NET);
        } catch (SQLException e) {
            lg.log(e.getMessage() + " when creating table "
                    + DataDumpContract.NetworkTable.TABLE_NAME);
        }
        try {
            db.execSQL(SQL_CREATE_WIFI_CON);
        } catch (SQLException e) {
            lg.log(e.getMessage() + " when creating table "
                    + DataDumpContract.WifiConnectionTable.TABLE_NAME);
        }
        try {
            db.execSQL(SQL_CREATE_WIFI_SCAN);
        } catch (SQLException e) {
            lg.log(e.getMessage() + " when creating table "
                    + DataDumpContract.WifiScanTable.TABLE_NAME);
        }
    }

    /**
     * Not concerned about this for this experiment.
     * Do nothing.
     *
     * @param db         provided by system. Unused
     * @param oldVersion provided by system. Unused
     * @param newVersion provided by system. Unused
     */
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
