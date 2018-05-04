package zachstuck.scannertool;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Zachary Stuck on 3/27/2018
 * for project ScannerTool.
 */

public class UserPageActivity extends AppCompatActivity {
    /*
    This activity allows scanners to scan a package, manually scan a package, or logout.
     */

    Button scanButton, logButton, manEntryButton;
    TextView userfield;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);

        scanButton = findViewById(R.id.scanButton);
        logButton = findViewById(R.id.logoutButton);
        manEntryButton = findViewById(R.id.manEntryButton);

        userfield = findViewById(R.id.userField);
        Bundle userInfo = getIntent().getExtras();
        try {
            String username = userInfo.getString("userKey");
            userfield.setText(username);
        }
        catch (NullPointerException ne) {
            userfield.setText(R.string.str_error);
        }

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scanIntent = new Intent(UserPageActivity.this, ScanActivity.class);
                scanIntent.putExtra("userKey", userfield.getText());
                startActivity(scanIntent);
            }
        });

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        manEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manIntent = new Intent(UserPageActivity.this, ManualEntryActivity.class);
                manIntent.putExtra("userKey", userfield.getText());
                startActivity(manIntent);
            }
        });
        //Request permissions from user to access fine location.
        ActivityCompat.requestPermissions(UserPageActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    public void onBackPressed() {
        //Construct an alert dialog to prompt user before logging out.
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(UserPageActivity.this);
        builder.setTitle("Logout Confirmation");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                logoutConfirmed();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Canceled logout, resume normal activity.
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    public void logoutConfirmed() {
        //Start activity for previous page, finish this activity.
        Intent logOutIntent = new Intent(UserPageActivity.this, LoginActivity.class);
        UserPageActivity.this.startActivity(logOutIntent);
        finish();
    }
}