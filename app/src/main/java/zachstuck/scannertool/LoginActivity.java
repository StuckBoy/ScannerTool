package zachstuck.scannertool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Zachary Stuck on 3/20/2018
 for project ScannerTool.
 */

public class LoginActivity extends AppCompatActivity{

    private Button submitButton, returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submitButton = findViewById(R.id.Login);
        returnButton = findViewById(R.id.goBack);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent finishIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(finishIntent);
        finish();
    }
}
