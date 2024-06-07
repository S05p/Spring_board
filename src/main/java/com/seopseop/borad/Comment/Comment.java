package com.seopseop.borad.Comment;
import com.seopseop.borad.Member.Member;
import com.seopseop.borad.Post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.parameters.P;

@Getter
@Setter
@Entity
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String content;

    @ManyToOne
    @JoinColumn(name = "member_id",nullable = false)
    public Member member;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    public Post post;

}
