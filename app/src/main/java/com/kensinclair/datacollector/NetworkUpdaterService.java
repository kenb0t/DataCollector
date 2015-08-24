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

/**
 * Creates a <code>NetworkReceiver</code>
 * to monitor for network changes.
 *
 * @see com.kensinclair.datacollector.UpdaterService
 *
 * @author Ken Sinclair
 */

public class NetworkUpdaterService extends UpdaterService {
    NetworkReceiver mNetworkReceiver;

    /**
     * Creates <code>NetworkReceiver</code>.
     *
     * @param intent  provided by system. Unused
     * @param flags   provided by system. Unused
     * @param startId provided by system. Unused
     * @return        <code>START_STICKY</code>
     */
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        mNetworkReceiver = new NetworkReceiver();
        return START_STICKY;
    }
}