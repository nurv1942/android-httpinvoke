package com.example.android.http_invoke.server;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

public interface Handle {
	public JSONObject Process(JSONObject json, HttpSession session);
}
