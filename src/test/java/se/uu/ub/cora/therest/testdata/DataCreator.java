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

import se.uu.ub.cora.data.Action;
import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataResourceLink;
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

		DataGroup dataGroup = DataGroup.withNameInData(RECORD_TYPE);
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
		dataGroup.addChild(DataAtomic.withNameInDataAndValue(SEARCH_METADATA_ID, id + "Search"));
		dataGroup.addChild(DataAtomic.withNameInDataAndValue(SEARCH_PRESENTATION_FORM_ID,
				"pg" + idWithCapitalFirst + "SearchForm"));

		dataGroup.addChild(DataAtomic.withNameInDataAndValue(USER_SUPPLIED_ID, userSuppliedId));
		dataGroup.addChild(DataAtomic.withNameInDataAndValue(SELF_PRESENTATION_VIEW_ID,
				"pg" + idWithCapitalFirst + "Self"));
		dataGroup.addChild(DataAtomic.withNameInDataAndValue("abstract", abstractValue));
		if (null != parentId) {
			dataGroup.addChild(createChildWithNameInDataLinkedTypeLinkedId("parentId", "recordType",
					parentId));
			dataGroup.addChild(DataAtomic.withNameInDataAndValue("parentId", parentId));
		}
		return dataGroup;
	}

	private static DataGroup createChildWithNameInDataLinkedTypeLinkedId(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		DataGroup child = DataGroup.withNameInData(nameInData);
		child.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		child.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		return child;
	}

	public static DataGroup createRecordTypeWithIdAndUserSuppliedIdAndParentId(String id,
			String userSuppliedId, String parentId) {
		return createRecordTypeWithIdAndUserSuppliedIdAndAbstractAndParentId(id, userSuppliedId,
				"false", parentId);
	}

	public static DataGroup createRecordInfoWithRecordTypeAndRecordId(String recordType,
			String recordId) {
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		DataGroup type = DataGroup.withNameInData("type");
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", recordType));
		recordInfo.addChild(type);

		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", recordId));
		return recordInfo;
	}

	public static DataGroup createRecordWithNameInDataAndIdAndLinkedRecordId(String nameInData,
			String id, String linkedRecordId) {
		DataGroup record = DataGroup.withNameInData(nameInData);
		DataGroup createRecordInfo = createRecordInfoWithIdAndLinkedRecordId(id, linkedRecordId);
		record.addChild(createRecordInfo);
		return record;
	}

	public static DataGroup createRecordWithNameInDataAndIdAndTypeAndLinkedRecordId(
			String nameInData, String id, String recordType, String linkedRecordId) {
		DataGroup record = DataGroup.withNameInData(nameInData);
		DataGroup createRecordInfo = createRecordInfoWithIdAndTypeAndLinkedRecordId(id, recordType,
				linkedRecordId);
		record.addChild(createRecordInfo);
		return record;
	}

	public static DataGroup createRecordInfoWithLinkedRecordId(String linkedRecordId) {
		DataGroup createRecordInfo = DataGroup.withNameInData("recordInfo");
		DataGroup dataDivider = createDataDividerWithLinkedRecordId(linkedRecordId);
		createRecordInfo.addChild(dataDivider);
		return createRecordInfo;
	}

	public static DataGroup createDataDividerWithLinkedRecordId(String linkedRecordId) {
		DataGroup dataDivider = DataGroup.withNameInData("dataDivider");
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDivider.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		return dataDivider;
	}

	public static DataGroup createRecordWithNameInDataAndLinkedRecordId(String nameInData,
			String linkedRecordId) {
		DataGroup record = DataGroup.withNameInData(nameInData);
		DataGroup createRecordInfo = createRecordInfoWithLinkedRecordId(linkedRecordId);
		record.addChild(createRecordInfo);
		return record;
	}

	public static DataGroup createRecordInfoWithIdAndLinkedRecordId(String id,
			String linkedRecordId) {
		DataGroup createRecordInfo = DataGroup.withNameInData("recordInfo");
		createRecordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));
		DataGroup dataDivider = createDataDividerWithLinkedRecordId(linkedRecordId);
		createRecordInfo.addChild(dataDivider);
		return createRecordInfo;
	}

	public static DataGroup createMetadataGroupWithOneChild() {
		DataGroup spiderDataGroup = DataGroup.withNameInData("metadata");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", "testNewGroup"));
		DataGroup type = DataGroup.withNameInData("type");
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "metadataGroup"));
		recordInfo.addChild(type);

		recordInfo.addChild(createDataDividerWithLinkedRecordId("test"));

		spiderDataGroup.addChild(recordInfo);

		spiderDataGroup.addChild(createChildReference());

		return spiderDataGroup;
	}

	private static DataGroup createChildReference() {
		DataGroup childReferences = DataGroup.withNameInData("childReferences");

		childReferences.addChild(createChildReference("childOne", "1", "1"));

		return childReferences;
	}

	public static DataGroup createRecordInfoWithIdAndTypeAndLinkedRecordId(String id,
			String recordType, String linkedRecordId) {
		DataGroup createRecordInfo = DataGroup.withNameInData("recordInfo");
		createRecordInfo.addChild(DataAtomic.withNameInDataAndValue("id", id));
		DataGroup type = DataGroup.withNameInData("type");
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", recordType));
		createRecordInfo.addChild(type);
		createRecordInfo.addChild(DataAtomic.withNameInDataAndValue("createdBy", "12345"));

		DataGroup dataDivider = createDataDividerWithLinkedRecordId(linkedRecordId);
		createRecordInfo.addChild(dataDivider);
		return createRecordInfo;
	}

	public static DataGroup createMetadataGroupWithTwoChildren() {
		DataGroup spiderDataGroup = DataGroup.withNameInData("metadata");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", "testNewGroup"));
		DataGroup type = DataGroup.withNameInData("type");
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "metadataGroup"));
		recordInfo.addChild(type);

		recordInfo.addChild(createDataDividerWithLinkedRecordId("test"));

		spiderDataGroup.addChild(recordInfo);

		spiderDataGroup.addChild(createChildReferences());

		return spiderDataGroup;
	}

	private static DataGroup createChildReferences() {
		DataGroup childReferences = DataGroup.withNameInData("childReferences");

		childReferences.addChild(createChildReference("childOne", "1", "1"));
		childReferences.addChild(createChildReference("childTwo", "0", "2"));

		return childReferences;
	}

	private static DataGroup createChildReference(String ref, String repeatMin, String repeatMax) {
		DataGroup childReference = DataGroup.withNameInData("childReference");

		DataGroup refGroup = DataGroup.withNameInData("ref");
		refGroup.addAttributeByIdWithValue("type", "group");
		refGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataGroup"));
		refGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", ref));
		childReference.addChild(refGroup);

		DataAtomic repeatMinAtomic = DataAtomic.withNameInDataAndValue("ref", repeatMin);
		childReference.addChild(repeatMinAtomic);

		DataAtomic repeatMaxAtomic = DataAtomic.withNameInDataAndValue("ref", repeatMax);
		childReference.addChild(repeatMaxAtomic);

		return childReference;
	}

	public static DataGroup createMetadataGroupWithThreeChildren() {
		DataGroup spiderDataGroup = createMetadataGroupWithTwoChildren();
		DataGroup childReferences = (DataGroup) spiderDataGroup
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
		DataGroup recordToRecordLink = DataGroup.withNameInData("recordToRecordLink");

		DataGroup from = DataGroup.withNameInData("from");
		from.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "dataWithLinks"));
		from.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "someId"));

		recordToRecordLink.addChild(from);

		DataGroup to = DataGroup.withNameInData("to");
		to.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "toRecordType"));
		to.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "toRecordId"));
		to.addChild(to);

		recordToRecordLink.addChild(to);
		return recordToRecordLink;
	}

	public static DataGroup createMetadataGroupWithCollectionVariableAsChild() {
		DataGroup spiderDataGroup = DataGroup.withNameInData("metadata");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", "testCollectionVar"));
		DataGroup type = DataGroup.withNameInData("type");
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "collectionVariable"));
		recordInfo.addChild(type);

		recordInfo.addChild(createDataDividerWithLinkedRecordId("test"));
		spiderDataGroup.addChild(recordInfo);

		DataGroup refCollection = DataGroup.withNameInData("refCollection");
		refCollection.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordType", "metadataItemCollection"));
		refCollection.addChild(
				DataAtomic.withNameInDataAndValue("linkedRecordId", "testItemCollection"));
		spiderDataGroup.addChild(refCollection);

		return spiderDataGroup;
	}

	public static DataGroup createMetadataGroupWithRecordLinkAsChild() {
		DataGroup spiderDataGroup = DataGroup.withNameInData("metadata");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", "testRecordLink"));
		DataGroup type = DataGroup.withNameInData("type");
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", "recordLink"));
		recordInfo.addChild(type);

		recordInfo.addChild(createDataDividerWithLinkedRecordId("test"));
		spiderDataGroup.addChild(recordInfo);

		return spiderDataGroup;
	}

	public static DataResourceLink createResourceLinkMaster() {
		DataResourceLink master = DataResourceLink.withNameInData("master");
		master.addChild(DataAtomic.withNameInDataAndValue("streamId", "aStreamId"));
		master.addChild(DataAtomic.withNameInDataAndValue("filename", "aFilename"));
		master.addChild(DataAtomic.withNameInDataAndValue("filesize", "1234"));
		master.addChild(DataAtomic.withNameInDataAndValue("mimeType", "application/tiff"));
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
