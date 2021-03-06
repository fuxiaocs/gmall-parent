package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.minio.OSSService;
import com.atguigu.gmall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/admin/product")
@RestController
public class FileController {

    @Autowired
    OSSService ossService;

    @PostMapping("/fileUpload")
    public Result uploadFile(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        long size = file.getSize();
        String contentType = file.getContentType();
        log.info("文件信息 => name : {}, size : {}, type :{}",
                filename,
                size,
                contentType);
        String path = ossService.uploadFile(file);
        return Result.ok(path);
    }
}
