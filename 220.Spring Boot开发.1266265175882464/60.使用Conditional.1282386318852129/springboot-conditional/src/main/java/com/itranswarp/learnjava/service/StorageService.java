package com.itranswarp.learnjava.service;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

	InputStream openInputStream(String uri) throws IOException;

	String store(String extName, InputStream input) throws IOException;
}
