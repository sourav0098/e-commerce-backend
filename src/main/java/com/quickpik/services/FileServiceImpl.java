package com.quickpik.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quickpik.exception.BadApiRequestException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		String fullPath=path+File.separator+name;
		InputStream inputStream=new FileInputStream(fullPath);
		return inputStream;
	}

	@Override
	public String uploadImage(MultipartFile file, String path) throws IOException {
		String originalFileName = file.getOriginalFilename();
		log.info("Filename {}", originalFileName);

		String fileName = UUID.randomUUID().toString();
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		String imageName = fileName + extension;

		String fullPathWithImageName = path + File.separator + imageName;
		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
				|| extension.equalsIgnoreCase(".jpeg")) {
			File folder = new File(path);
			if (!folder.exists()) {
				// create folder
				folder.mkdirs();
			}
			// upload file
			Files.copy(file.getInputStream(), Paths.get(fullPathWithImageName));
			return imageName;

		} else {
			throw new BadApiRequestException("Invalid image");
		}
	}

}
