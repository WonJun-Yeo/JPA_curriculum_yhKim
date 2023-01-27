package japbook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class MemberForm {

    @NotNull(message= "회원이름은 필수입니다.")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
