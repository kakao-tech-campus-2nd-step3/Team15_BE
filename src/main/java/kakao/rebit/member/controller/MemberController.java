package kakao.rebit.member.controller;

import java.util.List;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.ChargePointRequest;
import kakao.rebit.member.dto.MemberRequest;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.entity.Role;
import kakao.rebit.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 사용자 자신의 포인트 조회
    @GetMapping("/points")
    public ResponseEntity<Integer> getMyPoints(@MemberInfo MemberResponse memberResponse) {
        Integer points = memberService.getPoints(memberResponse.email());
        return ResponseEntity.ok(points);
    }

    // 포인트 충전
    @PostMapping("/points")
    public ResponseEntity<Void> chargePoints(@MemberInfo MemberResponse memberResponse, @RequestBody ChargePointRequest request) {
        memberService.chargePoints(memberResponse.email(), request.points());
        return ResponseEntity.noContent().build();
    }

    // 사용자 자신의 정보 조회
    @GetMapping("/me")
    public ResponseEntity<Member> getMyInfo(@MemberInfo MemberResponse memberResponse) {
        Member member = memberService.findMemberByEmailOrThrow(memberResponse.email());
        return ResponseEntity.ok(member);
    }

    // 사용자 자신의 정보 수정
    @PutMapping("/me")
    public ResponseEntity<Member> updateMyInfo(@MemberInfo MemberResponse memberResponse,
        @RequestBody MemberRequest memberRequest) {
        Member updatedMember = memberService.updateMyMember(memberResponse.email(), memberRequest);
        return ResponseEntity.ok(updatedMember);
    }

    // admin & editor : 모든 사용자 조회
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers(@MemberInfo(allowedRoles = {Role.ROLE_ADMIN, Role.ROLE_EDITOR}) MemberResponse memberResponse) {
        List<Member> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }

    // admin & editor : 특정 사용자 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@MemberInfo(allowedRoles = {Role.ROLE_ADMIN, Role.ROLE_EDITOR}) MemberResponse memberResponse,
        @PathVariable Long id) {
        Member member = memberService.findMemberByIdOrThrow(id);
        return ResponseEntity.ok(member);
    }

    // admin & editor : 특정 사용자 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@MemberInfo(allowedRoles = {Role.ROLE_ADMIN, Role.ROLE_EDITOR}) MemberResponse memberResponse,
        @PathVariable Long id,
        @RequestBody MemberRequest memberRequest) {
        Member updatedMember = memberService.updateMember(id, memberRequest);
        return ResponseEntity.ok(updatedMember);
    }

    // admin & editor : 특정 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@MemberInfo(allowedRoles = {Role.ROLE_ADMIN, Role.ROLE_EDITOR}) MemberResponse memberResponse,
        @PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
