package zachstuck.scannertool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Zachary Stuck on 3/29/2018
 * for project ScannerTool.
 */

public class ManualEntryActivity extends AppCompatActivity {

    Button backButton, submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualscan);
        submitButton = findViewById(R.id.scanButton);
        backButton = findViewById(R.id.goBack);
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
    }

    @Override
    public void onBackPressed() {
        Intent finishIntent = new Intent(ManualEntryActivity.this, UserPageActivity.class);
        startActivity(finishIntent);
        finish();
    }
}
