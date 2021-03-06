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

package se.uu.ub.cora.therest.testdata;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.spider.data.Action;
import se.uu.ub.cora.spider.data.SpiderDataAtomic;
import se.uu.ub.cora.spider.data.SpiderDataGroup;
import se.uu.ub.cora.spider.data.SpiderDataResourceLink;
import se.uu.ub.cora.therest.data.DataAtomicSpy;
import se.uu.ub.cora.therest.data.DataGroupSpy;
import se.uu.ub.cora.therest.data.RestDataAtomic;
import se.uu.ub.cora.therest.data.RestDataGroup;
import se.uu.ub.cora.therest.record.DataRecordLinkCollectorSpy;

public final class DataCreator {
	private static final String SELF_PRESENTATION_VIEW_ID = "selfPresentationViewId";
	private static final String USER_SUPPLIED_ID = "userSuppliedId";
	private static final String SEARCH_PRESENTATION_FORM_ID = "searchPresentationFormId";
	private static final String SEARCH_METADATA_ID = "searchMetadataId";
	private static final String LIST_PRESENTATION_VIEW_ID = "listPresentationViewId";
	private static final String NEW_PRESENTATION_FORM_ID = "newPresentationFormId";
	private static final String PRESENTATION_FORM_ID = "presentationFormId";
	private static final String PRESENTATION_VIEW_ID = "presentationViewId";
	private static final String METADATA_ID = "metadataId";
	private static final String NEW_METADATA_ID = "newMetadataId";
	private static final String RECORD_TYPE = "recordType";

	public static DataGroup createRecordTypeWithIdAndUserSuppliedIdAndAbstract(String id,
			String userSuppliedId, String abstractValue) {
		return createRecordTypeWithIdAndUserSuppliedIdAndAbstractAndParentId(id, userSuppliedId,
				abstractValue, null);
	}

	private static DataGroup createRecordTypeWithIdAndUserSuppliedIdAndAbstractAndParentId(
			String id, String userSuppliedId, String abstractValue, String parentId) {
		String idWithCapitalFirst = id.substring(0, 1).toUpperCase() + id.substring(1);

		DataGroup dataGroup = new DataGroupSpy(RECORD_TYPE);
		dataGroup.addChild(createRecordInfoWithRecordTypeAndRecordId(RECORD_TYPE, id));
		dataGroup.addChild(
				createChildWithNameInDataLinkedTypeLinkedId(METADATA_ID, "metadataGroup", id));
		dataGroup.addChild(createChildWithNameInDataLinkedTypeLinkedId(PRESENTATION_VIEW_ID,
				"presentationGroup", "pg" + idWithCapitalFirst + "View"));
		dataGroup.addChild(createChildWithNameInDataLinkedTypeLinkedId(PRESENTATION_FORM_ID,
				"presentationGroup", "pg" + idWithCapitalFirst + "Form"));
		dataGroup.addChild(createChildWithNameInDataLinkedTypeLinkedId(NEW_METADATA_ID,
				"metadataGroup", id + "New"));
		dataGroup.addChild(createChildWithNameInDataLinkedTypeLinkedId(NEW_PRESENTATION_FORM_ID,
				"presentationGroup", "pg" + idWithCapitalFirst + "FormNew"));
		dataGroup.addChild(createChildWithNameInDataLinkedTypeLinkedId(LIST_PRESENTATION_VIEW_ID,
				"presentationGroup", "pg" + idWithCapitalFirst + "List"));
		dataGroup.addChild(new DataAtomicSpy(SEARCH_METADATA_ID, id + "Search"));
		dataGroup.addChild(new DataAtomicSpy(SEARCH_PRESENTATION_FORM_ID,
				"pg" + idWithCapitalFirst + "SearchForm"));

		dataGroup.addChild(new DataAtomicSpy(USER_SUPPLIED_ID, userSuppliedId));
		dataGroup.addChild(
				new DataAtomicSpy(SELF_PRESENTATION_VIEW_ID, "pg" + idWithCapitalFirst + "Self"));
		dataGroup.addChild(new DataAtomicSpy("abstract", abstractValue));
		if (null != parentId) {
			dataGroup.addChild(createChildWithNameInDataLinkedTypeLinkedId("parentId", "recordType",
					parentId));
			dataGroup.addChild(new DataAtomicSpy("parentId", parentId));
		}
		return dataGroup;
	}

