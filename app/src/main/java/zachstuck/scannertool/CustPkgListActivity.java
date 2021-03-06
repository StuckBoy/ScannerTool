package zachstuck.scannertool;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by Zachary Stuck on 4/26/2018
 * for project ScannerTool.
 */

public class CustPkgListActivity extends AppCompatActivity {
    /*
    Similar to PkgListActivity, this activity builds textviews using information
    from previous activity, and then presents it to the user in a scrollview.
     */

    String[] pkgNums, pkgDetails;
    String username, preferences;
    LinearLayout listContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pkglist);
        Bundle extras = getIntent().getExtras();
        try {
            username = extras.getString("userKey");
        }
        catch (NullPointerException ne) {
            username = "";
        }

        listContainer = findViewById(R.id.pkgList);

        try {
            pkgNums = extras.getStringArray("requestedPkgs");
            pkgDetails = extras.getStringArray("pkgData");
            populateView(pkgNums, pkgDetails);
        }
        catch (NullPointerException npe) {
            pkgNums = null;
            pkgDetails = null;
            noPkgData();
        }
        try {
            preferences = extras.getString("preferences");
        }
        catch (NullPointerException ne) {
            preferences = "none";
        }
    }

    public void populateView(String[] pkgNums, String[] pkgDetails) {
        for (int i = 0; i < pkgDetails.length; i++) {
            TextView aTextView = buildATextView();
            if (Arrays.asList(pkgNums).contains(pkgDetails[i])) {
                aTextView.setTextSize(25);
                aTextView.setTypeface(null, Typeface.BOLD);
            }
            else {
                aTextView.setTextSize(11);
            }
            aTextView.setText(pkgDetails[i]);
            listContainer.addView(aTextView);
        }
    }

    public TextView buildATextView() {
        TextView tv = new TextView(CustPkgListActivity.this);
        tv.setTextColor(getResources().getColor(R.color.black));
        LinearLayout.LayoutParams layoutParams = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        tv.setLayoutParams(layoutParams);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(0,10,0,10);
        return tv;
    }

    public void noPkgData() {
        Intent backIntent = new Intent(CustPkgListActivity.this, CustomerPageActivity.class);
        Toast.makeText(CustPkgListActivity.this, "Couldn't load package details.", Toast.LENGTH_SHORT).show();
        backIntent.putExtra("userKey", username);
        backIntent.putExtra("preferences", preferences);
        CustPkgListActivity.this.startActivity(backIntent);
        finish();
    }

    public void onBackPressed() {
        Intent backIntent = new Intent(CustPkgListActivity.this, CustomerPageActivity.class);
        backIntent.putExtra("userKey", username);
        backIntent.putExtra("preferences", preferences);
        CustPkgListActivity.this.startActivity(backIntent);
        finish();
    }
}
