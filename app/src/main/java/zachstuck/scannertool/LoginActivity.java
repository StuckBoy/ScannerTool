package zachstuck.scannertool;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * Created by Zachary Stuck on 3/20/2018
 for project ScannerTool.
 */

public class LoginActivity extends AppCompatActivity {

    Button submitButton, returnButton, registerButton;
    EditText userCred, passCred;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userCred = findViewById(R.id.userField);
        passCred = findViewById(R.id.passField);
        submitButton = findViewById(R.id.Login);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do login checks
                username = userCred.getText().toString();
                password = passCred.getText().toString();
                new AsyncLogin().execute(username, password);
            }
        });
        returnButton = findViewById(R.id.goBack);
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
                //Redirect to register page.
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent finishIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(finishIntent);
        finish();
    }

    private class AsyncLogin extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {
            try {
                String username = args[0];
                String password = args[1];

                String link = "http://euclid.nmu.edu/~zstuck/seniorProjStuff/scannerLogin.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
            if (result.equals("Success!")) {
                Intent logIntent = new Intent(LoginActivity.this, UserPageActivity.class);
                logIntent.putExtra("userKey", username);
                LoginActivity.this.startActivity(logIntent);
                finish();
            }
            else if (result.equals("Error")) {
                Toast.makeText(LoginActivity.this, "Error logging in, please try again.", Toast.LENGTH_LONG).show();
            }
        }
    }
}