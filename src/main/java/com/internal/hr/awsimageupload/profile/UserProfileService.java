package com.internal.hr.awsimageupload.profile;

import com.internal.hr.awsimageupload.buckets.BucketName;
import com.internal.hr.awsimageupload.filestore.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles(){
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {

        //1. check if image is not empty
        isFileEmpty(file);

        //2. check if file is an image
        try{
            if(isFileAnImage(file)) {

                //3. check if the user exists in our database
                UserProfile user = getUser(userProfileId);

                //4. grab metadata from file if any
                Map<String, String> metadata = new HashMap<>();
                metadata.put("Content-type", file.getContentType());
                metadata.put("Content-length", String.valueOf(file.getSize()));

                //5. store the image in S3 and update database with S3 image link
                String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
                String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

                try {
                    fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
                } catch (IOException exception) {
                    throw new IllegalStateException(exception);
                }
            }
        }catch (IllegalStateException exception){
            exception.printStackTrace();
        }
    }

    private UserProfile getUser(UUID userProfileId) {
        return userProfileDataAccessService
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not found.", userProfileId)));
    }

    private void isFileEmpty(MultipartFile file) {
        if(file.isEmpty()){
            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
        }
    }

    private boolean isFileAnImage(MultipartFile file){
        String mimeType = file.getContentType();
        if(mimeType.startsWith("image")){
            return true;
        }else{
            throw new IllegalStateException("Must be an image!");
        }
    }
}
