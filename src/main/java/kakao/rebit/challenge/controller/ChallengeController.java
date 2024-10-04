package kakao.rebit.challenge.controller;

import java.net.URI;
import kakao.rebit.challenge.dto.ChallengeRequest;
import kakao.rebit.challenge.dto.ChallengeResponse;
import kakao.rebit.challenge.service.ChallengeService;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }

    @GetMapping
    public ResponseEntity<Page<ChallengeResponse>> getChallenges(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(challengeService.getChallenges(pageable));
    }

    @GetMapping("/{challenge-id}")
    public ResponseEntity<ChallengeResponse> getChallenge(@PathVariable("challenge-id") Long challengeId) {
        return ResponseEntity.ok().body(challengeService.getChallengeById(challengeId));
    }

    @PostMapping
    public ResponseEntity<Void> createChallenge(@RequestBody ChallengeRequest challengeRequest) {
        // 소셜 로그인이 아직 구현되지 않아 임의의 빈 MemberResponse를 넣어줌
        // 추후에 소셜 로그인 구현 후 ArgumentResolver로 MemberResponse를 받아오도록 수정
        MemberResponse memberResponse = new MemberResponse(1L, "testUser", "imageUrl", "bio", Role.ROLE_USER, 10000);
        Long challengeId = challengeService.createChallenge(memberResponse, challengeRequest);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId)).build();
    }

    @DeleteMapping("/{challenge-id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable("challenge-id") Long challengeId) {
        MemberResponse memberResponse = new MemberResponse(1L, "testUser", "imageUrl", "bio", Role.ROLE_USER, 10000);
        challengeService.deleteChallengeById(memberResponse, challengeId);
        return ResponseEntity.noContent().build();
    }
}
