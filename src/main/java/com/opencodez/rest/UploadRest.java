package com.opencodez.rest;

import com.opencodez.model.Archivos;
import com.opencodez.util.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.MultipartConfigElement;
import java.util.List;

@RestController
public class UploadRest {

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSizePerFile(52428800);
        return resolver;
    }

    @Bean
    @Order(0)
    public MultipartFilter multipartFilter() {
        MultipartFilter multipartFilter = new MultipartFilter();
        multipartFilter.setMultipartResolverBeanName("multipartResolver");
        return multipartFilter;
    }


    @Autowired
    private FileSystemStorageService storageService;

    @RequestMapping(value = "/files/upload", method = RequestMethod.POST)
    public ResponseEntity<String> handleFileUpload(@RequestParam("archivo") Archivos archivos,@RequestParam("file")MultipartFile[] files ) {

        System.out.println(".....");
        System.out.println("Grabando .....");
        System.out.println("Archivos "+archivos.toString());
        System.out.println("Files "+files);

//        for(int i=0;i<archivos.size();i++){
//            Archivos archivo = archivos.get(i);
//            MultipartFile file= files[i];
//            storageService.store(archivo,file);
//        }



        //redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return ResponseEntity.ok().body("");

    }
}
