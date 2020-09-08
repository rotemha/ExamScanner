package com.example.examscanner.image_processing;

public class ImageProcessingError extends RuntimeException {

    public ImageProcessingError(String given_input_is_null) {
        super(given_input_is_null);
    }

    public ImageProcessingError(Exception e) {
        super(e);
    }
}
