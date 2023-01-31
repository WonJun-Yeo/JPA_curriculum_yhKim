package japbook.jpashop.api;

import japbook.jpashop.domain.Member;
import japbook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

//@Controller @ResponseBody
@RestController
@RequiredArgsConstructor
public class MemberApiController {
  //
  private final MemberService memberService;

  // member join api
  @PostMapping("api/v1/members")
  public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
    Long id = memberService.join(member);
    return new CreateMemberResponse(id);
  }

  @PostMapping("api/v2/members")
  public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
    Member member = new Member();

    member.setName(request.getName());

    Long id = memberService.join(member);
    return new CreateMemberResponse(id);
  }

  // member modify api
  @PutMapping("api/v2/members/{id}")
  public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                             @RequestBody @Valid UpdateMemberRequest request) {
    // command, query 분리
    memberService.update(id, request.getName());
    Member findMember = memberService.findOne(id);
    return new UpdateMemberResponse(findMember.getId(), findMember.getName());
  }

  // member query api
  @GetMapping("/api/v1/members")
  public List<Member> membersV1() {
    return memberService.findMembers();
  }

  @GetMapping("api/v2/members")
  public Result membersV2() {
    List<Member> findMembers = memberService.findMembers();
    List<MemberDto> collect = findMembers.stream()
            .map(m -> new MemberDto(m.getName()))
            .collect(Collectors.toList());

    return new Result(collect);
  }

  @Data
  @AllArgsConstructor
  static class Result<T> {
    private T data;
  }

  @Data
  @AllArgsConstructor
  static class MemberDto {
    private String name;
  }


  /**
   * DTO
   * entity 를 직접 받고 반환해주게 되면 entity 변경시 api spec 이 변하게되어 side effect 가 심하게 발행할 수 있다.
   * */
  // join
  @Data
  static class CreateMemberResponse {
    private Long id;

    public CreateMemberResponse(Long id) {
      this.id = id;
    }
  }

  @Data
  static class CreateMemberRequest {
    @NotEmpty
    private String name;
  }

  // update

  @Data
  @AllArgsConstructor
  static class UpdateMemberResponse {
    private Long id;
    private String name;
  }

  @Data
  static class UpdateMemberRequest {
    private String name;
  }
}
