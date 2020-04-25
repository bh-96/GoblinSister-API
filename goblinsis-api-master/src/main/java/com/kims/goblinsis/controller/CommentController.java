package com.kims.goblinsis.controller;

import com.kims.goblinsis.model.domain.Comment;
import com.kims.goblinsis.model.domain.Post;
import com.kims.goblinsis.model.domain.user.User;
import com.kims.goblinsis.model.dto.CommentDTO;
import com.kims.goblinsis.security.AccountChecker;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.comment.CommentService;
import com.kims.goblinsis.service.post.PostService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping(value = Constants.API_COMMENT)
public class CommentController extends CommonService {

    @Autowired
    private AccountChecker accountChecker;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    /**
     * 댓글 등록
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        User user = accountChecker.getUser(request);
        Post post = postService.findById(commentDTO.getPostId());

        if (post == null) {
            return new ResponseEntity<>(returnErrJsonObj("잘못된 게시글 정보 입니다."), HttpStatus.BAD_REQUEST);
        }

        Comment comment = new Comment();
        comment.setComment(commentDTO.getComment());
        comment.setRegDate(new Date());
        comment.setUser(user);
        comment.setPost(post);

        comment = commentService.save(comment);

        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("댓글 달기 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 댓글 수정
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        User user = accountChecker.getUser(request);
        commentDTO.setReComment("");

        Comment comment = commentService.update(user, commentDTO);

        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("잘못된 형식의 댓글 입니다."), HttpStatus.BAD_REQUEST);
    }

    /**
     * 댓글 삭제
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam int id, HttpServletRequest request) {
        User user = accountChecker.getUser(request);
        return new ResponseEntity<>(commentService.delete(user, id), HttpStatus.OK);
    }

    /**
     * 대댓글 달기
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/reComment", method = RequestMethod.PUT)
    public ResponseEntity<?> reComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        User admin = accountChecker.getUser(request);
        commentDTO.setComment("");

        Comment comment = commentService.update(admin, commentDTO);

        if (comment != null) {
            return new ResponseEntity<>(comment, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("잘못된 형식의 댓글 입니다."), HttpStatus.BAD_REQUEST);
    }
}
