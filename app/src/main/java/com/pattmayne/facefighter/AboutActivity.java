package com.pattmayne.facefighter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * This Activity displays information about the application.
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    /**
     * This method loads my website so the user can check out all my awesome stuff.
     * @param view
     */
    public void loadSite(View view)
    {
        Intent internet = new Intent();
        internet.setAction(Intent.ACTION_VIEW);
        internet.addCategory(Intent.CATEGORY_BROWSABLE);
        internet.setData(Uri.parse("http://www.pattmayne.com"));
        startActivity(internet);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    public void exit(View view)
    {
        finish();
    }

}
