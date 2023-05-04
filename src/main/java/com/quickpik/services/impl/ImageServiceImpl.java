package com.quickpik.services.impl;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.quickpik.exception.BadApiRequestException;
import com.quickpik.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

	@Autowired
	private AmazonS3 s3client;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Value("${aws.s3.region}")
	private String region;

	
	@Override
	public String getImageUrl(String filename, String path) {
		String objectKey = path + "/" + filename;
		String bucketUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com";
		return bucketUrl + "/" + objectKey;
	}

	@Override
	public String uploadImage(MultipartFile file, String path) {
		String originalFileName = file.getOriginalFilename();
		String fileName = UUID.randomUUID().toString();
		String extension = FilenameUtils.getExtension(originalFileName);
		String imageName = fileName + "." + extension;
		String fullPathWithImageName = path + "/" + imageName;

		try {
			if (extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg")
					|| extension.equalsIgnoreCase("jpeg")) {
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentType(file.getContentType());
				metadata.setContentLength(file.getSize());
				if (file.getSize() > 2 * 1024 * 1024) {
					throw new BadApiRequestException("Image size cant be larger than 2MB");
				}
				s3client.putObject(bucketName, fullPathWithImageName, file.getInputStream(), metadata);
				return imageName;
			} else {
				throw new BadApiRequestException("Invalid image");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BadApiRequestException("Failed to upload image");
		}
	}

	@Override
	public void deleteImage(String filename) {
		s3client.deleteObject(bucketName, filename);
	}

}
