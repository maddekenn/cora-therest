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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.Action;
import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;
import se.uu.ub.cora.therest.data.RestDataRecord;
import se.uu.ub.cora.therest.data.converter.ConverterException;
import se.uu.ub.cora.therest.testdata.DataCreator;

public class DataRecordSpiderToRestConverterTest {
	private String baseURL = "http://localhost:8080/therest/rest/record/";
	private DataGroup dataGroup;
	private DataRecord dataRecord;
	private DataRecordSpiderToRestConverter dataRecordSpiderToRestConverter;
	private SpiderToRestConverterFactorySpy converterFactory;

	@BeforeMethod
	public void setUp() {
		dataGroup = DataGroup.withNameInData("someNameInData");
		dataRecord = DataRecord.withDataGroup(dataGroup);
		converterFactory = new SpiderToRestConverterFactorySpy();
		dataRecordSpiderToRestConverter = DataRecordSpiderToRestConverter
				.fromDataRecordWithBaseURLAndConverterFactory(dataRecord, baseURL,
						converterFactory);
	}

	@Test
	public void testToRest() {
		dataGroup.addChild(createRecordInfo("place"));
		dataRecordSpiderToRestConverter.toRest();

		assertSame(converterFactory.dataGroups.get(0), dataGroup);
		assertSame(converterFactory.converterInfos.get(0).baseURL, baseURL);
		assertEquals(converterFactory.converterInfos.get(0).recordURL,
				baseURL + "place/place:0001");

		SpiderToRestConverterSpy converter = converterFactory.factoredSpiderToRestConverters.get(0);
		assertTrue(converter.toRestWasCalled);
	}

	@Test(expectedExceptions = ConverterException.class)
	public void testToRestWithActionLinkNoRecordInfoButOtherChild() {
		dataGroup.addChild(DataAtomic.withNameInDataAndValue("type", "place"));
		dataRecord.addAction(Action.READ);
		dataRecordSpiderToRestConverter.toRest();
	}

	@Test(expectedExceptions = ConverterException.class)
	public void testToRestWithActionLinkNoRecordInfo() {
		dataRecord.addAction(Action.READ);
		dataRecordSpiderToRestConverter.toRest();
	}

	@Test(expectedExceptions = ConverterException.class)
	public void testToRestWithActionLinkNoId() {
		dataRecord.addAction(Action.READ);

		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		DataGroup type = DataGroup.withNameInData("type");
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "place"));
		recordInfo.addChild(type);

		recordInfo.addChild(DataAtomic.withNameInDataAndValue("createdBy", "userId"));
		dataGroup.addChild(recordInfo);

		dataRecordSpiderToRestConverter.toRest();
	}

	@Test(expectedExceptions = ConverterException.class)
	public void testToRestWithActionLinkNoType() {
		dataRecord.addAction(Action.READ);

		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", "place:0001"));
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("createdBy", "userId"));
		dataGroup.addChild(recordInfo);

		dataRecordSpiderToRestConverter.toRest();
	}

	@Test
	public void testToRestActionLinksSentToConverter() {
		dataRecord.addAction(Action.READ);
		dataRecord.addAction(Action.CREATE);
		dataRecord.addAction(Action.DELETE);

		dataGroup.addChild(createRecordInfo("place"));

		RestDataRecord restDataRecord = dataRecordSpiderToRestConverter.toRest();

		assertSame(converterFactory.dataGroups.get(0), dataGroup);
		assertSame(converterFactory.dataGroups.get(1), dataGroup);
		assertSame(converterFactory.converterInfos.get(1).baseURL, baseURL);
		assertEquals(converterFactory.converterInfos.get(1).recordURL,
				baseURL + "place/place:0001");

		assertTrue(converterFactory.addedActions.contains(Action.READ));
		assertTrue(converterFactory.addedActions.contains(Action.CREATE));
		assertTrue(converterFactory.addedActions.contains(Action.DELETE));
		assertEquals(converterFactory.addedActions.size(), 3);

		ActionSpiderToRestConverterSpy factoredActionsConverter = converterFactory.factoredActionsToRestConverters
				.get(0);
		assertTrue(factoredActionsConverter.toRestWasCalled);

		assertEquals(factoredActionsConverter.actionLinks, restDataRecord.getActionLinks());
	}

	private DataGroup createRecordInfo(String type) {
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", "place:0001"));
		DataGroup typeGroup = DataGroup.withNameInData("type");
		typeGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		typeGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", type));
		recordInfo.addChild(typeGroup);
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("createdBy", "userId"));
		return recordInfo;
	}

	@Test
	public void testToRestWithActionLinkSEARCHWhenRecordTypeIsRecordType() {
		dataRecord.addAction(Action.READ);

		dataGroup.addChild(createRecordInfo("recordType"));

		RestDataRecord restDataRecord = dataRecordSpiderToRestConverter.toRest();

		assertSame(converterFactory.dataGroups.get(0), dataGroup);
		assertSame(converterFactory.dataGroups.get(1), dataGroup);
		assertSame(converterFactory.converterInfos.get(1).baseURL, baseURL);
		assertEquals(converterFactory.converterInfos.get(1).recordURL,
				baseURL + "recordType/place:0001");

		assertTrue(converterFactory.addedActions.contains(Action.READ));
		assertEquals(converterFactory.addedActions.size(), 1);

		ActionSpiderToRestConverterSpy factoredActionsConverter = converterFactory.factoredActionsToRestConverters
				.get(0);
		assertTrue(factoredActionsConverter.toRestWasCalled);

		assertEquals(factoredActionsConverter.actionLinks, restDataRecord.getActionLinks());
	}

	@Test
	public void testToRestWithResourceLink() {
		dataGroup.addChild(createRecordInfo("place"));
		dataGroup.addChild(DataCreator.createResourceLinkMaster());

		RestDataRecord restDataRecord = dataRecordSpiderToRestConverter.toRest();

		assertSame(converterFactory.dataGroups.get(0), dataGroup);
		assertSame(converterFactory.converterInfos.get(0).baseURL, baseURL);
		assertEquals(converterFactory.converterInfos.get(0).recordURL,
				baseURL + "place/place:0001");

		SpiderToRestConverterSpy factoredConverter = converterFactory.factoredSpiderToRestConverters
				.get(0);
		assertTrue(factoredConverter.toRestWasCalled);
		assertSame(restDataRecord.getRestDataGroup(), factoredConverter.restDataGroup);

	}

	@Test
	public void testToRestWithKeys() {
		dataGroup.addChild(createRecordInfo("place"));

		dataRecord.addKey("KEY1");

		RestDataRecord restDataRecord = dataRecordSpiderToRestConverter.toRest();
		String key = restDataRecord.getKeys().iterator().next();
		assertEquals(key, "KEY1");

	}
}
