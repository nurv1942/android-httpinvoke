package com.example.android.http_invoke.server;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

public class HandleEvent {

	public static JSONObject Process(JSONObject json, HttpSession session) {
		// TODO Auto-generated method stub
		JSONObject responseJson = null;
		if (json == null) {
			return null;
		}
		int typecode = json.getInt("typecode");
		Handle handle = null;
		switch (typecode) {
		case Request.REQUEST_TYPECODE:
			handle = new HandleTypeCode();
			break;
		default:
				break;
		}

		if (handle == null) {
			responseJson = new JSONObject();
			responseJson.put("typecode", -2);
			return responseJson;
		}

		responseJson = handle.Process(json, session);
		return responseJson;
	}

}
