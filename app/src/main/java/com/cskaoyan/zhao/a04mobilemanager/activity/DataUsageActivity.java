package com.cskaoyan.zhao.a04mobilemanager.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;

import com.cskaoyan.zhao.a04mobilemanager.R;

import java.util.List;

public class DataUsageActivity extends AppCompatActivity {

    private static final String TAG = "DataUsageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_usage);

        TrafficStats trafficStats = new TrafficStats();

        //Rx  receive  下行/ Tx transmite 上行  
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        long mobileTxBytes = TrafficStats.getMobileTxBytes();

/*      TrafficStats.getMobileRxPackets();
        TrafficStats.getMobileRxPackets();*/

        String s = Formatter.formatFileSize(this, mobileRxBytes);
        String s2 = Formatter.formatFileSize(this, mobileTxBytes);

        Log.i(TAG,"mobileRxBytes"+s);
        Log.i(TAG,"mobileTxBytes"+s2);

        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(0);

        for (ApplicationInfo info:installedApplications) {
            long uidRxBytes = TrafficStats.getUidRxBytes(info.uid);
            long uidTxBytes = TrafficStats.getUidTxBytes(info.uid);
            Log.i(TAG,"name="+info.packageName+   "uidRxBytes"+uidRxBytes +"uidTxBytes"+uidTxBytes );
        }



    }
}
