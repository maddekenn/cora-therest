package epc.therest.json.parser.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;

import epc.therest.json.parser.JsonArray;
import epc.therest.json.parser.JsonObject;
import epc.therest.json.parser.JsonString;
import epc.therest.json.parser.JsonValue;
import epc.therest.json.parser.JsonValueType;

public class SimpleJsonArrayAdapter implements JsonArray {

	private JSONArray simpleJsonArray;

	public SimpleJsonArrayAdapter(JSONArray simpleJsonArray) {
		this.simpleJsonArray = simpleJsonArray;
	}

	@Override
	public JsonValueType getValueType() {
		return JsonValueType.ARRAY;
	}

	@Override
	public Iterator<JsonValue> iterator() {
		List<JsonValue> list = new ArrayList<>();
		for (Object jsonValue : simpleJsonArray) {
			list.add(SimpleJsonValueFactory.createFromSimpleJsonValue(jsonValue));
		}
		return list.iterator();
	}

	@Override
	public JsonValue getValue(int index) {
		Object value = simpleJsonArray.get(index);
		return SimpleJsonValueFactory.createFromSimpleJsonValue(value);
	}

	@Override
	public JsonString getValueAsJsonString(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonObject getValueAsJsonObject(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JsonArray getValueAsJsonArray(int index) {
		// TODO Auto-generated method stub
		return null;
	}

}
