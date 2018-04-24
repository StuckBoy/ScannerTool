package zachstuck.scannertool;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Zachary Stuck on 4/24/2018
 * for project ScannerTool.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("\n\nThe Token: ", refreshToken);

        sendToDB(refreshToken);
    }

    public void sendToDB(String token) {
        //Send to DB to allow push notifications
    }
}
