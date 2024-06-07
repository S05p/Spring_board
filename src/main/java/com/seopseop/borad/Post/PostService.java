package com.seopseop.borad.Post;
import com.seopseop.borad.Member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public void postsave (String title, String content, Member member) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setMember(member);
        postRepository.save(post);
    }
}
