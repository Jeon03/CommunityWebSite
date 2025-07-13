package com.community.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    //config파일에서 만든 bean객체 가져옴
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String key = "post-images/" + fileName;
        
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(bucket, key, file.getInputStream(), metadata); //s3 업로드
        return amazonS3.getUrl(bucket, key).toString(); //url반환
    }

    public void deleteImage(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("post-images/")) return;
        String key = imageUrl.substring(imageUrl.indexOf("post-images/"));
        amazonS3.deleteObject(bucket, key);
    }
}
