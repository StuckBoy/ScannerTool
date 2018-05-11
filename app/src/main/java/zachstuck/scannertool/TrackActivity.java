package zachstuck.scannertool;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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

/**
 * Created by Zachary Stuck on 3/21/2018
 * for project ScannerTool.
 */

public class TrackActivity extends AppCompatActivity {
    /*
    This activity is used for customers who do not have a profile, but still wish to look
    up information regarding a barcode that they know of.
     */

    Button trackPkg, returnButton, registerButton, loginButton;
    EditText packageField;
    String splitPkgs[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        packageField = findViewById(R.id.PackageEntry);

        trackPkg = findViewById(R.id.TrackButton);
        trackPkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pkgList = packageField.getText().toString();
                splitPkgs = pkgList.split("\\n");
                //Run async lookup using array of package numbers
                new AsyncPkgLookup().execute(splitPkgs);
            }
        });

        returnButton = findViewById(R.id.BackButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        registerButton = findViewById(R.id.Register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to register page for Tracker, not Scanner
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://euclid.nmu.edu/~zstuck/seniorProjStuff/webCustomerForm.php"));
                startActivity(browserIntent);
            }
        });

        loginButton = findViewById(R.id.Login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(TrackActivity.this, TrackActivityLogin.class);
                TrackActivity.this.startActivity(startIntent);
                finish();
            }
        });
    }

    public void onBackPressed() {
        Intent finishIntent = new Intent(TrackActivity.this, MainActivity.class);
        startActivity(finishIntent);
        finish();
    }

    private class AsyncPkgLookup extends AsyncTask<String[], String, String> {
        protected String doInBackground(String[]... args) {
            try {
                //Construct a URL using the package array given.
                String link = "http://euclid.nmu.edu/~zstuck/seniorProjStuff/pkgLookup.php";
                String data = "";
                for (int i = 0; i < args[0].length; i++) {
                    data += URLEncoder.encode(("pkg" + i), "UTF-8") + "=" + URLEncoder.encode(args[0][i], "UTF-8");
                    if (i + 1 < args[0].length) {
                        data += "&";
                    }
                }

                //Generate a URL object to flush and then read from.
                URL theURL = new URL(link);
                URLConnection connection = theURL.openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());
                outWriter.write(data);
                outWriter.flush();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();

            } catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }

        protected void onPostExecute(String result) {
            Log.d("onPostExecute", result);
            String pkgDetails[] = result.split("_");
            if (result.equals("Error")) {
                Toast.makeText(TrackActivity.this, "Error, package history not found.", Toast.LENGTH_LONG).show();
            }
            else {
                //Store the package data into an Intent, and then switch to PkgListActivity
                Intent ListIntent = new Intent(TrackActivity.this, PkgListActivity.class);
                ListIntent.putExtra("pkgData", pkgDetails);
                ListIntent.putExtra("requestedPkgs", splitPkgs);
                TrackActivity.this.startActivity(ListIntent);
                finish();
            }
        }
    }
}
