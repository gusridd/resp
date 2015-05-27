package com.app.net;

import java.util.StringTokenizer;

import android.util.Log;

public final class IpFormatter {
	
	/**
	 * This function transforms the subnetmask and the local IP to a Code Form
	 * @param subnetmask
	 * @param ip
	 * @return
	 */
	public String ipEncode(int subnetmask, int ip) {
		int localAddress = ip & ~subnetmask;
		return removeTrailingZeros(ipToHex(String.valueOf(swap(localAddress))));
	}
	
	/**
	 * The opposite funcion of ipEncode, takes the subnetmask and the local ip to decode some other local ip
	 * @param subnetmaks
	 * @param ip
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	public String ipDecode(int subnetmaks, int ip, String code) throws Exception {
		int localAddress;
		int netAddress = ip & subnetmaks;
		code = code.trim();
		if (code.compareTo("") == 0)
			localAddress = 0;
		else{
			try{
				Log.d("ipformatter",code);
				localAddress = swap(Integer.parseInt(code, 16));
			}
			catch(Exception e){
				throw new Exception();
			}
		}
			
		int address = netAddress | localAddress;
		return intToIp(swap(address));
	}
	
	
	/**
	 * Changes the endianness of an int from Little Endian to Big Endian and viceversa
	 * @param value
	 * @return
	 */
	public int swap(int value) {
		int b1 = (value >> 0) & 0xff;
		int b2 = (value >> 8) & 0xff;
		int b3 = (value >> 16) & 0xff;
		int b4 = (value >> 24) & 0xff;
		return b1 << 24 | b2 << 16 | b3 << 8 | b4 << 0;
	}
	
	/**
	 * This function transforms an integer value ip to a human form ip
	 * @param ip
	 * @return
	 */
	public String intToIp(int ip) {
		return ((ip >> 24) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
				+ ((ip >> 8) & 0xFF) + "." + (ip & 0xFF);
	}
	
	/**
	 * This function takes an human form IP and transforms it to a Hex form
	 * @param ip
	 * @return
	 */
	private String ipToHex(String ip) {
		String response = "";
		StringTokenizer t = new StringTokenizer(ip, ".");
		while (t.hasMoreElements()) {
			String segment = t.nextToken();
			int aux = Integer.parseInt(segment);
			String mid = Integer.toHexString(aux);
			if (mid.length() == 1)
				mid = "0" + mid;
			response += mid;
		}
		return response.toUpperCase();
	}
	
	/**
	 * This function removes the zeros contained at the left of the code String
	 * @param code
	 * @return
	 */
	private String removeTrailingZeros(String code) {
		StringBuffer buff = new StringBuffer(code);
		while (buff.length() > 0 && buff.charAt(0) == '0')
			buff.deleteCharAt(0);
		return buff.toString();
	}

}
