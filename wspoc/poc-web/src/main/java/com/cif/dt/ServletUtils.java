package com.cif.dt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ServletUtils {
	
	private static String postBodyStr(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		String line = null;		
		try {
			BufferedReader reader = request.getReader();
			while( (line = reader.readLine()) != null ){
				sb.append(line);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());			
		}
		return sb.toString();
	}
	
	public static JSONObject parsePostPayload(HttpServletRequest request) {
		String strPOST = postBodyStr(request);
		JSONObject jsonObj = (JSONObject)JSONValue.parse(strPOST);		
		return jsonObj; 
	}
	
	public static void writePostResponse(HttpServletResponse response, boolean success, String errorMsg) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
	    response.setContentType("application/json");
	    JSONObject result = new JSONObject();
	    JSONObject meta = new JSONObject();
	    result.put("meta", meta);
	    meta.put("success", success);	    
	    if (!success) { meta.put("error", errorMsg); }
        out.write(result.toJSONString()); 
        out.close();
	}

}
