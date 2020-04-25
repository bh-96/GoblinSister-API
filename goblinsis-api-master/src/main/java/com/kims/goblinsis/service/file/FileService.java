package com.kims.goblinsis.service.file;

import com.kims.goblinsis.model.domain.file.UploadFile;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface FileService {

    UploadFile load(int id, int type);

    Resource loadAsResource(String fileName) throws Exception;

    Path loadPath(String fileName);

    UploadFile store(MultipartFile file) throws Exception;

    @Transactional
    boolean delete(int postId, int type);

    @Transactional
    boolean update(int[] fileIds, int postId, int type);

    List<UploadFile> findAllByTypeAndPostId(int type, int postId);

    List<UploadFile> findAllByTypeIsNull();

}
