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
import android.util.Log;

import com.kensinclair.logger.MyLog;

/**
 * Kicks off DataCollector on device boot.
 *
 * @author Ken Sinclair
 */

public class AutoStart extends BroadcastReceiver {
    private static final boolean LOCAL_LOGD = true;
    private static final boolean LOCAL_LOGV = false;
    private static final String  TAG        = AutoStart.class.getSimpleName();

    private static MyLog sLg = new MyLog(TAG);

    /**
     * Starts <code>MainActivity</code> on
     * <code>BOOT_COMPLETED</code> signal.
     *
     * @param context provided by system
     * @param intent  provided by system. Unused
     */
    @Override public void onReceive(Context context, Intent intent) {
        if(LOCAL_LOGV) sLg.log("In onReceive");

        Intent kickOff = new Intent(context, MainActivity.class);

        kickOff.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        sLg.setLevel(Log.DEBUG);

        if(LOCAL_LOGD) sLg.log("Kicking off MainActivity...");
        context.startActivity(kickOff);
        if(LOCAL_LOGD) sLg.log("Kicked off MainActivity");

        sLg.setLevel(Log.VERBOSE);

        if(LOCAL_LOGV) sLg.log("Leaving onReceive");
    }
}