	private static DataGroup createChildWithNameInDataLinkedTypeLinkedId(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		DataGroup child = new DataGroupSpy(nameInData);
		child.addChild(new DataAtomicSpy("linkedRecordType", linkedRecordType));
		child.addChild(new DataAtomicSpy("linkedRecordId", linkedRecordId));
		return child;
	}

	public static DataGroup createRecordTypeWithIdAndUserSuppliedIdAndParentId(String id,
			String userSuppliedId, String parentId) {
		return createRecordTypeWithIdAndUserSuppliedIdAndAbstractAndParentId(id, userSuppliedId,
				"false", parentId);
	}

	public static DataGroup createRecordInfoWithRecordTypeAndRecordId(String recordType,
			String recordId) {
		DataGroup recordInfo = new DataGroupSpy("recordInfo");
		DataGroup type = new DataGroupSpy("type");
		type.addChild(new DataAtomicSpy("linkedRecordType", "recordType"));
		type.addChild(new DataAtomicSpy("linkedRecordId", recordType));
		recordInfo.addChild(type);

		recordInfo.addChild(new DataAtomicSpy("id", recordId));
		return recordInfo;
	}

	public static SpiderDataGroup createRecordWithNameInDataAndIdAndLinkedRecordId(
			String nameInData, String id, String linkedRecordId) {
		SpiderDataGroup record = SpiderDataGroup.withNameInData(nameInData);
		SpiderDataGroup createRecordInfo = createRecordInfoWithIdAndLinkedRecordId(id,
				linkedRecordId);
		record.addChild(createRecordInfo);
		return record;
	}

	public static SpiderDataGroup createRecordWithNameInDataAndIdAndTypeAndLinkedRecordId(
			String nameInData, String id, String recordType, String linkedRecordId) {
		SpiderDataGroup record = SpiderDataGroup.withNameInData(nameInData);
		SpiderDataGroup createRecordInfo = createRecordInfoWithIdAndTypeAndLinkedRecordId(id,
				recordType, linkedRecordId);
		record.addChild(createRecordInfo);
		return record;
	}

	public static SpiderDataGroup createRecordInfoWithLinkedRecordId(String linkedRecordId) {
		SpiderDataGroup createRecordInfo = SpiderDataGroup.withNameInData("recordInfo");
		SpiderDataGroup dataDivider = createDataDividerWithLinkedRecordId(linkedRecordId);
		createRecordInfo.addChild(dataDivider);
		return createRecordInfo;
	}

	public static SpiderDataGroup createDataDividerWithLinkedRecordId(String linkedRecordId) {
		SpiderDataGroup dataDivider = SpiderDataGroup.withNameInData("dataDivider");
		dataDivider.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		return dataDivider;
	}

	public static SpiderDataGroup createRecordWithNameInDataAndLinkedRecordId(String nameInData,
			String linkedRecordId) {
		SpiderDataGroup record = SpiderDataGroup.withNameInData(nameInData);
		SpiderDataGroup createRecordInfo = createRecordInfoWithLinkedRecordId(linkedRecordId);
		record.addChild(createRecordInfo);
		return record;
	}

