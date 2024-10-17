package kakao.rebit.utils.file;

public class FileUtil {

    /**
     * 전체 파일 이름에서 확장자를 제외한 파일 이름 가져오기
     */
    public static String extractFilename(String fullFilename){
        return fullFilename.substring(0, fullFilename.lastIndexOf("."));
    }

    /**
     * 전체 파일 이름에서 확장자 가져오기
     */
    public static String extractExtension(String fullFilename){
        return fullFilename.substring(fullFilename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 이미지 url로부터 전체 파일 이름 가져오기
     */
    public static String getFilenameFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
    }
}
