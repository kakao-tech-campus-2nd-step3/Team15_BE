package kakao.rebit.utils.image;

import kakao.rebit.common.exception.ImageDownloadErrorException;
import kakao.rebit.s3.dto.DownloadImageInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ImageDownloader {

    private final RestClient restClient;

    public ImageDownloader(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * 이미지 URL을 받아서 해당 URL로부터 이미지를 받아오는 메서드
     */
    public DownloadImageInfo downloadImageFromUrl(String imageUrl) {
        try {
            byte[] response = restClient.get()
                    .uri(imageUrl)
                    .retrieve()
                    .toEntity(byte[].class)
                    .getBody();

            return new DownloadImageInfo(response);
        } catch (Exception e) {
            throw ImageDownloadErrorException.EXCEPTION;
        }
    }
}
