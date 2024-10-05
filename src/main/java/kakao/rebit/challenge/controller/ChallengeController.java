package kakao.rebit.challenge.controller;

import java.net.URI;
import kakao.rebit.challenge.dto.ChallengeRequest;
import kakao.rebit.challenge.dto.ChallengeResponse;
import kakao.rebit.challenge.service.ChallengeService;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberResponse;
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
    public ResponseEntity<Void> createChallenge(@MemberInfo MemberResponse memberResponse, @RequestBody ChallengeRequest challengeRequest) {
        Long challengeId = challengeService.createChallenge(memberResponse, challengeRequest);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId)).build();
    }

    @DeleteMapping("/{challenge-id}")
    public ResponseEntity<Void> deleteChallenge(@MemberInfo MemberResponse memberResponse, @PathVariable("challenge-id") Long challengeId) {
       challengeService.deleteChallengeById(memberResponse, challengeId);
        return ResponseEntity.noContent().build();
    }
}
