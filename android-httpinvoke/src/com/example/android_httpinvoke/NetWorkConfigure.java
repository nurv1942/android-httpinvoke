package com.example.android_httpinvoke;

/**
 * 
 *  @author wsh
 * 设置网络访问参数
 * 
 */
public class NetWorkConfigure {

	private static NetWorkConfigure netconfigure;

	private int TIMEOUT_DEFAULT;
	private String ENCODING;
	
	public static NetWorkConfigure getInstance(){
		if(netconfigure==null)
			netconfigure = new NetWorkConfigure();
		return netconfigure;
	}

	private NetWorkConfigure() {
		this.TIMEOUT_DEFAULT = 1000;
		this.ENCODING = "UTF-8";
	}

	public int getTIMEOUT_DEFAULT() {
		return TIMEOUT_DEFAULT;
	}

	public void setTIMEOUT_DEFAULT(int tIMEOUT_DEFAULT) {
		TIMEOUT_DEFAULT = tIMEOUT_DEFAULT;
	}

	public String getENCODING() {
		return ENCODING;
	}

	public void setENCODING(String eNCODING) {
		ENCODING = eNCODING;
	}

}
