package zachstuck.scannertool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Zachary Stuck on 3/29/2018
 * for project ScannerTool.
 */

public class ManualEntryActivity extends AppCompatActivity {

    Button backButton, submitButton, refreshButton;
    EditText timeSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualscan);
        submitButton = findViewById(R.id.scanButton);
        backButton = findViewById(R.id.goBack);
        refreshButton = findViewById(R.id.refreshTime);
        timeSlot = findViewById(R.id.timeSlot);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Submit stuff
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
    public void onBackPressed() {
        Intent finishIntent = new Intent(ManualEntryActivity.this, UserPageActivity.class);
        startActivity(finishIntent);
        finish();
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
}
