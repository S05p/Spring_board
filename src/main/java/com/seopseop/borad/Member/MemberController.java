package com.seopseop.borad.Member;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/signup")
    public String GetSignup (Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "signup.html";
        }
        return "redirect:/list/1";
    }
    @PostMapping("/signup")
    public String PostSignup (@RequestParam String username,
                              @RequestParam String password1,
                              @RequestParam String password2,
                              @RequestParam String nickname) {
        Optional<Member> result = memberRepository.findByUsername(username);
        if (result.isPresent()) {
            return "redirect:/user/signup?error=username_exist";
        }
        if (!password1.equals(password2) || password1.length() <= 7) {
            return "redirect:/user/signup?error=password_problem";
        }
        var hashed = new BCryptPasswordEncoder().encode(password1);
        memberService.membersave(username,nickname,hashed);
        return "redirect:/list/1";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/infochange")
    public String GetInfoChange () {
        return "infochange.html";
    }
    @PostMapping("/infochange")
    public String PostInfoChange () {
        return "redirect:/list/1";
    }

    @GetMapping("/passwordchange")
    public String GetPasswordChange () {
        return "passwordchange.html";
    }

    @PostMapping("/passwordchange")
    public String PostPasswordChange () {
        return "redirect:/list/1";
    }

    @GetMapping("/mypage")
    public String mypage (Authentication auth) {
        return "mypage.html";
    }

}
