package vdt.kpimanagement.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vdt.kpimanagement.dto.FileUploadDTO;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public FileUploadDTO generatePresignedUrl(String originalFileName) {
        // Sinh tên file độc nhất để tránh trùng lặp trên bucket
        String fileExtension = "";
        if (originalFileName != null) {
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex >= 0) {
                fileExtension = originalFileName.substring(dotIndex);
            }
        }
        String objectKey = "evidences/" + UUID.randomUUID().toString() + fileExtension;

        // Set thời gian hết hạn cho URL (ví dụ: 15 phút)
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 15;
        expiration.setTime(expTimeMillis);

        // Tạo request cấp quyền PUT (Upload)
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        URL presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        // Trả về DTO chứa URL cho Client upload và objectKey để Client gửi lại sau khi upload xong
        FileUploadDTO result = new FileUploadDTO();
        result.setPresignedUrl(presignedUrl.toString());
        result.setObjectKey(objectKey);
        return result;
    }

    public String generateDownloadUrl(String objectKey) {
        // Thiết lập thời gian link có hiệu lực để xem (ví dụ: 10 phút)
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 10; // 10 phút
        expiration.setTime(expTimeMillis);

        // Tạo request cấp quyền GET (Đọc/Tải file)
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.GET) // Khác biệt ở đây: Dùng GET thay vì PUT
                        .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }
}
