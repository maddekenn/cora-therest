package se.uu.ub.cora.therest.record;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;
import se.uu.ub.cora.spider.authorization.AuthorizationException;
import se.uu.ub.cora.spider.record.SpiderRecordValidator;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class SpiderRecordValidatorSpy implements SpiderRecordValidator {

	public String authToken;
	public String recordType;
	public DataGroup spiderDataGroup;
	public DataGroup recordToValidate;
	public DataGroup validationRecord;

	@Override
	public DataRecord validateRecord(String authToken, String recordType,
			DataGroup validationRecord, DataGroup recordToValidate) {
		this.authToken = authToken;
		this.recordType = recordType;
		this.validationRecord = validationRecord;
		this.spiderDataGroup = validationRecord;
		this.recordToValidate = recordToValidate;

		if ("dummyNonAuthorizedToken".equals(authToken)) {
			throw new AuthorizationException("not authorized");
		}
		if ("recordType_NON_EXISTING".equals(recordToValidate.getNameInData())) {
			throw new RecordNotFoundException("no record exist with type " + recordType);
		}
		DataGroup validationResult = createValidationResult(recordType);
		return DataRecord.withDataGroup(validationResult);
	}

	private DataGroup createValidationResult(String recordType) {
		DataGroup validationResult = DataGroup.withNameInData("validationResult");
		validationResult.addChild(DataAtomic.withNameInDataAndValue("valid", "true"));
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("id", "someSpyId"));
		DataGroup type = DataGroup.withNameInData("type");
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		type.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", recordType));
		recordInfo.addChild(type);
		validationResult.addChild(recordInfo);
		return validationResult;
	}

}
