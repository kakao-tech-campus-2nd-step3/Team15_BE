package kakao.rebit.book.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AladinApiService {

    private static final String API_URL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String API_KEY = "ttbtommy452311545001";  // 알라딘 TTBKey
    private static final String API_QUERY_PARAMS = "&QueryType=%s&MaxResults=%s&start=1&SearchTarget=Book&output=js&Version=20131101";

    private String getApiUrlByQuery(String query, String queryType, Integer maxResults) {
        return API_URL + "?ttbkey=" + API_KEY + "&Query=" + query
            + String.format(API_QUERY_PARAMS, queryType, maxResults);
    }

    public String searchBookByTitle(String title) {
        String url = getApiUrlByQuery(title, "Title", 10);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String searchBookByIsbn(String isbn) {
        String url = getApiUrlByQuery(isbn, "ISBN", 1);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
