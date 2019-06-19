package se.uu.ub.cora.therest.record;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataList;
import se.uu.ub.cora.spider.authentication.AuthenticationException;
import se.uu.ub.cora.spider.authorization.AuthorizationException;
import se.uu.ub.cora.spider.record.DataException;
import se.uu.ub.cora.spider.record.SpiderRecordSearcher;
import se.uu.ub.cora.spider.record.storage.RecordNotFoundException;

public class SpiderRecordSearcherSpy implements SpiderRecordSearcher {

	public String authToken;
	public String searchId;
	public DataGroup searchData;

	@Override
	public DataList search(String authToken, String searchId, DataGroup searchData) {
		this.authToken = authToken;
		this.searchId = searchId;
		this.searchData = searchData;
		possiblyThrowException(authToken, searchId);
		DataList searchResult = DataList.withContainDataOfType("mix");
		searchResult.setFromNo("0");
		searchResult.setToNo("1");
		searchResult.setTotalNo("1");
		return searchResult;
	}

	private void possiblyThrowException(String authToken, String searchId) {
		if ("nonExistingToken".equals(authToken)) {
			throw new AuthenticationException("User not authenticated");
		}
		if ("dummyNonAuthorizedToken".equals(authToken)) {
			throw new AuthorizationException("User not authorized");
		}
		if ("aSearchId_NOT_FOUND".equals(searchId)) {
			throw new RecordNotFoundException("Record does not exist");
		}
		if ("aSearchId_INVALID_DATA".equals(searchId)) {
			throw new DataException("SearchData is invalid");
		}
	}

}
