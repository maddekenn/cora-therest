package epc.therest.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class RestDataGroup implements RestDataElement {

	private final String dataId;
	private Map<String, String> attributes = new HashMap<>();
	private List<RestDataElement> children = new ArrayList<>();
	private Set<ActionLink> actionLinks = new HashSet<>();

	public static RestDataGroup withDataId(String dataId) {
		return new RestDataGroup(dataId);
	}

	private RestDataGroup(String dataId) {
		this.dataId = dataId;
	}

	@Override
	public String getDataId() {
		return dataId;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void addAttribute(String dataId, String value) {
		attributes.put(dataId, value);
	}

	public void addChild(RestDataElement restDataElement) {
		children.add(restDataElement);
	}

	public List<RestDataElement> getChildren() {
		return children;
	}

	public void addActionLink(ActionLink actionLink) {
		actionLinks.add(actionLink);
	}

	public Set<ActionLink> getActionLinks() {
		return actionLinks;
	}
}
