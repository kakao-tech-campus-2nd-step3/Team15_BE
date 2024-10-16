package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.response.LikesMemberResponse;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.entity.Likes;
import kakao.rebit.feed.exception.likes.FindNotAuthorizedException;
import kakao.rebit.feed.exception.likes.LikesAlreadyPressedException;
import kakao.rebit.feed.exception.likes.LikesNotPressedException;
import kakao.rebit.feed.repository.LikesRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberService memberService;
    private final FeedService feedService;

    public LikesService(LikesRepository likesRepository, MemberService memberService,
            FeedService feedService) {
        this.likesRepository = likesRepository;
        this.memberService = memberService;
        this.feedService = feedService;
    }

    @Transactional(readOnly = true)
    public Page<LikesMemberResponse> getLikesMembers(MemberResponse memberResponse, Long feedId,
            Pageable pageable) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = feedService.findFeedByIdOrThrow(feedId);

        if (!feed.isWrittenBy(member)) {
            throw FindNotAuthorizedException.EXCEPTION;
        }
        return likesRepository.findAllByFeedWithMember(feed, pageable)
                .map(this::toLikesMemberResponse);
    }

    @Transactional
    public Long createLikes(MemberResponse memberResponse, Long feedId) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = feedService.findFeedByIdOrThrow(feedId);

        if (likesRepository.existsByMemberAndFeed(member, feed)) {
            throw LikesAlreadyPressedException.EXCEPTION;
        }

        return likesRepository.save(createLikes(member, feed)).getId();
    }

    @Transactional
    public void deleteLikes(MemberResponse memberResponse, Long feedId) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = feedService.findFeedByIdOrThrow(feedId);

        if (!likesRepository.existsByMemberAndFeed(member, feed)) {
            throw LikesNotPressedException.EXCEPTION;
        }

        likesRepository.deleteByMemberAndFeed(member, feed);
    }

    private LikesMemberResponse toLikesMemberResponse(Likes likes) {
        Member member = likes.getMember();
        return new LikesMemberResponse(member.getId(), member.getNickname());
    }

    private Likes createLikes(Member member, Feed feed) {
        return new Likes(member, feed);
    }
}
