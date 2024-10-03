package kakao.rebit.book.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kakao.rebit.book.dto.AladinApiResponseListResponse;
import kakao.rebit.book.dto.AladinApiResponseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AladinApiService {

    private static final String BASE_API_URL = "http://www.aladin.co.kr/ttb/api";
    @Value("${TTB_KEY}")
    private String TTB_KEY;
    private static final String QUERY_PARAMS_FORMAT = "&QueryType=%s&MaxResults=%s&start=1&SearchTarget=Book&output=js&Version=20131101";
    private static final String ITEM_LOOKUP_ENDPOINT = "/ItemLookUp.aspx";
    private static final String ITEM_SEARCH_ENDPOINT = "/ItemSearch.aspx";

    private final RestClient restClient;

    public AladinApiService(RestClient restClient) {
        this.restClient = restClient;
    }

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

    private <T> T executeApiRequest(String url, Class<T> responseType) {
        return restClient.get()
            .uri(url)
            .retrieve()
            .body(responseType);
    }

    public AladinApiResponseListResponse searchBooksByTitle(String title) {
        String url = buildTitleSearchUrl(title, 10);
        return executeApiRequest(url, AladinApiResponseListResponse.class);
    }

    public AladinApiResponseResponse searchBookByIsbn(String isbn) {
        String url = buildIsbnLookupUrl(isbn);
        AladinApiResponseListResponse response = executeApiRequest(url, AladinApiResponseListResponse.class);
        return extractFirstBookFromResponse(response);
    }


    // API 응답에서 첫 번째 책 정보를 추출
    // 알라딘 api 에서 item필드 아래의 책 정보를 가져와야 정상 작동. 첫 번째 항목에 책의 상세 정보가 위치
    private AladinApiResponseResponse extractFirstBookFromResponse(
        AladinApiResponseListResponse response) {
        if (response.item() == null || response.item().isEmpty()) {
            throw new RuntimeException("API 응답에서 도서를 찾을 수 없습니다.");
        }

        // 첫 번째 책 정보 추출
        AladinApiResponseResponse bookResponse = response.item().get(0);
        System.out.println("API에서 받은 책 정보: " + bookResponse);
        return bookResponse;
    }
}
