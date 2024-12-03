package com.doantotnghiep.nhanambooks.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UploadFirebase {

    private Storage storage;

    @EventListener
    public void init(ApplicationReadyEvent event) {
        try {
            ClassPathResource serviceAccount = new ClassPathResource("firebase.json");
            storage = StorageOptions.newBuilder().
                    setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream())).
                    setProjectId("doantotnghiep-f3dfe").build().getService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String saveImage(MultipartFile file) throws IOException {
        if("jpg,png,jpeg,gif".contains(Objects.requireNonNull(getExtension(file.getOriginalFilename())))) {

            String imageName = generateFileName(file.getOriginalFilename()) + System.currentTimeMillis();
            Map<String, String> map = new HashMap<>();
            map.put("firebaseStorageDownloadTokens", imageName);

            BlobId blobId = BlobId.of("doantotnghiep-f3dfe.appspot.com", imageName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setMetadata(map)
                    .setContentType(file.getContentType())
                    .build();
            storage.create(blobInfo, file.getBytes());
            return imageName;
        }
        return null;
    }

    private String generateFileName(String originalFileName) {
        return UUID.randomUUID().toString() + "." + getExtension(originalFileName);
    }

    private String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

}