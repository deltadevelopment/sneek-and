package no.twomonkeys.sneek.app.components;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import no.twomonkeys.sneek.R;
import no.twomonkeys.sneek.app.components.login.LoginActivity;
import no.twomonkeys.sneek.app.shared.helpers.DataHelper;

/**
 * Created by simenlie on 24.10.2016.
 */

public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

       // DataHelper.setStartActivity(this);
        DataHelper.setContext(this);
        System.out.println("AUTH TOKEN IS " + DataHelper.getAuthToken());
        if (DataHelper.getAuthToken() == null) {
            openLoginActivity();
        } else {
            openMainActivity();
        }

    }

    public void logout() {
        DataHelper.storeCredentials(null, 0);
        openLoginActivity();
    }

    private void openLoginActivity() {
        Intent getMainScreenIntent = new Intent(this, LoginActivity.class);
        final int result = 1;

        getMainScreenIntent.putExtra("callingActivity", "StartActivity");

        startActivity(getMainScreenIntent);
        //startActivityForResult(getMainScreenIntent, result);
    }

    private void openMainActivity() {
        Intent getMainScreenIntent = new Intent(this, MainActivity.class);
        final int result = 1;

        getMainScreenIntent.putExtra("callingActivity", "MainActivity");

        startActivity(getMainScreenIntent);
        //startActivityForResult(getMainScreenIntent, result);
    }
}
