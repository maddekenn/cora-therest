package epc.therest.json.builder.simple;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import epc.therest.json.builder.JsonArrayBuilder;
import epc.therest.json.builder.JsonObjectBuilder;
import epc.therest.json.parser.JsonArray;
import epc.therest.json.parser.JsonObject;
import epc.therest.json.parser.JsonString;

public class SimpleJsonArrayBuilderTest {
	@Test
	public void testAddStringValue() {
		JsonArrayBuilder jsonArrayBuilder = new SimpleJsonArrayBuilder();
		jsonArrayBuilder.add("value");
		JsonArray jsonArray = jsonArrayBuilder.build();
		assertEquals(((JsonString) jsonArray.get(0)).getStringValue(), "value");
	}

	@Test
	public void testAddArrayBuilderValue() {
		JsonArrayBuilder jsonArrayBuilder = new SimpleJsonArrayBuilder();

		JsonArrayBuilder jsonArrayBuilder2 = new SimpleJsonArrayBuilder();

		jsonArrayBuilder2.add("value");
		jsonArrayBuilder.add(jsonArrayBuilder2);

		JsonArray jsonArray = jsonArrayBuilder.build();
		assertEquals(((JsonString) ((JsonArray) jsonArray.get(0)).get(0)).getStringValue(), "value");

	}

	@Test
	public void testAddObjectBuilderValue() {
		JsonArrayBuilder jsonArrayBuilder = new SimpleJsonArrayBuilder();

		JsonObjectBuilder jsonObjectBuilder2 = new SimpleJsonObjectBuilder();

		jsonObjectBuilder2.add("id", "value");
		jsonArrayBuilder.add(jsonObjectBuilder2);

		JsonArray jsonArray = jsonArrayBuilder.build();
		assertEquals(
				((JsonString) ((JsonObject) jsonArray.get(0)).getValue("id")).getStringValue(),
				"value");
	}
}
