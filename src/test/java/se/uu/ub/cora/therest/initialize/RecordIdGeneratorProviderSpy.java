package se.uu.ub.cora.therest.initialize;

import java.util.Map;

import se.uu.ub.cora.storage.RecordIdGenerator;
import se.uu.ub.cora.storage.RecordIdGeneratorProvider;

public class RecordIdGeneratorProviderSpy implements RecordIdGeneratorProvider {
	public Map<String, String> initInfo;
	boolean started = false;

	@Override
	public int getOrderToSelectImplementionsBy() {
		return 0;
	}

	@Override
	public void startUsingInitInfo(Map<String, String> initInfo) {
		started = true;
		this.initInfo = initInfo;
	}

	@Override
	public RecordIdGenerator getRecordIdGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

}
