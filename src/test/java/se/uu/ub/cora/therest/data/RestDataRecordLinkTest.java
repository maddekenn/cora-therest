/*
 * Copyright 2016, 2019 Uppsala University Library
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
package se.uu.ub.cora.therest.data;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

<<<<<<< HEAD
import se.uu.ub.cora.data.Action;
=======
import se.uu.ub.cora.spider.data.Action;
>>>>>>> refs/remotes/origin/master

public class RestDataRecordLinkTest {
	private RestDataRecordLink recordLink;

	@BeforeMethod
	public void setUp() {
		recordLink = RestDataRecordLink.withNameInData("aNameInData");
		RestDataAtomic linkedRecordType = RestDataAtomic.withNameInDataAndValue("linkedRecordType",
				"aLinkedRecordType");
		recordLink.addChild(linkedRecordType);

		RestDataAtomic linkedRecordId = RestDataAtomic.withNameInDataAndValue("linkedRecordId",
				"aLinkedRecordId");
		recordLink.addChild(linkedRecordId);
	}

	@Test
	public void testInit() {
		assertEquals(recordLink.getChildren().size(), 2);
		assertNotNull(recordLink.getFirstChildWithNameInData("linkedRecordType"));
		assertTrue(recordLink.containsChildWithNameInData("linkedRecordId"));
	}

	@Test
	public void testWithActionLinks() {
		ActionLink actionLink = ActionLink.withAction(Action.READ);
		recordLink.addActionLink("read", actionLink);
		assertEquals(recordLink.getActionLink("read"), actionLink);
		assertEquals(recordLink.getActionLinks().get("read"), actionLink);
		assertNull(recordLink.getActionLink("notAnAction"));
	}

	@Test
	public void testWithRepeatId() {
		recordLink.setRepeatId("x2");
		assertEquals(recordLink.getRepeatId(), "x2");
	}
}
