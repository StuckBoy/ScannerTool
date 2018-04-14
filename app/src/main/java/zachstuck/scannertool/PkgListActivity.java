package zachstuck.scannertool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Zachary Stuck on 4/14/2018
 * for project ScannerTool.
 */
public class PkgListActivity extends AppCompatActivity {

    String[] pkgNums;
    String[] pkgDetails;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pkglist);
        Bundle extras = getIntent().getExtras();

        try {
            pkgNums = extras.getStringArray("requestedPkgs");
            pkgDetails = extras.getStringArray("pkgDetails");
            populateView(pkgNums, pkgDetails);
        }
        catch (NullPointerException npe) {
            pkgNums = null;
            pkgDetails = null;
            noPkgData();
        }
    }

    public void populateView(String[] pkgNums, String[] pkgDetails) {

    }

    public void noPkgData() {
        Intent backIntent = new Intent(PkgListActivity.this, TrackActivity.class);
        Toast.makeText(PkgListActivity.this, "Couldn't load package details.", Toast.LENGTH_SHORT).show();
        PkgListActivity.this.startActivity(backIntent);
    }
}
