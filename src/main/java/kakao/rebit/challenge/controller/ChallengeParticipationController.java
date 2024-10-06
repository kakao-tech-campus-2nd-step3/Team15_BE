package kakao.rebit.challenge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "챌린지 참여 API", description = "챌린지 참여 관련 API")
public class ChallengeParticipationController {

    private final ChallengeParticipationService challengeParticipationService;

    public ChallengeParticipationController(ChallengeParticipationService challengeParticipationService) {
        this.challengeParticipationService = challengeParticipationService;
    }

    @Operation(summary = "챌린지 참여자 목록 조회", description = "챌린지 참여자 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<ChallengeParticipationMemberResponse>> getChallengeParticipations(
            @PathVariable("challenge-id") Long challengeId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(challengeParticipationService.getChallengeParticipationsById(challengeId, pageable));
    }

    @Operation(summary = "챌린지 참여자 조회", description = "챌린지 참여자를 조회합니다.")
    @GetMapping("/{participation-id}")
    public ResponseEntity<ChallengeParticipationMemberResponse> getChallengeParticipation(
            @PathVariable("challenge-id") Long challengeId,    // challenge-id는 사용되지 않지만 URL 일관성을 위해 유지
            @PathVariable("participation-id") Long participationId) {
        return ResponseEntity.ok().body(challengeParticipationService.getChallengeParticipationById(participationId));
    }

    @Operation(summary = "챌린지 참여", description = "챌린지를 참여합니다.")
    @PostMapping
    public ResponseEntity<Void> createChallengeParticipation(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("challenge-id") Long challengeId,
            @RequestBody ChallengeParticipationRequest participationRequest) {
        Long challengeParticipantId = challengeParticipationService.createChallengeParticipation(memberResponse, challengeId, participationRequest);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId + "/participants/" + challengeParticipantId)).build();
    }

    @Operation(summary = "챌린지 참여 취소", description = "챌린지 참여를 취소합니다.")
    @DeleteMapping("/{participation-id}")
    public ResponseEntity<Void> cancelParticipation(
            @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
            @PathVariable("challenge-id") Long challengeId,    // challenge-id는 사용되지 않지만 URL 일관성을 위해 유지
            @PathVariable("participation-id") Long participationId) {
        challengeParticipationService.cancelParticipation(memberResponse, participationId);
        return ResponseEntity.noContent().build();
    }
}
