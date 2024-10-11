package kakao.rebit.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kakao.rebit.member.annotation.MemberInfo;
import kakao.rebit.member.dto.ChargePointRequest;
import kakao.rebit.member.dto.MemberRequest;
import kakao.rebit.member.dto.MemberResponse;
import kakao.rebit.member.entity.Role;
import kakao.rebit.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원 API", description = "회원 관련 API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "포인트 조회", description = "사용자의 포인트를 조회합니다.")
    @GetMapping("/points")
    public ResponseEntity<Integer> getMyPoints(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse) {
        Integer points = memberService.getPoints(memberResponse.email());
        return ResponseEntity.ok(points);
    }

    @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다.")
    @PostMapping("/points")
    public ResponseEntity<Void> chargePoints(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @RequestBody ChargePointRequest request) {
        memberService.chargePoints(memberResponse.email(), request.points());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내 정보 조회", description = "사용자 자신의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse) {
        MemberResponse response = memberService.getMemberResponseByEmail(memberResponse.email());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 정보 수정", description = "사용자 자신의 정보를 수정합니다.")
    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMyInfo(
        @Parameter(hidden = true) @MemberInfo MemberResponse memberResponse,
        @RequestBody MemberRequest memberRequest) {
        MemberResponse updatedMember = memberService.updateMyMember(memberResponse.email(),
            memberRequest);
        return ResponseEntity.ok(updatedMember);
    }

    @Operation(summary = "모든 사용자 조회", description = "관리자 및 에디터가 모든 사용자를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers(
        @Parameter(hidden = true) @MemberInfo(allowedRoles = {Role.ROLE_ADMIN,
            Role.ROLE_EDITOR}) MemberResponse memberResponse) {
        List<MemberResponse> members = memberService.getAllMemberResponses();
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "특정 사용자 조회", description = "관리자 및 에디터가 특정 사용자를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(
        @Parameter(hidden = true) @MemberInfo(allowedRoles = {Role.ROLE_ADMIN,
            Role.ROLE_EDITOR}) MemberResponse memberResponse,
        @PathVariable("id") Long id) {
        MemberResponse response = memberService.getMemberResponseById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 사용자 수정", description = "관리자 및 에디터가 특정 사용자의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(
        @Parameter(hidden = true) @MemberInfo(allowedRoles = {Role.ROLE_ADMIN,
            Role.ROLE_EDITOR}) MemberResponse memberResponse,
        @PathVariable("id") Long id,
        @RequestBody MemberRequest memberRequest) {
        MemberResponse updatedMember = memberService.updateMember(id, memberRequest);
        return ResponseEntity.ok(updatedMember);
    }

    @Operation(summary = "특정 사용자 삭제", description = "관리자 및 에디터가 특정 사용자를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
        @Parameter(hidden = true) @MemberInfo(allowedRoles = {Role.ROLE_ADMIN,
            Role.ROLE_EDITOR}) MemberResponse memberResponse,
        @PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
