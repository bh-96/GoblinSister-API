package com.kims.goblinsis.service.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import com.kims.goblinsis.model.domain.file.UploadFile;
import com.kims.goblinsis.repository.file.FileRepository;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.utils.UploadFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl extends CommonService implements FileService {

    private final Path rootLocation;

    @Autowired
    public FileServiceImpl(String uploadPath) {
        logger.info("PATH :: " + uploadPath);
        this.rootLocation = Paths.get(uploadPath);
    }

    @Autowired
    private FileRepository fileRepository;

    @Override
    public UploadFile load(int id, int type) {
        return fileRepository.findByIdAndType(id, type);
    }

    @Override
    public Resource loadAsResource(String fileName) throws Exception {
        try {
            if (fileName.toCharArray()[0] == '/') {
                fileName = fileName.substring(1);
            }

            Path file = loadPath(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new Exception("Could not read file: " + fileName);
            }
        } catch (Exception e) {
            throw new Exception("Could not read file: " + fileName);
        }
    }

    @Override
    public Path loadPath(String fileName) {
        return rootLocation.resolve(fileName);
    }

    @Override
    public UploadFile store(MultipartFile file) throws Exception {
        try {
            if (file.isEmpty()) {
                throw new Exception("Failed to save empty file " + file.getOriginalFilename());
            }

            String saveFileName = UploadFileUtils.fileSave(rootLocation.toString(), file);

            if (saveFileName.toCharArray()[0] == '/') {
                saveFileName = saveFileName.substring(1);
            }

            Resource resource = loadAsResource(saveFileName);

            UploadFile saveFile = new UploadFile();
            saveFile.setSaveFileName(saveFileName);
            saveFile.setFileName(file.getOriginalFilename());
            saveFile.setContentType(file.getContentType());
            saveFile.setFilePath(rootLocation.toString().replace(File.separatorChar, '/') + File.separator + saveFileName);
            saveFile.setSize(resource.contentLength());
            saveFile.setRegDate(new Date());
            saveFile = fileRepository.save(saveFile);

            return saveFile;
        } catch (IOException e) {
            throw new Exception("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public boolean delete(int postId, int type) {
        try {
            List<UploadFile> files = findAllByTypeAndPostId(type, postId);

            // 저장공간에서 파일 삭제
            for (UploadFile file : files) {
                new File(file.getFilePath()).delete();
            }

            // 디비에서 삭제
            fileRepository.deleteAll(files);
            return true;
        } catch (Exception e) {
            logger.error("FileService -> delete : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(int[] fileIds, int postId, int type) {
        try {
            List<UploadFile> files = fileRepository.findAllByIdIn(fileIds);

            for (UploadFile file : files) {
                file.setType(type);
                file.setPostId(postId);
            }

            fileRepository.saveAll(files);
            return true;
        } catch (Exception e) {
            logger.error("FileService -> update : " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<UploadFile> findAllByTypeAndPostId(int type, int postId) {
        try {
            return fileRepository.findAllByTypeAndPostId(type, postId);
        } catch (Exception e) {
            logger.error("FileService -> findAllByPostId : " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<UploadFile> findAllByTypeIsNull() {
        try {
            return fileRepository.findAllByTypeIsNull();
        } catch (Exception e) {
            logger.error("FileService -> findAllByTypeIsNull : " + e.getMessage());
        }

        return null;
    }
}
