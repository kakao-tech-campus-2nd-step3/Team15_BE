package kakao.rebit.feed.service;

import kakao.rebit.feed.dto.request.create.CreateFeedRequest;
import kakao.rebit.feed.dto.request.update.UpdateFeedRequest;
import kakao.rebit.feed.dto.response.FeedResponse;
import kakao.rebit.feed.entity.Feed;
import kakao.rebit.feed.mapper.FeedMapper;
import kakao.rebit.feed.repository.FeedRepository;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final MemberService memberService;

    private final FeedMapper feedMapper;

    public FeedService(FeedRepository feedRepository, MemberService memberService,
            FeedMapper feedMapper) {
        this.feedRepository = feedRepository;
        this.memberService = memberService;
        this.feedMapper = feedMapper;
    }

    @Transactional(readOnly = true)
    public Page<FeedResponse> getFeeds(Pageable pageable) {
        return feedRepository.findAll(pageable).map(feedMapper::toFeedResponse);
    }

    @Transactional(readOnly = true)
    public FeedResponse getFeedById(Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("찾는 피드가 존재하지 않습니다."));
        return feedMapper.toFeedResponse(feed);
    }

    @Transactional
    public Long createFeed(MemberResponse memberResponse, CreateFeedRequest feedRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = feedMapper.toFeed(member, feedRequest);
        return feedRepository.save(feed).getId();
    }

    @Transactional
    public void updateFeed(MemberResponse memberResponse, Long feedId,
            UpdateFeedRequest feedRequest) {
        Member member = memberService.findMemberByIdOrThrow(memberResponse.id());
        Feed feed = feedRepository.findByIdAndMember(feedId, member)
                .orElseThrow(() -> new IllegalArgumentException("찾는 피드가 없습니다."));

        Feed updateFeed = feedMapper.toFeed(member, feedRequest);
        feed.updateNonNullFields(updateFeed);
    }

    @Transactional
    public void deleteFeedById(Long feedId) {
        feedRepository.deleteById(feedId);
    }
}
