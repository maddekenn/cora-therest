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

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;
import se.uu.ub.cora.therest.data.RestDataGroup;
import se.uu.ub.cora.therest.data.RestDataRecord;
import se.uu.ub.cora.therest.data.converter.ConverterException;
import se.uu.ub.cora.therest.data.converter.ConverterInfo;

public final class DataRecordSpiderToRestConverter {

	private DataRecord dataRecord;
	private String baseURL;
	private DataGroup dataGroup;
	private RestDataRecord restDataRecord;
	private String recordId;
	private String recordType;
	private ConverterInfo converterInfo;
	private SpiderToRestConverterFactory converterFactory;

	public static DataRecordSpiderToRestConverter fromDataRecordWithBaseURLAndConverterFactory(
			DataRecord dataRecord, String url, SpiderToRestConverterFactory converterFactory) {
		return new DataRecordSpiderToRestConverter(dataRecord, url, converterFactory);
	}

	private DataRecordSpiderToRestConverter(DataRecord dataRecord, String url,
			SpiderToRestConverterFactory converterFactory) {
		this.dataRecord = dataRecord;
		this.baseURL = url;
		this.converterFactory = converterFactory;
	}

	public RestDataRecord toRest() {
		try {
			return convertToRest();
		} catch (Exception e) {
			throw new ConverterException("No recordInfo found conversion not possible: " + e);
		}
	}

	private RestDataRecord convertToRest() {
		dataGroup = dataRecord.getDataGroup();
		extractIdAndType();
		createConverterInfo();

		convertToRestRecord();

		if (hasActions()) {
			createRestLinks();
		}
		if (hasKeys()) {
			convertKeys();
		}
		return restDataRecord;
	}

	private void extractIdAndType() {
		DataGroup recordInfo = dataGroup.getFirstGroupWithNameInData("recordInfo");
		recordId = recordInfo.getFirstAtomicValueWithNameInData("id");
		DataGroup typeGroup = recordInfo.getFirstGroupWithNameInData("type");
		recordType = typeGroup.getFirstAtomicValueWithNameInData("linkedRecordId");
	}

	private void createConverterInfo() {
		String recordURL = baseURL + String.join("/", recordType, recordId);
		converterInfo = ConverterInfo.withBaseURLAndRecordURLAndTypeAndId(baseURL, recordURL,
				recordType, recordId);
	}

	private void convertToRestRecord() {
		SpiderToRestConverter dataGroupSpiderToRestConverter = converterFactory
				.factorForDataGroupWithConverterInfo(dataGroup, converterInfo);
		RestDataGroup restDataGroup = dataGroupSpiderToRestConverter.toRest();
		restDataRecord = RestDataRecord.withRestDataGroup(restDataGroup);
	}

	private boolean hasActions() {
		return !dataRecord.getActions().isEmpty();
	}

	private void createRestLinks() {
		ActionSpiderToRestConverter actionSpiderToRestConverter = converterFactory
				.factorForActionsUsingConverterInfoAndDataGroup(dataRecord.getActions(),
						converterInfo, dataGroup);

		restDataRecord.setActionLinks(actionSpiderToRestConverter.toRest());
	}

	private boolean hasKeys() {
		return !dataRecord.getKeys().isEmpty();
	}

	private void convertKeys() {
		for (String string : dataRecord.getKeys()) {
			restDataRecord.addKey(string);
		}
	}
}
