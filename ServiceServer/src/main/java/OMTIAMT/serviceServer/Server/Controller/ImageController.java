package OMTIAMT.serviceServer.Server.Controller;



import OMTIAMT.serviceServer.Server.Model.ImgurRes;
import OMTIAMT.serviceServer.Server.Service.ImageService;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    public ImageService imageService;

    @PostMapping("/uploadFile")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile[] file) throws Exception {
        try {
            for(MultipartFile f : file){
                String fileName = StringUtils.cleanPath(f.getOriginalFilename());
                System.out.println(fileName);

                byte[] fileContent = f.getBytes();

                if(fileContent != null){
                    System.out.println("File is not empty");
                }

                System.out.println(writeToStore(fileContent));
            }

            return new ResponseEntity<>("OK", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("File type not accepted",HttpStatus.BAD_REQUEST);
        }
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//        System.out.println(fileName);
//
//        byte[] fileContent = file.getBytes();
//
//        if(fileContent != null){
//            System.out.println("File is not empty");
//        }
//
//        System.out.println(writeToStore(fileContent));

//        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    public String writeToStore(byte[] fileBytes) throws Exception {
        String clientId="69590164bdb6010";
        final String uri = "https://api.imgur.com/3/image";

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("image", getUserFileResource(fileBytes));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", "Client-ID "+clientId);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ImgurRes> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, ImgurRes.class);
        System.out.println("response status: " + response.getStatusCode()); // it should return 200
        System.out.println("response body: " + response.getBody().getData().getLink()); // it should return link of your uploaded image

        return response.getBody().getData().getLink();
    }

    public static ByteArrayResource getUserFileResource(byte[] bytes) throws IOException {
        return new ByteArrayResource(bytes);
    }
}