package kakao.rebit.book.service;

import kakao.rebit.book.dto.AladinApiResponseListResponse;
import kakao.rebit.book.dto.AladinApiResponseResponse;
import kakao.rebit.book.exception.api.ApiErrorException;
import kakao.rebit.book.exception.book.InvalidIsbnException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AladinApiService {

    @Value("${TTB_KEY}")
    private String TTB_KEY;

    private static final String BASE_API_URL = "http://www.aladin.co.kr/ttb/api";
    private static final String QUERY_PARAMS_FORMAT = "&QueryType=%s&MaxResults=%s&start=%d&SearchTarget=Book&output=js&Version=20131101";

    private static final String ITEM_LOOKUP_ENDPOINT = "/ItemLookUp.aspx";
    private static final String ITEM_SEARCH_ENDPOINT = "/ItemSearch.aspx";

    private final RestClient restClient;

    public AladinApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public AladinApiResponseListResponse searchBooksByTitle(String title, Pageable pageable) {
        String url = buildTitleSearchUrl(title, pageable);
        return executeApiRequest(url, AladinApiResponseListResponse.class);
    }

    private String buildTitleSearchUrl(String title, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber() + 1;  // API에서 1부터 시작하는 페이지 번호를 기대
        return BASE_API_URL + ITEM_SEARCH_ENDPOINT
            + "?ttbkey=" + TTB_KEY
            + "&Query=" + title
            + String.format(QUERY_PARAMS_FORMAT, "Title", pageSize, pageNumber);
    }

    public AladinApiResponseResponse searchBookByIsbn(String isbn) {
        String url = buildIsbnLookupUrl(isbn);
        AladinApiResponseListResponse response = executeApiRequest(url,
            AladinApiResponseListResponse.class);
        return extractFirstBookFromResponse(response);
    }

    private String buildIsbnLookupUrl(String isbn) {
        return BASE_API_URL + ITEM_LOOKUP_ENDPOINT
            + "?ttbkey=" + TTB_KEY
            + "&itemIdType=ISBN"
            + "&ItemId=" + isbn
            + "&output=js"
            + "&Version=20131101";
    }

    private <T> T executeApiRequest(String url, Class<T> responseType) {
        try {
            return restClient.get()
                .uri(url)
                .retrieve()
                .body(responseType);
        } catch (Exception e) {
            throw ApiErrorException.EXCEPTION;
        }
    }

    // API 응답에서 첫 번째 책 정보를 추출
    // 알라딘 api 에서 item필드 아래의 책 정보를 가져와야 정상 작동. 첫 번째 항목에 책의 상세 정보가 위치
    private AladinApiResponseResponse extractFirstBookFromResponse(
        AladinApiResponseListResponse response) {
        if (response.item() == null || response.item().isEmpty()) {
            throw InvalidIsbnException.EXCEPTION;
        }
        return response.item().get(0);
    }
}
