package zachstuck.scannertool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button logButton, trackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logButton = findViewById(R.id.LoginButton);
        trackButton = findViewById(R.id.TrackButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(startIntent);
                finish();
            }
        });
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trackIntent = new Intent(MainActivity.this, TrackActivity.class);
                MainActivity.this.startActivity(trackIntent);
                finish();
            }
        });
    }
}
