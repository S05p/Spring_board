package com.seopseop.borad.Comment;
import com.seopseop.borad.Member.Member;
import com.seopseop.borad.Member.MemberRepository;
import com.seopseop.borad.Post.Post;
import com.seopseop.borad.Post.PostRepository;
import com.seopseop.borad.Post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @PostMapping("/detail/{post_id}/create_comment")
    public String Create_Comment (@PathVariable Long post_id,
                                  Authentication auth,
                                  @RequestParam String content) {
        int a = post_id.intValue();
        Optional<Member> member_result = memberRepository.findByUsername(auth.getName());
        Optional<Post> post_result = postRepository.findById(post_id);

        if (member_result.isEmpty() || post_result.isEmpty()) {
            return "redirect:/detail/"+a;
        }
        Comment comment = new Comment();
        comment.setPost(post_result.get());
        comment.setMember(member_result.get());
        comment.setContent(content);
        commentRepository.save(comment);
        return "redirect:/detail/"+a;

    }
}
