package com.itranswarp.learnjava.service.impl;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.itranswarp.learnjava.service.StorageService;

@Component
@ConditionalOnProperty(value = "storage.type", havingValue = "aliyun")
public class AliyunStorageService implements StorageService {

	@Value("${storage.aliyun.bucket:}")
	String bucket;

	@Value("${storage.aliyun.access-key:}")
	String accessKey;

	@Value("${storage.aliyun.access-secret:}")
	String accessSecret;

	final Logger logger = LoggerFactory.getLogger(getClass());

	@PostConstruct
	public void init() {
		// TODO:
		logger.info("Initializing Aliyun storage...");
	}

	@Override
	public InputStream openInputStream(String uri) throws IOException {
		// TODO:
		throw new IOException("File not found: " + uri);
	}

	@Override
	public String store(String extName, InputStream input) throws IOException {
		// TODO:
		throw new IOException("Unable to access cloud storage.");
	}
}
