package com.kims.goblinsis.service.comment;

import com.kims.goblinsis.model.domain.Comment;
import com.kims.goblinsis.model.domain.Post;
import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.model.dto.CommentDTO;
import com.kims.goblinsis.repository.CommentRepository;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.post.PostService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl extends CommonService implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    @Override
    public Comment save(Comment comment) {
        try {
            return commentRepository.saveAndFlush(comment);
        } catch (Exception e) {
            logger.error("CommentService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Comment update(User user, CommentDTO commentDTO) {
        Comment comment = findById(commentDTO.getId());

        if (comment == null) {
            return null;
        }

        // 해당 사용자가 쓴 댓글인지 확인
        if (user.getRole().getRolename().equalsIgnoreCase(Constants.ROLE_USER)) {
            if (comment.getUser().getId() != user.getId()) {
                return null;
            }
        }

        if (commentDTO.getComment() != null && !commentDTO.getComment().equals("")) {
            comment.setComment(commentDTO.getComment());
            comment.setModDate(new Date());

            return save(comment);
        }

        if (commentDTO.getReComment() != null && !commentDTO.getReComment().equals("")) {
            comment.setReComment(commentDTO.getReComment());
            comment.setModDate(new Date());

            return save(comment);
        }

        return null;
    }

    @Override
    public boolean delete(User user, int id) {
        try {
            Comment comment = findById(id);

            if (comment != null) {
                // 해당 사용자가 쓴 댓글인지 확인
                if (comment.getUser().getId() != user.getId()) {
                    return false;
                }

                commentRepository.deleteById(id);
                return true;
            }
        } catch (Exception e) {
            logger.error("CommentService -> delete : " + e.getMessage());
        }

        return false;
    }

    @Override
    public Comment findById(int id) {
        try {
            return commentRepository.findById(id);
        } catch (Exception e) {
            logger.error("CommentService -> findById : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean deleteByPost(Post post) {
        try {
            List<Comment> comments = commentRepository.findAllByPost(post);
            commentRepository.deleteAll(comments);
            return true;
        } catch (Exception e) {
            logger.error("CommentService -> findAllIdByPostId : " + e.getMessage());
        }

        return false;
    }

}
