package kakao.rebit.member.controller;

import java.util.List;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.MemberRequest;
import kakao.rebit.member.dto.MemberInfoDto;
import kakao.rebit.member.entity.Member;
import kakao.rebit.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 사용자 자신의 정보 조회
    @GetMapping("/me")
    public ResponseEntity<Member> getMyInfo(@MemberInfo MemberInfoDto memberInfo) {
        Member member = memberService.findMemberByEmail(memberInfo.getEmail());
        return ResponseEntity.ok(member);
    }

    // 사용자 자신의 정보 수정
    @PutMapping("/me")
    public ResponseEntity<Member> updateMyInfo(@MemberInfo MemberInfoDto memberInfo,
        @RequestBody MemberRequest memberRequest) {
        Member updatedMember = memberService.updateMyMember(memberInfo.getEmail(), memberRequest);
        return ResponseEntity.ok(updatedMember);
    }

    // admin & editor : 모든 사용자 조회
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers(@MemberInfo MemberInfoDto memberInfo) {
        authorize(memberInfo.getRole(), "ROLE_ADMIN", "ROLE_EDITOR");
        List<Member> members = memberService.findAllMembers();
        return ResponseEntity.ok(members);
    }

    // admin & editor : 특정 사용자 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@MemberInfo MemberInfoDto memberInfo,
        @PathVariable Long id) {
        authorize(memberInfo.getRole(), "ROLE_ADMIN", "ROLE_EDITOR");
        Member member = memberService.findMemberByIdOrThrow(id);
        return ResponseEntity.ok(member);
    }

    // admin & editor : 특정 사용자 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@MemberInfo MemberInfoDto memberInfo,
        @PathVariable Long id,
        @RequestBody MemberRequest memberRequest) {
        authorize(memberInfo.getRole(), "ROLE_ADMIN", "ROLE_EDITOR");
        Member updatedMember = memberService.updateMember(id, memberRequest);
        return ResponseEntity.ok(updatedMember);
    }

    // admin & editor : 특정 사용자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@MemberInfo MemberInfoDto memberInfo,
        @PathVariable Long id) {
        authorize(memberInfo.getRole(), "ROLE_ADMIN", "ROLE_EDITOR");
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    private void authorize(String role, String... roles) {
        if (!List.of(roles).contains(role)) {
            throw new IllegalStateException("Unauthorized");
        }
    }
}
