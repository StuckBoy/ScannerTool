package zachstuck.scannertool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Zachary Stuck on 3/21/2018
 * for project ScannerTool.
 */

public class TrackActivity extends AppCompatActivity {

    Button trackPkg, returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        trackPkg = findViewById(R.id.TrackButton);
        trackPkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do the tracking part...
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
}
