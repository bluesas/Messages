package sas.app.sms.base;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

/**
 * @author Wei Runqi
 * @date 15/7/15
 */

public class BaseActivity extends Activity {

    protected ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
    }

    protected void initActionBar() {
        actionBar = getActionBar();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
    }


}
