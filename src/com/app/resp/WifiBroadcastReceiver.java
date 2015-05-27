package com.app.resp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class WifiBroadcastReceiver extends BroadcastReceiver {
	
	protected AlertDialog.Builder alertDialog;
	protected Activity act;
	
	public WifiBroadcastReceiver(Activity a){
		this.act = a;
		initializeAlertDialog();
	}
	
	protected void initializeAlertDialog() {
		alertDialog = new AlertDialog.Builder(act);
		alertDialog.setCancelable(false);
		// Setting Dialog Title
		alertDialog.setTitle(act.getString(R.string.conex));

		// Setting Dialog Message
		alertDialog.setMessage(act.getString(R.string.conex_question));
		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton(act.getString(R.string.yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						act.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
						dialog.cancel();
					}
				});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton(act.getString(R.string.no),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						act.finish();
					}
				});
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int extraWifiState = intent.getIntExtra(
				WifiManager.EXTRA_WIFI_STATE,
				WifiManager.WIFI_STATE_UNKNOWN);

		switch (extraWifiState) {

		case WifiManager.WIFI_STATE_ENABLED:
			Toast.makeText(act, "Wifi Habilitada", Toast.LENGTH_SHORT);

			ConnectivityManager conMan = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
			while (conMan.getActiveNetworkInfo() == null
					|| conMan.getActiveNetworkInfo().getState() != NetworkInfo.State.CONNECTED) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Toast.makeText(act, "Wifi Conectada", Toast.LENGTH_SHORT);
			break;

		case WifiManager.WIFI_STATE_DISABLED:
			alertDialog.show();
			Log.d("AlertDialog","un dialogo");
			break;
		}
	}

}
