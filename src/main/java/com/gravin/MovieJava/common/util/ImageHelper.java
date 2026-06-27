package com.gravin.MovieJava.common.util;

import java.util.Base64;

public class ImageHelper {
    public enum ImageType {
        PNG("png");

        private final String mimeType;

        ImageType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getMimeType() {
            return mimeType;
        }
    }

    private static final String DATA_URI_PREFIX = "data:image/%s;base64,";

    public static String convertToDataUri(byte[] bytes, ImageType imageType) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        String base64 = Base64.getEncoder().encodeToString(bytes);
        String prefix = DATA_URI_PREFIX.formatted(imageType);

        return prefix + base64;
    }

    public static String convertToDataUri(byte[] bytes) {
        return ImageHelper.convertToDataUri(bytes, ImageType.PNG);
    }
}
