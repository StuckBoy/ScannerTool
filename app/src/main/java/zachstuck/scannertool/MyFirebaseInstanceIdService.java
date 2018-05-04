package zachstuck.scannertool;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Zachary Stuck on 4/24/2018
 * for project ScannerTool.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        /*
        This token identifies the phone, allowing for push notifications specifically to it.
        It is generated on first startup, and must be properly stored for later use.
         */
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("\n\n\nThe Token: \n\n\n", refreshToken);
        System.out.print(refreshToken);

        //sendToDB(refreshToken);
    }
    /*
    public void sendToDB(String token) {
        //Send to DB to allow push notifications
        try {

            String link = "http://euclid.nmu.edu/~zstuck/seniorProjStuff/phoneToken.php";
            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
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

            String result = stringBuilder.toString();

        }
        catch (Exception e) {
            Log.d("Exception: ", e.getMessage());
        }
        //PHP inserts into firebasekeys table (token, null)
        //Prompt user to associate phone with account on tracking page for faster package updates.
    } */
}
