package com.quickpik.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	InputStream getResource(String path, String name) throws FileNotFoundException;
	String uploadImage(MultipartFile file, String path) throws IOException;
	
}
