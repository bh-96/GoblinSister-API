package com.kims.goblinsis.controller.file;

import com.kims.goblinsis.model.domain.file.UploadFile;
import com.kims.goblinsis.service.CommonService;
import com.kims.goblinsis.service.file.FileService;
import com.kims.goblinsis.utils.Constants;
import com.kims.goblinsis.utils.MediaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = Constants.API_FILE)
public class FileController extends CommonService {

    @Autowired
    private FileService fileService;

    /**
     * 파일 불러오기
     */
    @RequestMapping(value = "{type}/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> loadFile(@PathVariable int id, @PathVariable String type) {
        try {
            // type 지정
            int t = 0;
            if (type.equalsIgnoreCase("POST")) {
                t = Constants.TYPE_POST;
            } else if (type.equalsIgnoreCase("REVIEW")) {
                t = Constants.TYPE_REVIEW;
            } else if (type.equalsIgnoreCase("REFUND")) {
                t = Constants.TYPE_REFUND;
            } else {
                return ResponseEntity.badRequest().build();
            }

            UploadFile uploadedFile = fileService.load(id, t);
            HttpHeaders headers = new HttpHeaders();

            String fileName = uploadedFile.getFileName();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") + "\"");

            if (MediaUtils.containsImageMediaType(uploadedFile.getContentType())) {
                headers.setContentType(MediaType.valueOf(uploadedFile.getContentType()));
            } else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            Resource resource = fileService.loadAsResource(uploadedFile.getSaveFileName());
            return ResponseEntity.ok().headers(headers).body(resource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 파일 저장
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file) {
        try {
            return new ResponseEntity<>(fileService.store(file), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(returnErrJsonObj(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
