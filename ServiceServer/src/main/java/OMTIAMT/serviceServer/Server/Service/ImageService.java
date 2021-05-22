package OMTIAMT.serviceServer.Server.Service;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class ImageService {

   // public final String storageDirectoryPath = "C:\\Users\\Ahmed\\Desktop\\mediumImages";

    public ResponseEntity uploadToLocalFileSystem(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

//        Path storageDirectory = Paths.get(storageDirectoryPath);
//
//        if(!Files.exists(storageDirectory)){
//            try {
//                Files.createDirectories(storageDirectory);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//
//        Path destination = Paths.get(storageDirectory.toString() + "\\" + fileName);
//
//        try {
//            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("api/images/getImage/")
//                .path(fileName)
//                .toUriString();
//
        return ResponseEntity.ok("OK");
    }

}