package com.kims.goblinsis.repository.file;

import com.kims.goblinsis.model.domain.file.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<UploadFile, Integer> {

    UploadFile findByIdAndType(int id, int type);

    List<UploadFile> findAllByIdIn(int[] ids);

    List<UploadFile> findAllByTypeAndPostId(int type, int postId);

    List<UploadFile> findAllByTypeIsNull();
}
