package epc.therest.data.converter;

import java.util.Map.Entry;

import epc.therest.data.RestDataElement;
import epc.therest.data.RestDataGroup;
import epc.therest.json.parser.JsonArray;
import epc.therest.json.parser.JsonObject;
import epc.therest.json.parser.JsonParseException;
import epc.therest.json.parser.JsonString;
import epc.therest.json.parser.JsonValue;

public final class JsonToDataGroupConverter implements JsonToDataConverter {

	private JsonObject jsonObject;
	private RestDataGroup restDataGroup;
	private JsonObject jsonGroupChildren;

	static JsonToDataGroupConverter forJsonObject(JsonObject jsonObject) {
		return new JsonToDataGroupConverter(jsonObject);
	}

	private JsonToDataGroupConverter(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public RestDataElement toInstance() {
		try {
			return tryToInstanciate();
		} catch (Exception e) {
			throw new JsonParseException("Error parsing jsonObject", e);
		}
	}

	private RestDataElement tryToInstanciate() {
		String dataId = getDataIdFromJsonObject();
		jsonGroupChildren = jsonObject.getObject(dataId);
		validateJsonData();
		return createDataGroupInstance();
	}

	private String getDataIdFromJsonObject() {
		return jsonObject.keySet().iterator().next();
	}

	private void validateJsonData() {
		validateOnlyOneKeyValuePairAtTopLevel();
		validateGroupOnlyContainsAttributesOrChildren();
	}

	private void validateOnlyOneKeyValuePairAtTopLevel() {
		if (jsonObject.size() != 1) {
			throw new JsonParseException("Group data can only contain one key value pair");
		}
	}

	private void validateGroupOnlyContainsAttributesOrChildren() {
		for (Entry<String, JsonValue> childEntry : jsonGroupChildren.entrySet()) {
			validateChildEntryIsAttributesOrChildren(childEntry);
		}
	}

	private void validateChildEntryIsAttributesOrChildren(Entry<String, JsonValue> childEntry) {
		String key = childEntry.getKey();
		if (!"attributes".equals(key) && !"children".equals(key)) {
			throw new JsonParseException("Group data can only contain attributes or children");
		}
	}

	private RestDataElement createDataGroupInstance() {
		String dataId2 = getDataIdFromJsonObject();
		restDataGroup = RestDataGroup.withDataId(dataId2);
		if (hasAttributes()) {
			addAttributesToGroup();
		}
		if (hasChildren()) {
			addChildrenToGroup();
		}
		return restDataGroup;
	}

	private boolean hasAttributes() {
		return jsonGroupChildren.containsKey("attributes");
	}

	private void addAttributesToGroup() {
		JsonObject attributes = jsonGroupChildren.getObject("attributes");
		for (Entry<String, JsonValue> attributeEntry : attributes.entrySet()) {
			addAttributeToGroup(attributeEntry);
		}
	}

	private void addAttributeToGroup(Entry<String, JsonValue> attributeEntry) {
		String value = ((JsonString) attributeEntry.getValue()).getStringValue();
		restDataGroup.addAttribute(attributeEntry.getKey(), value);
	}

	private boolean hasChildren() {
		return jsonGroupChildren.containsKey("children");
	}

	private void addChildrenToGroup() {
		JsonArray children = jsonGroupChildren.getArray("children");
		for (JsonValue child : children) {
			addChildToGroup(child);
		}
	}

	private void addChildToGroup(JsonValue child) {
		JsonToDataConverterFactoryImp jsonToDataConverterFactoryImp = new JsonToDataConverterFactoryImp();
		JsonObject jsonChildObject = (JsonObject) child;
		JsonToDataConverter childJsonToDataConverter = jsonToDataConverterFactoryImp
				.createForJsonObject(jsonChildObject);
		restDataGroup.addChild(childJsonToDataConverter.toInstance());
	}
}