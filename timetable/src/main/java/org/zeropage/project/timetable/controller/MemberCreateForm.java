package org.zeropage.project.timetable.controller;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.zeropage.project.timetable.domain.Member;

@Data
public class MemberCreateForm {
    @Size(min = 1, max = Member.userNameLen)
    @NotEmpty(message = "ID는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String password2;

    //비밀번호 양식은 제한하지 않음
}
