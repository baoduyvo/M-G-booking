package com.vtnq.web.Helper;

import java.util.UUID;

public class FileHelper {
    public static String generateImageName(String orginalName){
        String uniqueId= UUID.randomUUID().toString();
        String extension=orginalName.substring(orginalName.lastIndexOf("."));
        return uniqueId+extension;
    }
}
