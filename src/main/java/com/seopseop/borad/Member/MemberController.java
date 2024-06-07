package com.seopseop.borad.Member;
import com.seopseop.borad.Post.Post;
import com.seopseop.borad.Post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PostRepository postRepository;

    @GetMapping("/signup")
    public String GetSignup (Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "Member/signup.html";
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

        return "Member/login.html";
    }

    @GetMapping("/infochange")
    public String GetInfoChange (Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/user/login";
        }
        return "Member/infochange.html";
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
        return "Member/passwordchange.html";
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

    @GetMapping("/mypage/{page}")
    public String mypage (Authentication auth,
                          Model model,
                          @PathVariable Long page) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/user/login";
        }
        Optional<Member> result = memberRepository.findByUsername(auth.getName());
        int a = page.intValue();

        if (result.isEmpty()) {
            return "redirect:/user/login";
        }

        Member user = result.get();

        Page<Post> results = postRepository.findByMember(user,PageRequest.of(a - 1, 5, Sort.by("id").descending()));
        int totalPages = results.getTotalPages();
        int currentPage = a;
        int startPage = Math.max(1, currentPage - 5);
        int endPage = Math.min(totalPages, currentPage + 4);
        if (endPage - startPage < 9) {
            if (startPage == 1) {
                endPage = Math.min(startPage + 9, totalPages);
            } else if (endPage == totalPages) {
                startPage = Math.max(1, endPage - 9);
            }
        }
        model.addAttribute("results", results.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("hasNext", results.hasNext());
        model.addAttribute("hasPrevious", results.hasPrevious());


        model.addAttribute("user",user);
        return "Member/mypage.html";
    }

}
