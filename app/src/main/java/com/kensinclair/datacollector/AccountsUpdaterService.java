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
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.Intent;
import android.util.Log;

/**
 * Service listens for account updates.
 *
 * @see android.accounts.OnAccountsUpdateListener
 *
 * @author Ken Sinclair
 */

public class AccountsUpdaterService extends    UpdaterService
                                    implements OnAccountsUpdateListener {
    private static final boolean LOCAL_LOGD = true;
    private static final boolean LOCAL_LOGV = false;
    private static final String  TAG        = AccountsUpdaterService.class.getSimpleName();

    private AccountManager mAccountManager;
    private DbUpdater      mDbUpdater;

    /**
     * Starts sticky service and assigns itself as a
     * listener of account changes.
     *
     * @param intent  provided by system. Unused
     * @param flags   provided by system. Unused
     * @param startId provided by system. Unused
     * @return        <code>START_STICKY</code>
     */
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if(LOCAL_LOGV) sLg.log("In onStartCommand.");

        mDbUpdater = new DbUpdater(getBaseContext());

        try {
            sLg.setLevel(Log.DEBUG);

            if(LOCAL_LOGD) sLg.log("Setting up accountManager.");
            mAccountManager = AccountManager.get(this);
            mAccountManager.addOnAccountsUpdatedListener(this, null, true);
            if(LOCAL_LOGD) sLg.log("Account manager setup with listener " + toString() + ".");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up accounts listener: " + e.getMessage());
            e.printStackTrace();
        } finally { sLg.setLevel(Log.VERBOSE); }

        if(LOCAL_LOGV) sLg.log("onStartCommand returning START_STICKY = " + START_STICKY + ".");

        return START_STICKY;
    }

    /**
     * Calls database updater.
     *
     * @param accounts provided by system
     */
    @Override public void onAccountsUpdated(Account[] accounts) {
        sLg.setLevel(Log.DEBUG);
        if(LOCAL_LOGD) sLg.log("Updating accounts.");
        sLg.setLevel(Log.VERBOSE);

        long time = System.currentTimeMillis();
        mDbUpdater.updateTable(accounts, time);
    }

    /** Removes accounts updated listener. */
    @Override public void onDestroy() {
        super.onDestroy();
        mAccountManager.removeOnAccountsUpdatedListener(this);

        sLg.setLevel(Log.DEBUG);
        if(LOCAL_LOGD) sLg.log("Destroying account updater service " + this.toString() + ".");
        sLg.setLevel(Log.VERBOSE);
    }

    /** Sets logger's tag. */
    @Override public void onCreate() {
        super.onCreate();
        sLg.setTag(TAG);
    }
}
