package kakao.rebit.feed.service;

import java.util.Set;
import kakao.rebit.feed.dto.response.LikesMemberResponse;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.entity.Likes;
import kakao.rebit.feed.exception.likes.LikesAlreadyPressedException;
import kakao.rebit.feed.exception.likes.LikesNotPressedException;
import kakao.rebit.feed.repository.LikesRepository;
import kakao.rebit.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikesService {

    private final LikesRepository likesRepository;

    public LikesService(LikesRepository likesRepository) {
        this.likesRepository = likesRepository;
    }

    @Transactional(readOnly = true)
    public Page<LikesMemberResponse> findLikesMembers(Feed feed, Pageable pageable) {
        return likesRepository.findAllByFeedWithMember(feed, pageable).map(this::toLikesMemberResponse);
    }

    @Transactional
    public Long createLikes(Member member, Feed feed) {
        if (likesRepository.existsByMemberAndFeed(member, feed)) {
            throw LikesAlreadyPressedException.EXCEPTION;
        }

        return likesRepository.save(Likes.of(member, feed)).getId();
    }

    @Transactional
    public void deleteLikes(Member member, Feed feed) {
        if (!likesRepository.existsByMemberAndFeed(member, feed)) {
            throw LikesNotPressedException.EXCEPTION;
        }

        likesRepository.deleteByMemberAndFeed(member, feed);
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Member viewer, Feed feed) {
        return likesRepository.existsByMemberAndFeed(viewer, feed);
    }

    @Transactional(readOnly = true)
    public Set<Long> getLikedFeedIdsByMember(Member member) {
        return likesRepository.findFeedIdsByMember(member);
    }

    public boolean isLikedBySet(Set<Long> feedIds, Feed feed) {
        return feedIds.contains(feed.getId());
    }

    private LikesMemberResponse toLikesMemberResponse(Likes likes) {
        Member member = likes.getMember();
        return new LikesMemberResponse(member.getId(), member.getNickname());
    }
}
