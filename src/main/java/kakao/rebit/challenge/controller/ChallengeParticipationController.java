package kakao.rebit.challenge.controller;

import java.net.URI;
import kakao.rebit.challenge.dto.ChallengeParticipationMemberResponse;
import kakao.rebit.challenge.dto.ChallengeParticipationRequest;
import kakao.rebit.challenge.service.ChallengeParticipationService;
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
@RequestMapping("/api/challenges/{challenge-id}/participations")
public class ChallengeParticipationController {

    private final ChallengeParticipationService challengeParticipationService;

    public ChallengeParticipationController(ChallengeParticipationService challengeParticipationService) {
        this.challengeParticipationService = challengeParticipationService;
    }

    @GetMapping
    public ResponseEntity<Page<ChallengeParticipationMemberResponse>> getChallengeParticipations(
            @PathVariable("challenge-id") Long challengeId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(challengeParticipationService.getChallengeParticipationsById(challengeId, pageable));
    }

    @GetMapping("/{participation-id}")
    public ResponseEntity<ChallengeParticipationMemberResponse> getChallengeParticipation(
            @PathVariable("challenge-id") Long challengeId,    // challenge-id는 사용되지 않지만 URL 일관성을 위해 유지
            @PathVariable("participation-id") Long participationId) {
        return ResponseEntity.ok().body(challengeParticipationService.getChallengeParticipationById(participationId));
    }

    @PostMapping
    public ResponseEntity<Void> createChallengeParticipation(
            @MemberInfo MemberResponse memberResponse,
            @PathVariable("challenge-id") Long challengeId,
            @RequestBody ChallengeParticipationRequest participationRequest) {
        Long challengeParticipantId = challengeParticipationService.createChallengeParticipation(memberResponse, challengeId, participationRequest);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId + "/participants/" + challengeParticipantId)).build();
    }

    @DeleteMapping("/{participation-id}")
    public ResponseEntity<Void> cancelParticipation(
            @MemberInfo MemberResponse memberResponse,
            @PathVariable("challenge-id") Long challengeId,    // challenge-id는 사용되지 않지만 URL 일관성을 위해 유지
            @PathVariable("participation-id") Long participationId) {
        challengeParticipationService.cancelParticipation(memberResponse, participationId);
        return ResponseEntity.noContent().build();
    }
}
