package com.kims.goblinsis.repository;

import com.kims.goblinsis.model.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    Post findById(int id);

    List<Post> findAllByRegDateOrModDate(Date regDate, Date modDate);
}
