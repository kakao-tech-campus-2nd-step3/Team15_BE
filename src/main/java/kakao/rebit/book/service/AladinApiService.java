package kakao.rebit.book.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AladinApiService {

    private static final String BASE_API_URL = "http://www.aladin.co.kr/ttb/api";
    private static final String TTB_KEY = "ttbtommy452311545001";  // 알라딘 TTBKey
    private static final String QUERY_PARAMS_FORMAT = "&QueryType=%s&MaxResults=%s&start=1&SearchTarget=Book&output=js&Version=20131101";
    private static final String ITEM_LOOKUP_ENDPOINT = "/ItemLookUp.aspx";
    private static final String ITEM_SEARCH_ENDPOINT = "/ItemSearch.aspx";


    private String buildTitleSearchUrl(String title, int maxResults) {
        return BASE_API_URL + ITEM_SEARCH_ENDPOINT
            + "?ttbkey=" + TTB_KEY
            + "&Query=" + title
            + String.format(QUERY_PARAMS_FORMAT, "Title", maxResults);
    }

    private String buildIsbnLookupUrl(String isbn) {
        return BASE_API_URL + ITEM_LOOKUP_ENDPOINT
            + "?ttbkey=" + TTB_KEY
            + "&itemIdType=ISBN"
            + "&ItemId=" + isbn
            + "&output=js&Version=20131101";
    }

    private String sendRequest(String query, String queryType, int maxResults) {
        String url = getApiUrlByQuery(query, queryType, maxResults);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
    public String searchBookByTitle(String title) {
        return sendRequest(title, "Title", 10);
    }

    public String searchBookByIsbn(String isbn) {
        return sendRequest(isbn, "ISBN", 1);
    }
}
