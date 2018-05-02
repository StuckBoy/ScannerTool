package zachstuck.scannertool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by Zachary Stuck on 3/29/2018
 * for project ScannerTool.
 */

public class ManualEntryActivity extends AppCompatActivity {

    Button backButton, submitButton, refreshButton;
    EditText timeSlot, coordSlot, userSlot, pkgSlot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualscan);
        submitButton = findViewById(R.id.scanButton);
        backButton = findViewById(R.id.goBack);
        refreshButton = findViewById(R.id.refreshTime);
        userSlot = findViewById(R.id.userSlot);
        timeSlot = findViewById(R.id.timeSlot);
        coordSlot = findViewById(R.id.coordSlot);
        pkgSlot = findViewById(R.id.pkgField);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = userSlot.getText().toString();
                String pkgNum = pkgSlot.getText().toString();
                String timestamp = timeSlot.getText().toString();
                String coords = coordSlot.getText().toString();
                new ManualEntryActivity.AsyncScanSubmit().execute(username, pkgNum, timestamp, coords);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });

        Bundle extras = getIntent().getExtras();
        String username;

        try {
            username = extras.getString("userKey");
            userSlot.setText(username);
        }
        catch (NullPointerException npe){
            userSlot.setText(R.string.str_error);
        }
        setTime();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(ManualEntryActivity.this);
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
                Toast.makeText(ManualEntryActivity.this, "Upload Complete.", Toast.LENGTH_LONG).show();
                Intent finishIntent = new Intent(ManualEntryActivity.this, UserPageActivity.class);
                finishIntent.putExtra("userKey", userSlot.getText().toString());
                ManualEntryActivity.this.startActivity(finishIntent);
                finish();
            }
            else if (result.equals("Error")) {
                Toast.makeText(ManualEntryActivity.this, "Error while submitting, please try again.", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(ManualEntryActivity.this, "Technical Error", Toast.LENGTH_LONG).show();
            }
        }
    }
}
