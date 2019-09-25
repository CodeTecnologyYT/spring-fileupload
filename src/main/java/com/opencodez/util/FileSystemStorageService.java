package com.opencodez.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import com.opencodez.controller.UploadController;
import com.opencodez.model.Archivos;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Service
public class FileSystemStorageService {
	private static List<Archivos> ltsArchivos;
	static {

		ltsArchivos=new ArrayList<>();
	}

	private Path uploadLocation;
	
	@PostConstruct
	public void init() {
		this.uploadLocation = Paths.get(Constants.UPLOAD_LOCATION+(new SimpleDateFormat("MM-dd-yyyy").format(new Date())).toString());
		try {
			Files.createDirectories(uploadLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage", e);
		}
	}
	
	public void store(Archivos archivo,MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		System.out.println("File: "+filename);
		archivo.setHrefText(filename.toString());
		try {
			if (file.isEmpty()) {
				throw new RuntimeException("Failed to store empty file " + filename);
			}
			
			// This is a security check
			if (filename.contains("..")) {
				throw new RuntimeException("Cannot store file with relative path outside current directory " + filename);
			}
			
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, this.uploadLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
				archivo.setHref(MvcUriComponentsBuilder
						.fromMethodName(UploadController.class, "serveFile", String.valueOf(ltsArchivos.size()))
						.build()
						.toString());
			}
			String url=uploadLocation.resolve(filename).normalize().toString();
			System.out.println(url);
			archivo.setUrl(url);

			ltsArchivos.add(archivo);
		} catch (IOException e) {
			throw new RuntimeException("Failed to store file " + filename, e);
		}
	}

	public Resource loadAsResource(Integer idArchivo) {
		try {
			Path file = Paths.get(ltsArchivos.get(idArchivo).getUrl());
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read file: " + ltsArchivos.get(idArchivo).getDescripcion());
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Could not read file: " + ltsArchivos.get(idArchivo).getDescripcion(), e);
		}
	}

	public List<Archivos> listSourceFiles() throws IOException {
		return ltsArchivos;
	}
	
	public Path getUploadLocation() {
		return uploadLocation;
	}
}

