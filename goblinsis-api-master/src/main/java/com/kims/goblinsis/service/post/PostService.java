package com.kims.goblinsis.service.post;

import com.kims.goblinsis.model.domain.Post;
import com.kims.goblinsis.model.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Post findById(int id);

    Page<Post> findAll(Pageable pageable);

    Post save(Post post);

    Post update(PostDTO postDTO);

    boolean delete(int id);

    List<Post> findAllByRegDateOrModDate();

}
