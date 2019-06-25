/*
 * Copyright 2015 Uppsala University Library
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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.therest.data.RestDataAtomic;
import se.uu.ub.cora.therest.data.RestDataAttribute;
import se.uu.ub.cora.therest.data.RestDataGroup;
import se.uu.ub.cora.therest.data.RestDataRecordLink;
import se.uu.ub.cora.therest.data.converter.ConverterException;

public class DataGroupRestToSpiderConverterTest {
	private RestDataGroup restDataGroup;
	private DataGroupRestToSpiderConverter dataGroupRestToSpiderConverter;

	@BeforeMethod
	public void setUp() {
		restDataGroup = RestDataGroup.withNameInData("nameInData");
		dataGroupRestToSpiderConverter = DataGroupRestToSpiderConverter
				.fromRestDataGroup(restDataGroup);
	}

	@Test
	public void testToSpider() {
		DataGroup dataGroup = dataGroupRestToSpiderConverter.toSpider();
		assertEquals(dataGroup.getNameInData(), "nameInData");
	}

	@Test
	public void testToSpiderWithRepeatId() {
		restDataGroup.setRepeatId("34");
		DataGroup dataGroup = dataGroupRestToSpiderConverter.toSpider();
		assertEquals(dataGroup.getNameInData(), "nameInData");
		assertEquals(dataGroup.getRepeatId(), "34");
	}

	@Test
	public void testToSpiderWithAttribute() {
		restDataGroup.addAttributeByIdWithValue("attributeId", "attributeValue");

		DataGroup dataGroup = dataGroupRestToSpiderConverter.toSpider();

		String attributeValue = dataGroup.getAttributes().get("attributeId");
		assertEquals(attributeValue, "attributeValue");
	}

	@Test
	public void testToSpiderWithAtomicChild() {
		restDataGroup.addChild(RestDataAtomic.withNameInDataAndValue("atomicId", "atomicValue"));

		DataGroup dataGroup = dataGroupRestToSpiderConverter.toSpider();

		DataAtomic atomicChild = (DataAtomic) dataGroup.getChildren().get(0);
		assertEquals(atomicChild.getNameInData(), "atomicId");
		assertEquals(atomicChild.getValue(), "atomicValue");
	}

	@Test
	public void testToSpiderWithRecordLinkChild() {
		RestDataRecordLink restDataRecordLink = RestDataRecordLink.withNameInData("aLink");
		RestDataAtomic linkedRecordTypeChild = RestDataAtomic
				.withNameInDataAndValue("linkedRecordType", "someRecordType");
		restDataRecordLink.addChild(linkedRecordTypeChild);

		RestDataAtomic linkedRecordIdChild = RestDataAtomic.withNameInDataAndValue("linkedRecordId",
				"someRecordId");
		restDataRecordLink.addChild(linkedRecordIdChild);
		restDataGroup.addChild(restDataRecordLink);

		DataGroup dataGroup = dataGroupRestToSpiderConverter.toSpider();

		DataGroup dataRecordLink = (DataGroup) dataGroup.getFirstChildWithNameInData("aLink");
		assertEquals(dataRecordLink.getNameInData(), "aLink");

		DataAtomic linkedRecordType = (DataAtomic) dataRecordLink
				.getFirstChildWithNameInData("linkedRecordType");
		DataAtomic linkedRecordId = (DataAtomic) dataRecordLink
				.getFirstChildWithNameInData("linkedRecordId");

		assertEquals(linkedRecordType.getValue(), "someRecordType");
		assertEquals(linkedRecordId.getValue(), "someRecordId");
	}

	@Test
	public void testToSpiderWithGroupChild() {
		restDataGroup.addChild(RestDataGroup.withNameInData("groupId"));

		DataGroup dataGroup = dataGroupRestToSpiderConverter.toSpider();

		DataGroup groupChild = (DataGroup) dataGroup.getChildren().get(0);
		assertEquals(groupChild.getNameInData(), "groupId");
	}

	@Test
	public void testToSpiderWithGroupChildWithAtomicChild() {
		RestDataGroup restGroupChild = RestDataGroup.withNameInData("groupId");
		restGroupChild.addChild(RestDataAtomic.withNameInDataAndValue("grandChildAtomicId",
				"grandChildAtomicValue"));
		restGroupChild.addAttributeByIdWithValue("groupChildAttributeId",
				"groupChildAttributeValue");
		restDataGroup.addChild(restGroupChild);

		DataGroup dataGroup = dataGroupRestToSpiderConverter.toSpider();

		DataGroup groupChild = (DataGroup) dataGroup.getChildren().get(0);
		DataAtomic grandChildAtomic = (DataAtomic) groupChild.getChildren().get(0);
		assertEquals(grandChildAtomic.getNameInData(), "grandChildAtomicId");
		String groupChildAttributeValue = restGroupChild.getAttributes()
				.get("groupChildAttributeId");
		assertEquals(groupChildAttributeValue, "groupChildAttributeValue");
	}

	@Test(expectedExceptions = ConverterException.class)
	public void testToSpiderWithAttributeAsChild() {
		restDataGroup.addChild(RestDataAttribute.withNameInDataAndValue("atomicId", "atomicValue"));

		DataGroup dataGroup = dataGroupRestToSpiderConverter.toSpider();

		DataAtomic atomicChild = (DataAtomic) dataGroup.getChildren().get(0);
		assertEquals(atomicChild.getNameInData(), "atomicId");
		assertEquals(atomicChild.getValue(), "atomicValue");
	}
}
