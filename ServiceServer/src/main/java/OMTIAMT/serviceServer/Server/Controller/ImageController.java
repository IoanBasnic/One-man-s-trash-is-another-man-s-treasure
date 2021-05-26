package OMTIAMT.serviceServer.Server.Controller;

import OMTIAMT.serviceServer.Server.Model.ImgurRes;
import OMTIAMT.serviceServer.Server.Service.ImageService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    public ImageService imageService;

    private String productId;


    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        System.out.println("called endpoint");

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            System.out.println(fileName);

            byte[] fileContent = file.getBytes();

            if (fileContent == null) {
                return new ResponseEntity("{\"message\": \"File empty\"}", HttpStatus.NOT_ACCEPTABLE);
            }

            ResponseEntity<ImgurRes> response = imageService.writeToStore(fileContent);

            if (response.getStatusCodeValue() != 200) {
                return new ResponseEntity("{\"message\": \"Something happened while uploading image\"}", HttpStatus.BAD_REQUEST);
            }

            String image = response.getBody().getData().getLink();

            if(imageService.sendImage(this.productId,image))
                return new ResponseEntity("{\"message\": \"OK\"}", HttpStatus.OK);
            else
                return new ResponseEntity("{\"message\": \"File type not accepted\"}", HttpStatus.BAD_REQUEST);


        } catch (Exception e) {
            System.out.println(e);

            return new ResponseEntity("{\"message\": \"File type not accepted\"}", HttpStatus.BAD_REQUEST);
        }
    }
    public static ByteArrayResource getUserFileResource(byte[] bytes) throws IOException {
        return new ByteArrayResource(bytes);
    }

    @PostMapping(value="/checkProduct")
    public ResponseEntity<Object> productExistById(@RequestBody String data){

        JsonObject dataJson = (JsonObject) JsonParser.parseString(data);
        String clientToken = dataJson.get("clientToken").getAsString();
        this.productId = dataJson.get("productId").getAsString();

        ResponseEntity responseEntity = imageService.checkProduct(productId,clientToken);

        System.out.println(getProductId());

        return responseEntity;
    }

    public String getProductId(){
        return this.productId;
    }

//    @GetMapping(params = {"productId", "clientToken"})
//    public ResponseEntity<Object> productExistByTokenAndId(@PathParam("productId") String productId, String clientToken){
//
//        System.out.println(productId);
//        System.out.println(clientToken);
//
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
}