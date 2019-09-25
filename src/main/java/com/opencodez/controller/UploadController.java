package com.opencodez.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.opencodez.model.Archivos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.opencodez.model.HRefModel;
import com.opencodez.util.FileSystemStorageService;

@Controller
public class UploadController {
	
	@Autowired
	private FileSystemStorageService storageService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView upload() {
		return new ModelAndView("upload");
	}


	@RequestMapping(value = "/files/list", method = RequestMethod.GET)
	public String listFiles(Model model) {
		List<Archivos> archivos = new ArrayList<>();

		try {
            archivos = storageService.listSourceFiles();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("listOfEntries", archivos);
		return "file_list :: urlFileList";
	}


	@GetMapping("/files/{id}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable Integer id) {
		Resource file = storageService.loadAsResource(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
//	@RequestMapping(value = "/files/upload", method = RequestMethod.POST)
//	public String handleFileUpload(@RequestParam("file") MultipartFile[] files, RedirectAttributes redirectAttributes) {
//		for(MultipartFile file : files )
//		storageService.store(file);
//		//redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
//		return "redirect:/";
//	}

	/*@RequestMapping(value = "/files/upload", method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("archivos") List<Archivos> archivos, RedirectAttributes redirectAttributes) {
		for(Archivos archivo :  archivos){
			System.out.println("Nombre: "+archivo.getNombre());
			System.out.println("Grabando .....");
			storageService.store(archivo.getFile());
		}
		//redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
		return "redirect:/";
	}*/
}
