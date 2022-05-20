package com.atguigu.gmall.common.minio;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface OSSService {

    /**
     * 文件上传
     * @param file
     * @return
     */
    String uploadFile(MultipartFile file) throws IOException, NoSuchAlgorithmException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, InvalidBucketNameException, XmlParserException, ErrorResponseException, Exception;
}
