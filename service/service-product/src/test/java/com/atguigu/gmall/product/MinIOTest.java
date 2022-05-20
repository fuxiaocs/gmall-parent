package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;

@SpringBootTest
public class MinIOTest {

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Test
    public void fileUp(){
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(
                    "http://192.168.60.130:9000",
                    "admin",
                    "admin123456");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("gmall");
            if(isExist) {
                System.out.println("Bucket gmall 已经存在");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket("asiatrip");
            }

            FileInputStream fis = new FileInputStream("D:\\图片\\山.jpg");
            PutObjectOptions poo = new PutObjectOptions(fis.available(), -1);
            poo.setContentType("image/jpeg");
            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject("gmall","mo.jpg",fis,poo);
            System.out.println("文件上传成功");
        } catch(Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Test
    public void test01(){
        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(50L);
        skuInfo.setIsSale(1);
        skuInfoMapper.updateById(skuInfo);

    }
}
