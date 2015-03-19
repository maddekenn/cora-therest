package epc.therest.json.parser.javax;

import epc.therest.json.parser.JsonString;
import epc.therest.json.parser.JsonValueType;

public final class JavaxJsonString implements JsonString {

	private javax.json.JsonString javaxJsonString;

	public static JavaxJsonString usingJavaxJsonString(javax.json.JsonString javaxJsonString) {
		return new JavaxJsonString(javaxJsonString);
	}

	private JavaxJsonString(javax.json.JsonString javaxJsonString) {
		this.javaxJsonString = javaxJsonString;
	}

	@Override
	public JsonValueType getValueType() {
		return JsonValueType.STRING;
	}

	@Override
	public String getStringValue() {
		return javaxJsonString.getString();
	}

}