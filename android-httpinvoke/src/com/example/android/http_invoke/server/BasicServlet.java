package com.example.android.http_invoke.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public class BasicServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		JSONObject json;
		JSONObject retJson = null;
		try {
			String jsonString = request.getParameter("json");
			json = JSONObject.fromObject(URLDecoder.decode(jsonString));
			System.out.println("request:" + jsonString);
			retJson = new HandleEvent().Process(json, request.getSession());
			if (retJson == null) {
				retJson = JSONObject.fromObject("{\"typecode\":-1}");
			}

		} catch (Exception e) {
			retJson = JSONObject.fromObject("{\"typecode\":-1}");
		} finally {
			System.out.println("response:" + retJson.toString());
			PrintWriter out = response.getWriter();
			out.print(retJson.toString());
			out.flush();
			out.close();

		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		HandleEvent handle = null;
		JSONObject json;
		JSONObject retJson = null;
		System.out.println("In Post");
		try {

			StringBuffer sb = new StringBuffer();
			String line = null;
			BufferedReader br = request.getReader();
			while ((line = br.readLine()) != null)
				sb.append(line);
			String s = URLDecoder.decode(sb.toString(), "utf-8");
			json = JSONObject.fromObject(s);
			retJson = new HandleEvent().Process(json, request.getSession());
			if (retJson == null) {
				retJson = JSONObject.fromObject("{\"typecode\":-1}");
			}
		} catch (Exception e) {
			retJson = JSONObject.fromObject("{\"typecode\":-1}");
		} finally {
			PrintWriter out = response.getWriter();
			out.print(retJson.toString());
			out.flush();
			out.close();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

}
