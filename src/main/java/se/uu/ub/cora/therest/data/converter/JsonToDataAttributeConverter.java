package se.uu.ub.cora.therest.data.converter;

import se.uu.ub.cora.therest.data.RestDataAttribute;
import se.uu.ub.cora.therest.data.RestDataElement;
import se.uu.ub.cora.therest.json.parser.JsonObject;
import se.uu.ub.cora.therest.json.parser.JsonParseException;
import se.uu.ub.cora.therest.json.parser.JsonString;
import se.uu.ub.cora.therest.json.parser.JsonValue;

public final class JsonToDataAttributeConverter implements JsonToDataConverter {

	private JsonObject jsonObject;

	static JsonToDataAttributeConverter forJsonObject(JsonObject jsonObject) {
		return new JsonToDataAttributeConverter(jsonObject);
	}

	private JsonToDataAttributeConverter(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public RestDataElement toInstance() {
		try {
			return tryToInstantiate();
		} catch (Exception e) {
			throw new JsonParseException("Error parsing jsonObject: " + e.getMessage(), e);
		}
	}

	private RestDataElement tryToInstantiate() {
		validateJsonData();
		String nameInData = getNameInDataFromJsonObject();
		JsonString value = (JsonString) jsonObject.getValue(nameInData);
		return RestDataAttribute.withNameInDataAndValue(nameInData, value.getStringValue());
	}

	private String getNameInDataFromJsonObject() {
		return jsonObject.keySet().iterator().next();
	}

	private void validateJsonData() {
		validateOnlyOneKeyValuePairAtTopLevel();
		validateNameInDataValueIsString();
	}

	private void validateOnlyOneKeyValuePairAtTopLevel() {
		if (jsonObject.size() != 1) {
			throw new JsonParseException("Attribute data can only contain one key value pair");
		}
	}

	private void validateNameInDataValueIsString() {
		String nameInData = getNameInDataFromJsonObject();
		JsonValue value = jsonObject.getValue(nameInData);
		if (!(value instanceof JsonString)) {
			throw new JsonParseException(
					"Value of attribute data \"" + nameInData + "\" must be a String");
		}
	}

}
