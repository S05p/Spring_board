package com.seopseop.borad.Member;
import com.seopseop.borad.Comment.Comment;
import com.seopseop.borad.Post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String username;
    public String password;
    public String nickname;

    @OneToMany(mappedBy = "member")
    public List<Comment> comments;

    @OneToMany(mappedBy = "member")
    public List<Post> posts;

}
