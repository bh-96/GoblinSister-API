package com.kims.goblinsis.service.post;

import com.kims.goblinsis.model.domain.Post;
import com.kims.goblinsis.model.dto.PostDTO;
import com.kims.goblinsis.repository.PostRepository;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.comment.CommentService;
import com.kims.goblinsis.service.file.FileService;
import com.kims.goblinsis.service.redis.RedisService;
import com.kims.goblinsis.utils.Constants;
import com.kims.goblinsis.utils.DateFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl extends CommonService implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisService redisService;

    @Override
    public Post findById(int id) {
        try {
            Post post = postRepository.findById(id);
            post.setStock(redisService.getStock(id));
            return post;
        } catch (Exception e) {
            logger.error("PostService -> findById : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        try {
            Page<Post> posts = postRepository.findAll(pageable);

            for (Post post : posts) {
                post.setStock(redisService.getStock(post.getId()));
            }

            return posts;
        } catch (Exception e) {
            logger.error("PostService -> findAll : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Post save(Post post) {
        try {
            return postRepository.saveAndFlush(post);
        } catch (Exception e) {
            logger.error("PostService -> save : " + e.getMessage());
        }

        return null;
    }

    @Override
    public Post update(PostDTO postDTO) {
        Post post = findById(postDTO.getId());

        if (post == null) {
            return null;
        }

        if (postDTO.getSubject() != null) {
            post.setSubject(postDTO.getSubject());
        }

        if (postDTO.getProductName() != null) {
            post.setProductName(postDTO.getProductName());
        }

        if (postDTO.getPrice() != post.getPrice()) {
            post.setPrice(postDTO.getPrice());
        }

        if (postDTO.getContent() != null) {
            post.setContent(postDTO.getContent());
        }

        if (postDTO.getCategory() != post.getCategory()) {
            post.setCategory(postDTO.getCategory());
        }

        if (postDTO.getStock() != post.getStock()) {
            // 레디스에 저장된 제고량 변경 (postId : stock)
            redisService.saveStock(post.getId(), post.getStock());
        }

        post.setModDate(new Date());
        post = save(post);

        return post;
    }

    @Override
    public boolean delete(int id) {
        try {
            Post post = findById(id);

            if (post == null) {
                return false;
            }

            // 댓글, 파일 삭제
            if (commentService.deleteByPost(post) && fileService.delete(id, Constants.TYPE_POST)) {
                // 게시글 삭제
                postRepository.deleteById(id);
                return true;
            }
        } catch (Exception e) {
            logger.error("PostService -> delete : " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<Post> findAllByRegDateOrModDate() {
        try {
            Date date = DateFormatUtil.getFormatDate("yyyy-MM-dd", new Date());
            return postRepository.findAllByRegDateOrModDate(date, date);
        } catch (Exception e) {
            logger.error("PostService -> findAll : " + e.getMessage());
            return null;
        }
    }

}
