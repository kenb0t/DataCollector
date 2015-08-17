/*
 * Copyright (c) 2015 Ken Sinclair
 *
 * This work is licensed under the Creative Commons
 * Attribution-ShareAlike 4.0 International License. To view a copy
 * of this license, visit
 *
 *       http://creativecommons.org/licenses/by-sa/4.0/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kensinclair.logger;

import android.util.Log;

/**
 * Provides stateful log object for easier logging.
 */

public class MyLog {
    private static boolean LOCAL_LOGD = true;
    private static String  TAG        = "MyLog";

    private static final String DEFAULT_TAG = "NO TAG";

    private int    mLevel;
    private String mTag;

    /**
     * Constructs default logger with the tag as "NO TAG"
     * and the log level at verbose.
     */
    public MyLog() {
        mLevel = Log.VERBOSE;
        mTag   = DEFAULT_TAG;
    }

    /**
     * Constructs a logger with a given tag
     * and the log level at verbose.
     *
     * @param t tag for the logger
     */
    public MyLog(String t) {
        mLevel = Log.VERBOSE;
        mTag   = t;
    }

    /**
     * Constructs a logger wit a given tag and
     * a given log level.
     *
     * @param t tag for the logger
     * @param l log level
     */
    public MyLog(String t, int l){
        mTag = t;
        setLevel(l);
    }

    /**
     * Logs a message with the logger's tag and
     * log level.
     *
     * @param msg the log message
     */
    public void log(String msg){
        switch (mLevel) {
            case Log.DEBUG:
                Log.d(mTag, msg);
                break;
            case Log.ERROR:
                Log.e(mTag, msg);
                break;
            case Log.INFO:
                Log.i(mTag, msg);
                break;
            default:
                Log.v(mTag, msg);
        }
    }

    public void setTag(String t){
        mTag = t;
    }

    /**
     * Checks that the log level is valid.
     * If not, sets log level to verbose.
     */
    public void setLevel(int l) {
        String err = "Invalid log setting " + l + ". Defaulting to verbose.";

        if(l != Log.VERBOSE &&
           l != Log.DEBUG   &&
           l != Log.ERROR   &&
           l != Log.INFO) {
            if(LOCAL_LOGD) Log.d(TAG, err);
            mLevel = Log.VERBOSE;
        }
        else mLevel = l;

    }
}
