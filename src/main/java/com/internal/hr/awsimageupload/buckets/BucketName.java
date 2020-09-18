package com.internal.hr.awsimageupload.buckets;

public enum BucketName {

    PROFILE_IMAGE("markachus-image-upload-test");

    private final String bucketName;


    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
