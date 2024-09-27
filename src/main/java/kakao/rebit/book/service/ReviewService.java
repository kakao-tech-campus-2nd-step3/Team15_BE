package kakao.rebit.book.service;

import kakao.rebit.book.entity.Review;
import kakao.rebit.book.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getBriefReviews(String isbn) {
        return reviewRepository.findByIsbn(isbn);
    }
}
