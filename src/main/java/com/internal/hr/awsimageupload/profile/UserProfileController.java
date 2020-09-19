package com.internal.hr.awsimageupload.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user-profile")
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public List<UserProfile> getUserProfiles(){
        return userProfileService.getUserProfiles();
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = "{userProfileId}/image/upload"
    )
    public void uploadUserProfileImage(
            @PathVariable("userProfileId") UUID userProfileId,
            @RequestParam("file")MultipartFile file){  //ovo ime "file" mora bit isto kao u
                                                       //ReactJS u liniji formData.append("file", file");

        userProfileService.uploadUserProfileImage(userProfileId, file);

    }




}
