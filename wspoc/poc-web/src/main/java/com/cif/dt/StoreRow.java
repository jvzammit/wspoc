package com.cif.dt;

import org.json.simple.JSONObject;

public abstract class StoreRow {
	long id;
	abstract public JSONObject toJson();
}
