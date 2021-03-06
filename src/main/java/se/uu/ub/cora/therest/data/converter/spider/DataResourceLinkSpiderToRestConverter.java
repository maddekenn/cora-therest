/*
 * Copyright 2015, 2016, 2019 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.therest.data.converter.spider;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.uu.ub.cora.spider.data.Action;
import se.uu.ub.cora.spider.data.SpiderDataResourceLink;
import se.uu.ub.cora.therest.data.ActionLink;
import se.uu.ub.cora.therest.data.RestDataGroup;
import se.uu.ub.cora.therest.data.RestDataResourceLink;
import se.uu.ub.cora.therest.data.converter.ConverterInfo;

public final class DataResourceLinkSpiderToRestConverter extends DataGroupSpiderToRestConverter {
	private SpiderDataResourceLink spiderDataResourceLink;
	private RestDataResourceLink restDataResourceLink;

	public static DataResourceLinkSpiderToRestConverter fromSpiderDataResourceLinkWithConverterInfo(
			SpiderDataResourceLink spiderDataResourceLink, ConverterInfo converterInfo) {
		return new DataResourceLinkSpiderToRestConverter(spiderDataResourceLink, converterInfo);
	}

	private DataResourceLinkSpiderToRestConverter(SpiderDataResourceLink spiderDataResourceLink,
			ConverterInfo converterInfo) {
		super(spiderDataResourceLink, converterInfo);
		this.spiderDataResourceLink = spiderDataResourceLink;
	}

	@Override
	protected RestDataGroup createNewRest() {
		return RestDataResourceLink.withNameInData(spiderDataGroup.getNameInData());
	}

	@Override
	public RestDataResourceLink toRest() {
		restDataResourceLink = (RestDataResourceLink) super.toRest();

		createRestLinks();
		return restDataResourceLink;
	}

	private void createRestLinks() {
		String url = convertInfo.recordURL + "/" + spiderDataResourceLink.getNameInData();
		String mimeType = spiderDataResourceLink.extractAtomicValue("mimeType");
		List<Action> actions = spiderDataResourceLink.getActions();

		Map<String, ActionLink> actionLinks = new LinkedHashMap<>(actions.size());
		for (Action action : actions) {
			createRestLink(url, mimeType, actionLinks, action);
		}
		restDataResourceLink.setActionLinks(actionLinks);
	}

	private void createRestLink(String url, String mimeType, Map<String, ActionLink> actionLinks,
			Action action) {
		ActionLink actionLink = ActionLink.withAction(action);
		actionLink.setRequestMethod("GET");
		actionLink.setURL(url);
		actionLink.setAccept(mimeType);

		actionLinks.put("read", actionLink);
	}
}
