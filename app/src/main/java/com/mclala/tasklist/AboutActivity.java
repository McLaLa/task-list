package com.mclala.tasklist;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.mclala.tasklist.R;

public class AboutActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_main);
        getVersion();
    }

    private void getVersion() {
        PackageInfo packageInfo;
        int appBuild = 0;
        String version = "2.0";

        try {
            String packageName = getApplicationContext().getApplicationInfo().packageName;
            packageInfo = getPackageManager().getPackageInfo(packageName, 0);
            appBuild = packageInfo.versionCode;
            version = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        StringBuilder versionString = new StringBuilder();
        versionString.append(getString(R.string.app_version)).append(" ").append(version).append(".").append(appBuild);

        TextView textView = (TextView)findViewById(R.id.textView2);
        textView.setText(versionString);
    }
}
