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

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.kensinclair.logger.MyLog;

/**
 * Provides abstraction for updater services.
 *
 * @author Ken Sinclair
 */

public abstract class UpdaterService extends Service {
    protected static MyLog sLg = new MyLog();

    /**
     * Does nothing. Prevents method from cluttering up child classes.
     */
    @Override public IBinder onBind(Intent intent) { return null; }
}
