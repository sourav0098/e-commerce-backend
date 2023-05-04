package com.quickpik.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	String getImageUrl(String filename, String path);
	String uploadImage(MultipartFile file, String path) throws IOException;
	void deleteImage(String filename);
}