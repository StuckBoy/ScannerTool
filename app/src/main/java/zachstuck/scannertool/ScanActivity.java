package zachstuck.scannertool;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Calendar;

/**
 * Created by Zachary Stuck on 4/10/2018
 * for project ScannerTool.
 */

public class ScanActivity extends AppCompatActivity {

    Button submitButton, cancelButton, coordButton, scanPkgButton, refreshButton;
    EditText timestamp, userSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanpage);
        submitButton = findViewById(R.id.submitData);
        cancelButton = findViewById(R.id.cancelScan);
        coordButton = findViewById(R.id.coordsButton);
        scanPkgButton = findViewById(R.id.scanButton);
        refreshButton = findViewById(R.id.refreshTime);
        timestamp = findViewById(R.id.timeSlot);
        userSlot = findViewById(R.id.userSlot);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Submit Stuff
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
                //Poll Location
            }
        });
        scanPkgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do the scan thing
                //Fetch the result
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });

        setTime();
    }

    public void setTime() {
        Calendar cal = Calendar.getInstance();
        int year, month, day, hour, minute;
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DATE);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        timestamp.setText(month + "/" + day + "/" + year + " @ " + hour + ":" + minute);
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
}
