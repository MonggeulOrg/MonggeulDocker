package com.cmc.monggeul.domain.diary.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cmc.monggeul.domain.diary.dto.S3ImageDto;
import com.cmc.monggeul.domain.diary.entity.UploadFile;
import com.cmc.monggeul.domain.diary.repository.UploadFileRepository;
import com.cmc.monggeul.domain.user.entity.User;
import com.cmc.monggeul.domain.user.repository.UserRepository;
import com.cmc.monggeul.global.config.error.ErrorCode;
import com.cmc.monggeul.global.config.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {

    private final AmazonS3Client amazonS3Client;
    private final UploadFileRepository uploadFileRepository;

    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public S3ImageDto saveUploadFile(MultipartFile multipartFile,String userEmail) throws IOException {

        // 유저 정보 먼저 조회
        Optional<User> user=userRepository.findByEmail(userEmail);
        Long userId=user.orElseThrow(()->new BaseException(ErrorCode.USER_NOT_EXIST)).getId();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;
        String key = "test/" + storeFileName;

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String storeFileUrl = amazonS3Client.getUrl(bucket, key).toString();
        UploadFile uploadFile = new UploadFile(originalFilename, storeFileUrl,userId);
        UploadFile saveFile=uploadFileRepository.save(uploadFile);
        return S3ImageDto.builder().imageUrl(storeFileUrl).userId(userId).build();
    }

}