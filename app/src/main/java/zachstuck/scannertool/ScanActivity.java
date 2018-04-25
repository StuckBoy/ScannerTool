package zachstuck.scannertool;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by Zachary Stuck on 4/10/2018
 * for project ScannerTool.
 */

public class ScanActivity extends AppCompatActivity {

    Button submitButton, cancelButton, coordButton, scanPkgButton, refreshButton;
    EditText timeSlot, userSlot, pkgSlot, coordSlot;
    LocationManager locMan;
    Context aContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanpage);
        aContext = this;

        submitButton = findViewById(R.id.submitData);
        cancelButton = findViewById(R.id.cancelScan);
        coordButton = findViewById(R.id.coordsButton);
        scanPkgButton = findViewById(R.id.scanButton);
        refreshButton = findViewById(R.id.refreshTime);

        timeSlot = findViewById(R.id.timeSlot);
        userSlot = findViewById(R.id.userSlot);
        pkgSlot = findViewById(R.id.pkgField);
        coordSlot = findViewById(R.id.coordField);

        Bundle extras = getIntent().getExtras();
        String username;

        try {
            username = extras.getString("userKey");
            userSlot.setText(username);
        }
        catch (NullPointerException npe){
            userSlot.setText(R.string.str_error);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Grab all relevant info, send to php
                String username = userSlot.getText().toString();
                String pkgNum = pkgSlot.getText().toString();
                String timestamp = timeSlot.getText().toString();
                String coords = coordSlot.getText().toString();
                new AsyncScanSubmit().execute(username, pkgNum, timestamp, coords);

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        coordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Toast.makeText(ScanActivity.this, "Last Location Fetched", Toast.LENGTH_SHORT).show();
                }
                catch(SecurityException e) {
                    checkForLocEnabled(locMan, aContext);
                }
            }
        });
        scanPkgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //IntentIntegrator derives from ZXing integrated library
                new IntentIntegrator(ScanActivity.this).initiateScan();
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });

        locMan = (LocationManager) aContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListen = new LocationListener() {
            @Override
            public void onLocationChanged(Location loc) {
                double latString = loc.getLatitude();
                double lonString = loc.getLongitude();
                String timeString = String.format("%.4f", latString) + ", " + String.format("%.4f", lonString);
                coordSlot.setText(timeString);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

         }

        };
        try {
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locListen);
        }
        catch(SecurityException e) {
            checkForLocEnabled(locMan, aContext);
        }
        setTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForLocEnabled(locMan, aContext);
    }

    private void checkForLocEnabled(LocationManager locMan, Context aContext) {
        if (!locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder locAlert = new AlertDialog.Builder(aContext);
            locAlert.setTitle("Enable Location Services");
            locAlert.setMessage("In order to properly log packages, please enable your location services.");
            locAlert.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent locIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(locIntent);
                }
            });
            locAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            locAlert.create();
            locAlert.show();
        }
        else {
            //Provider is enabled, and nothing happens here.
            return;
        }
    }

    //Handles the results returned from starting the scanner activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(ScanActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                pkgSlot.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Refreshes the current time and stores it to the EditText field.
    public void setTime() {
        Calendar cal = Calendar.getInstance();
        int year, month, day, hour, minute;
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        month++;
        day = cal.get(Calendar.DATE);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        String aMinute = String.valueOf(minute);
        if (minute < 10) {
            aMinute = "0" + String.valueOf(minute);
        }
        timeSlot.setText(month + "/" + day + "/" + year + " @ " + hour + ":" + aMinute);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(ScanActivity.this);
        builder.setTitle("Cancel Confirmation");
        builder.setMessage("Are you sure you want to cancel this scan?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelScan();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Continue normal operations
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    public void cancelScan() {
        finish();
    }

    private class AsyncScanSubmit extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            try {
                String username = args[0];
                String pkgNum = args[1];
                String timestamp = args[2];
                String coords = args[3];

                String link = "http://euclid.nmu.edu/~zstuck/seniorProjStuff/trackpkg.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("pkgNum", "UTF-8") + "=" + URLEncoder.encode(pkgNum, "UTF-8");
                data += "&" + URLEncoder.encode("timestamp", "UTF-8") + "=" + URLEncoder.encode(timestamp, "UTF-8");
                data += "&" + URLEncoder.encode("coords", "UTF-8") + "=" + URLEncoder.encode(coords, "UTF-8");

                URL theURL = new URL(link);
                URLConnection connection = theURL.openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());

                outWriter.write(data);
                outWriter.flush();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                return stringBuilder.toString();
            }
            catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }

        protected void onPostExecute(String result) {
            Log.d("onPostExecute", result);
            if (result.contains("Success")) {
                Toast.makeText(ScanActivity.this, "Upload Complete.", Toast.LENGTH_LONG).show();
                Intent finishIntent = new Intent(ScanActivity.this, UserPageActivity.class);
                finishIntent.putExtra("userKey", userSlot.getText().toString());
                ScanActivity.this.startActivity(finishIntent);
                finish();
            }
            else if (result.equals("Error")) {
                Toast.makeText(ScanActivity.this, "Error while submitting, please try again.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(ScanActivity.this, "Technical Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}