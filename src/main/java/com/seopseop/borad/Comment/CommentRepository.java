package com.seopseop.borad.Comment;
import com.seopseop.borad.Post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Page<Comment> findByPost(Post post_id, Pageable pageable);
}