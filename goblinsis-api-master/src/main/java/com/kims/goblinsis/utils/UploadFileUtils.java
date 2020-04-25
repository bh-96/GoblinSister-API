package com.kims.goblinsis.utils;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import org.imgscalr.Scalr;

public class UploadFileUtils {

    public static String fileSave(String uploadPath, MultipartFile file) throws IllegalStateException, IOException {

        File uploadPathDir = new File(uploadPath);

        if ( !uploadPathDir.exists() ){
            uploadPathDir.mkdirs();
        }

        // 파일 중복명 처리
        String genId = UUID.randomUUID().toString();
        genId = genId.replace("-", "");

        String originalfileName = file.getOriginalFilename();
        String fileExtension = getExtension(originalfileName);
        String saveFileName = genId + "." + fileExtension;

        String savePath = calcPath(uploadPath);

        File target = new File(uploadPath + savePath, saveFileName);

        FileCopyUtils.copy(file.getBytes(), target);

        return makeFilePath(uploadPath, savePath, saveFileName);
    }

    /**
     * 파일이름으로부터 확장자를 반환
     *
     * @param fileName
     *            확장자를 포함한 파일 명
     * @return 파일 이름에서 뒤의 확장자 이름만을 반환
     */
    public static String getExtension(String fileName) {
        int dotPosition = fileName.lastIndexOf('.');

        if (-1 != dotPosition && fileName.length() - 1 > dotPosition) {
            return fileName.substring(dotPosition + 1);
        } else {
            return "";
        }
    }

    private static String calcPath(String uploadPath) {

        Date date = new Date();

        String yyyyMMdd = File.separator + DateFormatUtil.getFormatStringByDate("yyyyMMdd", date);
        String HHmm = File.separator + DateFormatUtil.getFormatStringByDate("HHmm", date);

        makeDir(uploadPath, yyyyMMdd, HHmm);

        return yyyyMMdd + HHmm;
    }

    private static void makeDir(String uploadPath, String yyyyMMdd, String HHmm) {

        File yyyyMMddDirPath = new File(uploadPath + yyyyMMdd);

        if (!yyyyMMddDirPath.exists()) {
            yyyyMMddDirPath.mkdir();
        }

        File HHmmDirPath = new File(uploadPath + yyyyMMdd + HHmm);

        if (!HHmmDirPath.exists()) {
            HHmmDirPath.mkdir();
        }
    }

    private static String makeFilePath(String uploadPath, String path, String fileName) throws IOException {
        String filePath = uploadPath + path + File.separator + fileName;
        return filePath.substring(uploadPath.length()).replace(File.separatorChar, '/');
    }

    private static String makeThumbnail(String uploadPath, String path, String fileName) throws Exception {

        BufferedImage sourceImg = ImageIO.read(new File(uploadPath + path, fileName));

        BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);

        String thumbnailName = uploadPath + path + File.separator + "s_" + fileName;

        File newFile = new File(thumbnailName);
        String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

        ImageIO.write(destImg, formatName.toUpperCase(), newFile);

        return thumbnailName.substring(uploadPath.length()).replace(File.separatorChar, '/');
    }
}
