package com.cif.dt;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestParameters;

public abstract class StoreHandler {

	private final static Logger LOGGER = Logger.getLogger(StoreHandler.class.getName());

	protected static final String ERROR_LOAD_MESSAGE = "Error loading %s! Please inform your administrator";

	protected static final String ERROR_SAVE_MESSAGE = "Error saving store entity! Please inform your administrator";

	protected static final String ERROR_DELETE_MESSAGE = "Error deleting store entity! Please inform your administrator";

	protected HttpServletRequest request;

	protected HttpServletResponse response;

	protected HttpSession session;

	@Inject
	@RequestParameters
	protected Map<String, String[]> params;

	@Inject
	public StoreHandler(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		this.request = request;
		this.response = response;
		this.session = session;
	}

	protected void initFilter(Filter filter) throws ParseException {
		if (params.containsKey("start") && params.containsKey("limit")) {
			filter.setStart(Integer.valueOf(params.get("start")[0]));
			filter.setLimit(Integer.valueOf(params.get("limit")[0]));
		}
		if (params.containsKey("sort")) {
			filter.setSortColumn(params.get("sort")[0]);
		}
		if (params.containsKey("dir")) {
			filter.setSortDirection(params.get("dir")[0]);
		}
	}

	protected HashMap<String, String> parseStoreFilters(String filterStr) {
		HashMap<String, String> hMap = new HashMap<String, String>();
		Object rawObj = JSONValue.parse(filterStr);
		JSONArray filterArr = (JSONArray)rawObj;
		
		for (Object obj: filterArr) {
			JSONObject jsonObj = (JSONObject)obj;
			String property = jsonObj.get("property").toString();
			String value = jsonObj.get("value").toString();
			hMap.put(property, value);
		}
		
		return hMap;
	}
	
	@SuppressWarnings("unchecked")
	protected void writeStoreResponse(List<StoreRow> rows, boolean success, String errorMsg) throws IOException {
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();	    
	    
		JSONObject result = new JSONObject();
	    JSONObject meta = new JSONObject();
	    result.put("meta", meta);
	    
	    JSONArray data = new JSONArray();						
		for (StoreRow row : rows) {
			data.add(row.toJson());
		}
	    result.put("data", data);
	    meta.put("success", success);	    
	    if (!success) { meta.put("error", errorMsg); }
        
	    out.write(result.toJSONString());
        out.flush();
        out.close();
	}
	
	protected JSONArray parseStorePayload() {
		JSONObject jsonObj = ServletUtils.parsePostPayload(request);
		JSONArray array = (JSONArray)jsonObj.get("data");
		return array; 
	}
	
	protected abstract List<StoreRow> handleStoreRead() throws Exception;
	
	protected abstract List<StoreRow> handleStoreWrite(Boolean delete);
	
	public void read() throws IOException {
		Boolean success = false;
		String errorMsg = "";
		List<StoreRow> rows = new ArrayList<StoreRow>();
		
		try {
			rows = handleStoreRead();
			success = true;
		} catch (Exception e) {
			errorMsg = String.format(ERROR_LOAD_MESSAGE, "workspaces");
			LOGGER.log(Level.SEVERE, "Exception", e);
		}
		writeStoreResponse(rows, success, errorMsg);
	}
	
	public void write(Boolean delete) throws IOException {
		Boolean success = false;
		String errorMsg = "";
		List<StoreRow> rows = new ArrayList<StoreRow>();		
		
		try {
			rows = handleStoreWrite(delete);
			success = true;
		} catch (Exception e) {
			errorMsg = (delete==true) ? ERROR_DELETE_MESSAGE : ERROR_SAVE_MESSAGE; 
			LOGGER.log(Level.SEVERE, "Exception", e);		
			LOGGER.log(Level.SEVERE, "Exception", e);
		}
		writeStoreResponse(rows, success, errorMsg);
	}
}
