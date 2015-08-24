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

import android.provider.BaseColumns;

/**
 * Provides a dictionary for the DataDump database.
 *
 * @author Ken Sinclair
 */

final class DataDumpContract {
    /** Prevents an instance from being accidentally instantiated. */
    private DataDumpContract() {}

    /**
     * Accounts table.
     */

    public static abstract class AccountsTable implements BaseColumns {
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String TABLE_NAME       = "accounts";
    }

    /**
     * Location table.
     */

    public static abstract class LocationTable implements BaseColumns {
        public static final String COLUMN_NAME_ACC     = "accuracy";
        public static final String COLUMN_NAME_ALT     = "altitude";
        public static final String COLUMN_NAME_BEARING = "bearing";
        public static final String COLUMN_NAME_LAT     = "latitude";
        public static final String COLUMN_NAME_LNG     = "longitude";
        public static final String COLUMN_NAME_SPEED   = "speed";
        public static final String COLUMN_NAME_TIME    = "time";
        public static final String TABLE_NAME          = "location";
    }

    /**
     * Network table.
     */

    public static abstract class NetworkTable implements BaseColumns {
        public static final String COLUMN_NAME_REASON  = "reason";
        public static final String COLUMN_NAME_STATE   = "state";
        public static final String COLUMN_NAME_SUBTYPE = "subtype";
        public static final String COLUMN_NAME_TIME    = "time";
        public static final String COLUMN_NAME_TYPE    = "type";
        public static final String TABLE_NAME          = "network";
    }

    /**
     * WiFi Connection table.
     */

    public static abstract class WifiConnectionTable implements BaseColumns {
        public static final String COLUMN_NAME_BSSID = "bssid";
        public static final String COLUMN_NAME_HID   = "hidden";
        public static final String COLUMN_NAME_IP    = "ip_address";
        public static final String COLUMN_NAME_MAC   = "mac_address";
        public static final String COLUMN_NAME_SSID  = "ssid";
        public static final String COLUMN_NAME_TIME  = "time";
        public static final String TABLE_NAME        = "wifi_connection";
    }

    /**
     * WiFi Scan table.
     */

    public static abstract class WifiScanTable implements BaseColumns {
        public static final String COLUMN_NAME_BSSID = "bssid";
        public static final String COLUMN_NAME_SSID  = "ssid";
        public static final String COLUMN_NAME_CAP   = "capabilities";
        public static final String COLUMN_NAME_TIME  = "time";
        public static final String TABLE_NAME        = "wifi_scan";
    }
}
