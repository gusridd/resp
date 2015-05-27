package com.app.resp;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

public class WifiAnnoyActivity extends RespActivity {
	
	protected BroadcastReceiver mWifiStateChangedReceiver = new WifiBroadcastReceiver(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.registerReceiver(mWifiStateChangedReceiver, new IntentFilter(
				WifiManager.WIFI_STATE_CHANGED_ACTION));
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPause() {
		// tries to unregister the received because memory issues
		try {
			this.unregisterReceiver(mWifiStateChangedReceiver);
		} catch (IllegalArgumentException e) {
			Log.e("ERROR", "unregisterReceiver", e);
		}
		super.onPause();
	}

	

	
}
