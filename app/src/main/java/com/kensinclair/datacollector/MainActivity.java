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

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kensinclair.logger.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Main activity for the DataCollector project.
 *
 * @see android.app.Activity
 * @see android.content.Context
 * @see android.content.ContextWrapper
 *
 * @author Ken Sincliar
 */

public class MainActivity extends Activity {
    private static final boolean LOCAL_LOGD = true;
    private static final boolean LOCAL_LOGV = false;
    private static final String  TAG        = MainActivity.class.getSimpleName();

    private static MyLog sLg = new MyLog(TAG);

    /**
     * Creates the app's main view (the "Export" button) and launches services.
     *
     * @param savedInstanceState provided by system
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(LOCAL_LOGV) sLg.log("In onCreate.");

        if(savedInstanceState != null) {      // If an instance of Datacollector is already running,
            if(LOCAL_LOGD) Log.d(TAG, "Already running.");                            // do nothing.
        }
        else {
            setContentView(R.layout.activity_main);

            final Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) { saveDb(); }
            });
            launchServices();
        }

        if(LOCAL_LOGV) sLg.log("Leaving onCreate.");
    }

    /** Launches all services which monitor for updates */
    private void launchServices() {
        Intent acctUpdaterService = new Intent(this, AccountsUpdaterService.class);
        Intent locationService    = new Intent(this, LocationUpdaterService.class);
        Intent netUpdaterService  = new Intent(this, NetworkUpdaterService.class);

        sLg.setLevel(Log.DEBUG);

        if(LOCAL_LOGD) sLg.log("Starting AcctUpdaterService.");
        startService(acctUpdaterService);
        if(LOCAL_LOGD) sLg.log("Started AcctUpdaterService.");

        if(LOCAL_LOGD) sLg.log("Starting LocationUpdaterService.");
        startService(locationService);
        if(LOCAL_LOGD) sLg.log("Started LocationUpdaterService.");

        if(LOCAL_LOGD) sLg.log("Starting NetUpdaterService.");
        startService(netUpdaterService);
        if(LOCAL_LOGD) sLg.log("Started NetUpdaterService.");

        sLg.setLevel(Log.VERBOSE);
    }

    /** Closes the database of harvested user information */
    @Override protected void onDestroy() {
        super.onDestroy();
        new DataDumpDbHelper(this).close();
    }

    /**
     * Saves the database to a directory the user can access.
     * Called when "Export" button is clicked.
     */
    private void saveDb() {
        if(LOCAL_LOGV) sLg.log("Saving database.");

        SQLiteDatabase db             = new DataDumpDbHelper(this).getReadableDatabase();
        File           dbFile         = new File(db.getPath());
        File           outputF;
        File           outputFileDir  = getExternalFilesDir(null);
        String         outputFileName = DataDumpDbHelper.DATABASE_NAME;

        outputF = new File(outputFileDir, outputFileName);

        try {
            if(outputF.createNewFile()) {
                if(LOCAL_LOGV) sLg.log("Created new file " + outputF + ".");
            } else if(LOCAL_LOGV) sLg.log("File " + outputF + " already exists.");

            FileChannel fin = new FileInputStream(dbFile).getChannel();
            FileChannel fout = new FileOutputStream(outputF).getChannel();

            fout.transferFrom(fin, 0, fin.size());

            fin.close();
            fout.close();

            Toast.makeText(this, outputF.getPath(), Toast.LENGTH_LONG).show();

            if(LOCAL_LOGV) sLg.log(outputF.getPath());

        } catch (IOException e) {
            String errorText = "Error creating " + outputF.getPath() + ": " + e.getMessage() + ".";
            Log.e(TAG, errorText);
            e.printStackTrace();
        }
    }
}
