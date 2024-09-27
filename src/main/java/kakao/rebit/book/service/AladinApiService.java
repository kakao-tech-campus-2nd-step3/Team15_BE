package kakao.rebit.book.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AladinApiService {

    private static final String API_URL = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx";
    private static final String API_KEY = "ttbtommy452311545001";  // 알라딘 TTBKey

    public String searchBookByTitle(String title) {
        String url = API_URL + "?ttbkey=" + API_KEY + "&Query=" + title
            + "&QueryType=Title&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    public String searchBookByIsbn(String isbn) {
        String url = API_URL + "?ttbkey=" + API_KEY + "&Query=" + isbn
            + "&QueryType=ISBN&MaxResults=1&start=1&SearchTarget=Book&output=js&Version=20131101";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
