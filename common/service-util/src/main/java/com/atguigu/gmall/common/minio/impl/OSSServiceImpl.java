package com.atguigu.gmall.common.minio.impl;

import com.atguigu.gmall.common.minio.OSSService;
import com.atguigu.gmall.common.properties.MinIOProperties;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OSSServiceImpl implements OSSService {

    @Autowired
    MinioClient minioClient;
    @Autowired
    MinIOProperties minIOProperties;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        //1. 文件名称
        String filename = UUID.randomUUID().toString().replace("-","") + "_" + file.getOriginalFilename();
        //2.文件流
        InputStream is = file.getInputStream();
        //3.文件设置
        PutObjectOptions poo = new PutObjectOptions(is.available(), -1);
        //4.设置文件类型
        poo.setContentType(file.getContentType());
        //5.上传
        minioClient.putObject(
                minIOProperties.getBucket(),
                filename,
                is,
                poo);
        //6.返回文件路径
        return minIOProperties.getEndpoint() + "/" +
                minIOProperties.getBucket() + "/" +
                filename;
    }
}
