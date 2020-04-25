package com.kims.goblinsis.repository;

import com.kims.goblinsis.model.domain.Comment;
import com.kims.goblinsis.model.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findById(int id);

    List<Comment> findAllByPost(Post post);
}
