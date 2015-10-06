package se.uu.ub.cora.therest.data;

public final class RestDataAttribute implements RestDataElement {

	private String nameInData;
	private String value;

	public static RestDataAttribute withNameInDataAndValue(String nameInData, String value) {
		return new RestDataAttribute(nameInData, value);
	}

	private RestDataAttribute(String nameInData, String value) {
		this.nameInData = nameInData;
		this.value = value;
	}

	@Override
	public String getNameInData() {
		return nameInData;
	}

	public String getValue() {
		return value;
	}

}
