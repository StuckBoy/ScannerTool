package zachstuck.scannertool;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Zachary Stuck on 3/27/2018
 * for project ScannerTool.
 */
public class UserPageActivity extends AppCompatActivity {

    Button scanButton, logButton, manEntryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpage);
        scanButton = findViewById(R.id.scanButton);
        logButton = findViewById(R.id.logoutButton);
        manEntryButton = findViewById(R.id.manEntryButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open the ZXing scanner
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
                startActivity(manIntent);
            }
        });
    }

    public void onBackPressed() {
        //Pop-up confirmation
        boolean confirmed = false;
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
                //Canceled logout, resume normal activity
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    public void logoutConfirmed() {
        //Log the person out...kill their session
        Intent logOutIntent = new Intent(UserPageActivity.this, LoginActivity.class);
        UserPageActivity.this.startActivity(logOutIntent);
        finish();
    }
}
