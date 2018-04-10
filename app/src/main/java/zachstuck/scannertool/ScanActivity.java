package zachstuck.scannertool;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;

/**
 * Created by Zachary Stuck on 4/10/2018
 * for project ScannerTool.
 */

public class ScanActivity extends AppCompatActivity {

    Button submitButton, cancelButton, coordButton, scanPkgButton, refreshButton;
    EditText timeSlot, userSlot, pkgSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanpage);
        submitButton = findViewById(R.id.submitData);
        cancelButton = findViewById(R.id.cancelScan);
        coordButton = findViewById(R.id.coordsButton);
        scanPkgButton = findViewById(R.id.scanButton);
        refreshButton = findViewById(R.id.refreshTime);

        timeSlot = findViewById(R.id.timeSlot);
        userSlot = findViewById(R.id.userSlot);
        pkgSlot = findViewById(R.id.pkgField);

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
                //IntentIntegrator derives from ZXing integrated library
                new IntentIntegrator(ScanActivity.this).initiateScan();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(ScanActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                pkgSlot.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setTime() {
        Calendar cal = Calendar.getInstance();
        int year, month, day, hour, minute;
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DATE);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);
        timeSlot.setText(month + "/" + day + "/" + year + " @ " + hour + ":" + minute);
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