	public static SpiderDataGroup createRecordInfoWithIdAndLinkedRecordId(String id,
			String linkedRecordId) {
		SpiderDataGroup createRecordInfo = SpiderDataGroup.withNameInData("recordInfo");
		createRecordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));
		SpiderDataGroup dataDivider = createDataDividerWithLinkedRecordId(linkedRecordId);
		createRecordInfo.addChild(dataDivider);
		return createRecordInfo;
	}

	public static SpiderDataGroup createMetadataGroupWithOneChild() {
		SpiderDataGroup spiderDataGroup = SpiderDataGroup.withNameInData("metadata");
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", "testNewGroup"));
		SpiderDataGroup type = SpiderDataGroup.withNameInData("type");
		type.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "metadataGroup"));
		recordInfo.addChild(type);

		recordInfo.addChild(createDataDividerWithLinkedRecordId("test"));

		spiderDataGroup.addChild(recordInfo);

		spiderDataGroup.addChild(createChildReference());

		return spiderDataGroup;
	}

	private static SpiderDataGroup createChildReference() {
		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");

		childReferences.addChild(createChildReference("childOne", "1", "1"));

		return childReferences;
	}

	public static SpiderDataGroup createRecordInfoWithIdAndTypeAndLinkedRecordId(String id,
			String recordType, String linkedRecordId) {
		SpiderDataGroup createRecordInfo = SpiderDataGroup.withNameInData("recordInfo");
		createRecordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", id));
		SpiderDataGroup type = SpiderDataGroup.withNameInData("type");
		type.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", recordType));
		createRecordInfo.addChild(type);
		createRecordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("createdBy", "12345"));

		SpiderDataGroup dataDivider = createDataDividerWithLinkedRecordId(linkedRecordId);
		createRecordInfo.addChild(dataDivider);
		return createRecordInfo;
	}

	public static SpiderDataGroup createMetadataGroupWithTwoChildren() {
		SpiderDataGroup spiderDataGroup = SpiderDataGroup.withNameInData("metadata");
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", "testNewGroup"));
		SpiderDataGroup type = SpiderDataGroup.withNameInData("type");
		type.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "metadataGroup"));
		recordInfo.addChild(type);

		recordInfo.addChild(createDataDividerWithLinkedRecordId("test"));

		spiderDataGroup.addChild(recordInfo);

		spiderDataGroup.addChild(createChildReferences());

		return spiderDataGroup;
	}

	private static SpiderDataGroup createChildReferences() {
		SpiderDataGroup childReferences = SpiderDataGroup.withNameInData("childReferences");

		childReferences.addChild(createChildReference("childOne", "1", "1"));
		childReferences.addChild(createChildReference("childTwo", "0", "2"));

		return childReferences;
	}

	private static SpiderDataGroup createChildReference(String ref, String repeatMin,
			String repeatMax) {
		SpiderDataGroup childReference = SpiderDataGroup.withNameInData("childReference");

		SpiderDataGroup refGroup = SpiderDataGroup.withNameInData("ref");
		refGroup.addAttributeByIdWithValue("type", "group");
		refGroup.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
		refGroup.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", ref));
		childReference.addChild(refGroup);

		SpiderDataAtomic repeatMinAtomic = SpiderDataAtomic.withNameInDataAndValue("ref",
				repeatMin);
		childReference.addChild(repeatMinAtomic);

		SpiderDataAtomic repeatMaxAtomic = SpiderDataAtomic.withNameInDataAndValue("ref",
				repeatMax);
		childReference.addChild(repeatMaxAtomic);

		return childReference;
	}

	public static SpiderDataGroup createMetadataGroupWithThreeChildren() {
		SpiderDataGroup spiderDataGroup = createMetadataGroupWithTwoChildren();
		SpiderDataGroup childReferences = (SpiderDataGroup) spiderDataGroup
				.getFirstChildWithNameInData("childReferences");
		childReferences.addChild(createChildReference("childThree", "1", "1"));

		return spiderDataGroup;
	}

	public static DataRecordLinkCollectorSpy getDataRecordLinkCollectorSpyWithCollectedLinkAdded() {
		DataGroup recordToRecordLink = createDataForRecordToRecordLink();

		DataRecordLinkCollectorSpy linkCollector = new DataRecordLinkCollectorSpy();
		linkCollector.collectedDataLinks.addChild(recordToRecordLink);
		return linkCollector;
	}

	public static DataGroup createDataForRecordToRecordLink() {
		DataGroup recordToRecordLink = new DataGroupSpy("recordToRecordLink");

		DataGroup from = new DataGroupSpy("from");
		from.addChild(new DataAtomicSpy("linkedRecordType", "dataWithLinks"));
		from.addChild(new DataAtomicSpy("linkedRecordId", "someId"));

		recordToRecordLink.addChild(from);

		DataGroup to = new DataGroupSpy("to");
		to.addChild(new DataAtomicSpy("linkedRecordType", "toRecordType"));
		to.addChild(new DataAtomicSpy("linkedRecordId", "toRecordId"));
		to.addChild(to);

		recordToRecordLink.addChild(to);
		return recordToRecordLink;
	}

	public static SpiderDataGroup createMetadataGroupWithCollectionVariableAsChild() {
		SpiderDataGroup spiderDataGroup = SpiderDataGroup.withNameInData("metadata");
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", "testCollectionVar"));
		SpiderDataGroup type = SpiderDataGroup.withNameInData("type");
		type.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "collectionVariable"));
		recordInfo.addChild(type);

		recordInfo.addChild(createDataDividerWithLinkedRecordId("test"));
		spiderDataGroup.addChild(recordInfo);

		SpiderDataGroup refCollection = SpiderDataGroup.withNameInData("refCollection");
		refCollection.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType",
				"metadataItemCollection"));
		refCollection.addChild(
				SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "testItemCollection"));
		spiderDataGroup.addChild(refCollection);

		return spiderDataGroup;
	}

	public static SpiderDataGroup createMetadataGroupWithRecordLinkAsChild() {
		SpiderDataGroup spiderDataGroup = SpiderDataGroup.withNameInData("metadata");
		SpiderDataGroup recordInfo = SpiderDataGroup.withNameInData("recordInfo");
		recordInfo.addChild(SpiderDataAtomic.withNameInDataAndValue("id", "testRecordLink"));
		SpiderDataGroup type = SpiderDataGroup.withNameInData("type");
		type.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(SpiderDataAtomic.withNameInDataAndValue("linkedRecordId", "recordLink"));
		recordInfo.addChild(type);

		recordInfo.addChild(createDataDividerWithLinkedRecordId("test"));
		spiderDataGroup.addChild(recordInfo);

		return spiderDataGroup;
	}

	public static SpiderDataResourceLink createResourceLinkMaster() {
		SpiderDataResourceLink master = SpiderDataResourceLink.withNameInData("master");
		master.addChild(SpiderDataAtomic.withNameInDataAndValue("streamId", "aStreamId"));
		master.addChild(SpiderDataAtomic.withNameInDataAndValue("filename", "aFilename"));
		master.addChild(SpiderDataAtomic.withNameInDataAndValue("filesize", "1234"));
		master.addChild(SpiderDataAtomic.withNameInDataAndValue("mimeType", "application/tiff"));
		master.addAction(Action.READ);
		return master;
	}

	public static RestDataGroup createWorkOrder() {
		RestDataGroup workOrder = RestDataGroup.withNameInData("workOrder");

		RestDataGroup recordTypeLink = RestDataGroup.withNameInData("recordType");
		recordTypeLink
				.addChild(RestDataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		recordTypeLink.addChild(RestDataAtomic.withNameInDataAndValue("linkedRecordId", "person"));
		workOrder.addChild(recordTypeLink);

		workOrder.addChild(RestDataAtomic.withNameInDataAndValue("recordId", "personOne"));
		workOrder.addChild(RestDataAtomic.withNameInDataAndValue("type", "index"));
		return workOrder;
	}
}
