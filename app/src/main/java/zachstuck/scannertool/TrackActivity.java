package zachstuck.scannertool;

import android.content.Intent;
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
import java.util.ArrayList;

/**
 * Created by Zachary Stuck on 3/21/2018
 * for project ScannerTool.
 */

public class TrackActivity extends AppCompatActivity {

    Button trackPkg, returnButton;
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
                //Do async php stuff and switch to scrollview with detailed info.
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
    }

    public void onBackPressed() {
        Intent finishIntent = new Intent(TrackActivity.this, MainActivity.class);
        startActivity(finishIntent);
        finish();
    }

    private class AsyncPkgLookup extends AsyncTask<String[], String, String> {
        protected String doInBackground(String[]... args) {
            try {
                String link = "http://euclid.nmu.edu/~zstuck/seniorProjStuff/pkgLookup.php";
                String data = "";
                for (int i = 0; i <= args.length; i++) {
                    data += URLEncoder.encode(("pkg" + i), "UTF-8") + "=" + URLEncoder.encode(args[0][i], "UTF-8");
                    if (i + 1 <= args.length) {
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

            if (result.equals("Success")) {
            /*    Intent ListIntent = new Intent(TrackActivity.this, PkgListActivity.class);
                ListIntent.putExtra("pkgData", pkgDetails);
                ListIntent.putExtra("requestedPkgs", splitPkgs);
                TrackActivity.this.startActivity(ListIntent);
                finish();
            }else if (result.equals("Error")) {
                Toast.makeText(TrackActivity.this, "Error, package history not found.", Toast.LENGTH_LONG).show();
            */}
        }
    }
}
