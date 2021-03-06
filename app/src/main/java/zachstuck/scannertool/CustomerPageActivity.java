package zachstuck.scannertool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zachary Stuck on 4/27/2018
 * for project ScannerTool.
 */
public class CustomerPageActivity extends AppCompatActivity {
    Button trackPkgButton, logoutButton, quickLookupButton;
    CheckBox email, notifications;
    EditText packageField;
    TextView customerField;
    String splitPkgs[];
    String prefStatus;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle userInfo = getIntent().getExtras();
        setContentView(R.layout.activity_customerpage);

        //Fetch SharedPreference string, this string is the short term history from last search.
        pref = getApplicationContext().getSharedPreferences("pkgs", Context.MODE_PRIVATE);
        final String qPkgs = pref.getString("pkgs", "");
        final String[] qPkgList = qPkgs.split(",");

        packageField = findViewById(R.id.PackageEntry);
        customerField = findViewById(R.id.userField);

        try {
            String username = userInfo.getString("userKey");
            customerField.setText(username);
        }
        catch (NullPointerException ne) {
            customerField.setText(R.string.str_error);
        }

        email = findViewById(R.id.email);
        notifications = findViewById(R.id.pushNotify);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBoxes();
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBoxes();
            }
        });

        try {
            //Set checkbox toggles from user preferences.
            prefStatus = userInfo.getString("preferences");
            switch (prefStatus) {
                case ("both"):
                    email.toggle();
                    notifications.toggle();
                    break;

                case ("email"):
                    email.toggle();
                    break;

                case ("notifications"):
                    notifications.toggle();
                    break;
            }
        }
        catch (NullPointerException ne) {
            email.setChecked(false);
            notifications.setChecked(false);
        }

        trackPkgButton = findViewById(R.id.TrackButton);
        trackPkgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pkgList = packageField.getText().toString();
                splitPkgs = pkgList.split("\\n");
                List<String> pkgStringList = new ArrayList<>(Arrays.asList(qPkgList));
                //To avoid repeats, clear old history
                pkgStringList.clear();
                for (String aPkg: splitPkgs) {
                    pkgStringList.add(aPkg);
                }
                //This cover initial default case
                if (pkgStringList.get(0).equals("")) {
                    pkgStringList.set(0, " ");
                }
                //Only lookup 5 numbers
                while (pkgStringList.size() > 5) {
                    pkgStringList.remove(0);
                }
                String[] pkgStringListArray = pkgStringList.toArray(new String[pkgStringList.size()]);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < pkgStringListArray.length; i++) {
                    sb.append(pkgStringListArray[i]).append(",");
                }
                //Commit package list to SharedPreferences
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("pkgs", sb.toString()).commit();
                new CustomerPageActivity.AsyncPkgLookup().execute(splitPkgs);
            }
        });
        logoutButton = findViewById(R.id.BackButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        quickLookupButton = findViewById(R.id.QuickLookupButton);
        quickLookupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //splitPkgs becoms qPkgList contents for Intent storage
                splitPkgs = qPkgList;
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("pkgs", qPkgs).commit();
                new CustomerPageActivity.AsyncPkgLookup().execute(qPkgList);
            }
        });
    }

    public void onBackPressed() {
        Intent finishIntent = new Intent(CustomerPageActivity.this, TrackActivityLogin.class);
        startActivity(finishIntent);
        finish();
    }

    public void checkCheckBoxes() {
        //Get status of check boxes, then send to php with status of both.
    }

    private class AsyncPkgLookup extends AsyncTask<String[], String, String> {
        protected String doInBackground(String[]... args) {
            try {
                String link = "http://euclid.nmu.edu/~zstuck/seniorProjStuff/pkgLookup.php";
                String data = "";
                //Construct URL with package numbers as data
                for (int i = 0; i < args[0].length; i++) {
                    data += URLEncoder.encode(("pkg" + i), "UTF-8") + "=" + URLEncoder.encode(args[0][i], "UTF-8");
                    if (i + 1 < args[0].length) {
                        data += "&";
                    }
                }

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
                Toast.makeText(CustomerPageActivity.this, "Error, package history not found.", Toast.LENGTH_LONG).show();
            }
            else {
                Intent ListIntent = new Intent(CustomerPageActivity.this, CustPkgListActivity.class);
                ListIntent.putExtra("pkgData", pkgDetails);
                ListIntent.putExtra("requestedPkgs", splitPkgs);
                ListIntent.putExtra("preferences", prefStatus);
                ListIntent.putExtra("userKey", customerField.getText());
                CustomerPageActivity.this.startActivity(ListIntent);
                finish();
            }
        }
    }
}
