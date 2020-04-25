package com.kims.goblinsis.service.comment;

import com.kims.goblinsis.model.domain.Comment;
import com.kims.goblinsis.model.domain.Post;
import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.model.dto.CommentDTO;

public interface CommentService {

    Comment save(Comment comment);

    Comment update(User user, CommentDTO commentDTO);

    boolean delete(User user, int id);

    Comment findById(int id);

    boolean deleteByPost(Post post);
}
