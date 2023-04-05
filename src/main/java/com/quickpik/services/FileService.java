package com.quickpik.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	// Returns an input stream for a resource with the specified path and name
	InputStream getResource(String path, String name) throws FileNotFoundException;
	
	// uploads an image file to the specified path and returns the file name
	String uploadImage(MultipartFile file, String path) throws IOException;
}
