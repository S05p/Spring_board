package com.seopseop.borad.Post;
import com.seopseop.borad.Comment.Comment;
import com.seopseop.borad.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String title;
    public String content;

    @ManyToOne
    @JoinColumn(name = "member_id",nullable = false)
    public Member member;

    @OneToMany(mappedBy = "post")
    public List<Comment> comments;

}

