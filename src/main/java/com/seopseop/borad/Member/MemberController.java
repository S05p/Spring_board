package com.seopseop.borad.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String GetInfoChange (Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/user/login";
        }
        return "infochange.html";
    }
    @PostMapping("/infochange")
    public String PostInfoChange (@RequestParam String nickname,
                                  Authentication auth) {
        Optional<Member> result = memberRepository.findByUsername(auth.getName());

        if (result.isEmpty()) {
            return "redirect:/user/login";
        }
        var user = result.get();
        user.setId(user.getId());
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        user.setNickname(nickname);
        memberRepository.save(user);

        return "redirect:/list/1";
    }

    @GetMapping("/passwordchange")
    public String GetPasswordChange (Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/user/login";
        }
        return "passwordchange.html";
    }

    @PostMapping("/passwordchange")
    public String PostPasswordChange (@RequestParam String password1,
                                      @RequestParam String password2,
                                      Authentication auth) {

        Optional<Member> result = memberRepository.findByUsername(auth.getName());

        if (result.isEmpty()) {
            return "redirect:/user/login";
        }

        if (password1.equals(password2) && password1.length() > 7) {
            var user = result.get();
            var hashed = new BCryptPasswordEncoder().encode(password1);
            user.setId(user.getId());
            user.setUsername(user.getUsername());
            user.setNickname(user.getNickname());
            user.setPassword(hashed);
            return "redirect:/list/1";
        } else {
            return "redirect:/user/passwordchange?error=password_has_problem";
        }
    }

    @GetMapping("/mypage")
    public String mypage (Authentication auth,
                          Model model) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/user/login";
        }
        Optional<Member> result = memberRepository.findByUsername(auth.getName());
        var user = result.get();
        model.addAttribute("user",user);
        return "mypage.html";
    }

}
