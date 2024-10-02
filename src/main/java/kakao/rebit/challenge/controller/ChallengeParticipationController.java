package kakao.rebit.challenge.controller;

import java.net.URI;
import kakao.rebit.challenge.dto.ChallengeParticipationRequest;
import kakao.rebit.challenge.dto.ChallengeParticipationMemberResponse;
import kakao.rebit.challenge.service.ChallengeParticipationService;
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
@RequestMapping("/api/challenges/{challenge-id}/participants")
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
            @PathVariable("participation-id") Long participantId) {
        return ResponseEntity.ok().body(challengeParticipationService.getChallengeParticipationById(participantId));
    }

    @PostMapping
    public ResponseEntity<Void> createChallengeParticipation(
            @PathVariable("challenge-id") Long challengeId,
            @RequestBody ChallengeParticipationRequest participationRequest) {
        MemberResponse memberResponse = new MemberResponse(1L, "testUser", "imageUrl", "bio", Role.ROLE_USER, 10000);
        Long challengeParticipantId = challengeParticipationService.createChallengeParticipation(memberResponse, challengeId, participationRequest);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId + "/participants/" + challengeParticipantId)).build();
    }

    @DeleteMapping("/{participation-id}")
    public ResponseEntity<Void> cancelParticipation(
            @PathVariable("challenge-id") Long challengeId,    // challenge-id는 사용되지 않지만 URL 일관성을 위해 유지
            @PathVariable("participation-id") Long participantId) {
        challengeParticipationService.cancelParticipation(participantId);
        return ResponseEntity.noContent().build();
    }
}
