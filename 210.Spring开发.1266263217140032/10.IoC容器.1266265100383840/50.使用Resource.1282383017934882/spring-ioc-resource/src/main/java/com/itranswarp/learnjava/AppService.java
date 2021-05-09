package com.itranswarp.learnjava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class AppService {

	@Value("1")
	private int version;

	@Value("classpath:/logo.txt")
	private Resource resource;

	private String logo;

	@PostConstruct
	public void init() throws IOException {
		try (var reader = new BufferedReader(
				new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
			this.logo = reader.lines().collect(Collectors.joining("\n"));
		}
	}

	public void printLogo() {
		System.out.println(logo);
		System.out.println("app.version: " + version);
	}
}
