package kakao.rebit.wishlist.dto.response;

public record BookWishlistResponse(
        String isbn,
        boolean isWishListed
) {

}
