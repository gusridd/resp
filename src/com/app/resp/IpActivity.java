package com.app.resp;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import com.app.net.IpFormatter;

public class IpActivity extends Activity {
	
	String room;
	
	protected String getIp() throws Exception {
		String code = room;
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		IpFormatter formatter = new IpFormatter();
		return formatter.ipDecode(dhcp.netmask, dhcp.ipAddress, code);
	}
	
}
