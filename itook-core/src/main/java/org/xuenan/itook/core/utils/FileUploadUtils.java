package org.xuenan.itook.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xuenan.itook.core.exception.GlobalException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUploadUtils.class);

    public FileUploadUtils() {
    }

    public static File upload(MultipartFile checkFile, File rootFolder) {
        return upload(checkFile, rootFolder, (String)null);
    }

    public static String uploadGetRelative(MultipartFile checkFile, File rootFolder, String folder) {
        File sourceFile = upload(checkFile, rootFolder, folder);
        String absoluteRoot = rootFolder.getAbsolutePath();
        String absoluteFile = sourceFile.getAbsolutePath();
        return absoluteFile.substring(absoluteRoot.length());
    }

    public static File upload(MultipartFile checkFile, File rootFolder, String folder) {
        if (StringUtils.isEmpty(new String[]{folder})) {
            folder = "";
        }

        File destFolder = new File(rootFolder, folder);
        if (!destFolder.exists()) {
            destFolder.mkdirs();
            log.debug("创建目录 {}完毕", destFolder);
        }

        File dest = new File(destFolder, newFileName(checkFile.getOriginalFilename()));

        try {
            checkFile.transferTo(dest);
            log.info("上传文件完毕  >>> {}", dest);
        } catch (IOException | IllegalStateException var6) {
            log.error("上传文件{}失败", dest, var6);
            GlobalException.error("上传文件失败", new Object[0]);
        }

        return dest;
    }

    private static String newFileName(String originalFilename) {
        String fileName;
        if (StringUtils.isEmpty(new String[]{originalFilename})) {
            fileName = UUID.randomUUID().toString();
        } else {
            int index = originalFilename.lastIndexOf(46);
            if (index <= 0) {
                fileName = UUID.randomUUID().toString();
            } else {
                fileName = UUID.randomUUID().toString() + originalFilename.substring(index);
            }
        }

        return fileName.replaceAll("\\-", "");
    }

    public static String uploadGetRelative(String checkFileName, File rootFolder, String folder) {
        File checkFile = new File(rootFolder, checkFileName);
        Assert.isTrue(checkFile.isFile(), "对账文件不存在", new Object[0]);
        if (StringUtils.isEmpty(new String[]{folder})) {
            folder = "";
        }

        File destFolder = new File(rootFolder, folder);
        if (!destFolder.exists()) {
            destFolder.mkdirs();
            log.debug("创建目录 {}完毕", destFolder);
        }

        File sourceFile = new File(destFolder, newFileName(checkFileName));

        try {
            FileCopyUtils.copy(checkFile, sourceFile);
            checkFile.delete();
            log.info("移动文件完毕  {}  ==>> {}", checkFile, sourceFile);
        } catch (IOException var8) {
            log.warn("移动文件失败  {}  ==>> {}", new Object[]{checkFile, sourceFile, var8});
            GlobalException.warn("文件移动失败", new Object[0]);
        }

        String absoluteRoot = rootFolder.getAbsolutePath();
        String absoluteFile = sourceFile.getAbsolutePath();
        return absoluteFile.substring(absoluteRoot.length());
    }
}
