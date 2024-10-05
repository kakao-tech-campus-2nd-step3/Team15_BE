package kakao.rebit.challenge.controller;

import java.net.URI;
import kakao.rebit.challenge.dto.ChallengeVerificationRequest;
import kakao.rebit.challenge.dto.ChallengeVerificationResponse;
import kakao.rebit.challenge.service.ChallengeVerificationService;
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
@RequestMapping("/api/challenges/{challenge-id}/verifications")
public class ChallengeVerificationController {

    private final ChallengeVerificationService challengeVerificationService;

    public ChallengeVerificationController(ChallengeVerificationService challengeVerificationService) {
        this.challengeVerificationService = challengeVerificationService;
    }

    @GetMapping
    public ResponseEntity<Page<ChallengeVerificationResponse>> getChallengeVerifications(
            @PathVariable("challenge-id") Long challengeId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(challengeVerificationService.getChallengeVerificationsById(challengeId, pageable));
    }

    @GetMapping("/{verification-id}")
    public ResponseEntity<ChallengeVerificationResponse> getChallengeVerification(
            @PathVariable("challenge-id") Long challengeId,
            @PathVariable("verification-id") Long verificationId) {
        return ResponseEntity.ok().body(challengeVerificationService.getChallengeVerificationById(challengeId, verificationId));
    }

    @PostMapping
    public ResponseEntity<Void> createChallengeVerification(
            @MemberInfo MemberResponse memberResponse,
            @PathVariable("challenge-id") Long challengeId,
            @RequestBody ChallengeVerificationRequest challengeVerificationRequest) {
       Long verificationId = challengeVerificationService.createChallengeVerification(memberResponse, challengeId,
                challengeVerificationRequest);
        return ResponseEntity.created(URI.create("/challenges/" + challengeId + "/verifications/" + verificationId)).build();
    }

    @DeleteMapping("/{verification-id}")
    public ResponseEntity<Void> deleteChallengeVerification(
            @MemberInfo MemberResponse memberResponse,
            @PathVariable("challenge-id") Long challengeId,
            @PathVariable("verification-id") Long verificationId) {
        challengeVerificationService.deleteChallengeVerification(memberResponse, challengeId, verificationId);
        return ResponseEntity.noContent().build();
    }
}
