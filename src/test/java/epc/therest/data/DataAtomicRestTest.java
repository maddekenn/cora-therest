package epc.therest.data;

import org.testng.Assert;
import org.testng.annotations.Test;

import epc.metadataformat.data.DataAtomic;

public class DataAtomicRestTest {
	@Test
	public void testInit() {
		DataAtomicRest dataAtomicRest = DataAtomicRest.withDataIdAndValue("dataId", "value");
		Assert.assertEquals(dataAtomicRest.getDataId(), "dataId");
		Assert.assertEquals(dataAtomicRest.getValue(), "value");
	}

	@Test
	public void testCreateFromDataAtomic() {
		DataAtomic dataAtomic = DataAtomic.withDataIdAndValue("dataId", "value");
		DataAtomicRest dataAtomicRest = DataAtomicRest.fromDataAtomic(dataAtomic);
		Assert.assertEquals(dataAtomicRest.getDataId(), "dataId");
		Assert.assertEquals(dataAtomicRest.getValue(), "value");
	}
}
