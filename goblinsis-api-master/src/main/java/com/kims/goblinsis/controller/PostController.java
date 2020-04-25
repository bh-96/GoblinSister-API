package com.kims.goblinsis.controller;

import com.kims.goblinsis.model.domain.Post;
import com.kims.goblinsis.model.dto.PostDTO;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.file.FileService;
import com.kims.goblinsis.service.post.PostService;
import com.kims.goblinsis.service.redis.RedisService;
import com.kims.goblinsis.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(Constants.API_POST)
public class PostController extends CommonService {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Autowired
    private RedisService redisService;

    /**
     * 게시글 가져오기
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getPost(@PathVariable int id) {
        Post post = postService.findById(id);

        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("postId가 유효하지 않습니다."), HttpStatus.BAD_REQUEST);
    }

    /**
     * 게시글 목록 가져오기
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getPostList(Pageable pageable) {
        Page<Post> posts = postService.findAll(pageable);

        if (posts != null) {
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("게시글을 읽어오지 못했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 게시글 등록
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> savePost(@RequestBody PostDTO postDTO) {
        Post post = toPost(postDTO);
        post.setRegDate(new Date());
        post.setModDate(new Date());
        post = postService.save(post);

        if (post != null) {
            // 레디스에 제고량 저장 (postId : stock)
            redisService.saveStock(post.getId(), post.getStock());

            int[] fileIds = findFileIdByContent(post.getContent());
            if (fileIds != null) {
                // 첨부한 파일이 있으면 해당 파일 type 지정
                fileService.update(fileIds, post.getId(), Constants.TYPE_POST);
            }

            return new ResponseEntity<>(post, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("게시글 저장 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 게시글 수정
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/post", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO) {
        Post post = postService.update(postDTO);

        if (post != null) {
            int[] fileIds = findFileIdByContent(post.getContent());
            if (fileIds != null) {
                // 첨부한 파일이 있으면 해당 파일 type 지정
                fileService.update(fileIds, post.getId(), Constants.TYPE_POST);
            }

            return new ResponseEntity<>(post, HttpStatus.OK);
        }

        return new ResponseEntity<>(returnErrJsonObj("게시글 수정 실패"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 게시글 삭제
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/post", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePost(@RequestParam(value = "id") int postId) {
        return new ResponseEntity<>(postService.delete(postId), HttpStatus.OK);
    }

    private Post toPost(PostDTO postDTO) {
        Post post = new Post();

        post.setSubject(postDTO.getSubject());
        post.setProductName(postDTO.getProductName());
        post.setPrice(postDTO.getPrice());
        post.setContent(postDTO.getContent());
        post.setRegDate(new Date());
        post.setCategory(postDTO.getCategory());

        return post;
    }

}
