package OMTIAMT.serviceServer.Server.Controller;



import OMTIAMT.serviceServer.Server.Model.ImgurRes;
import OMTIAMT.serviceServer.Server.Service.ImageService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.SocketUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    public ImageService imageService;

    private String productId;

    @PostMapping("/uploadFile")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        try {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                System.out.println(fileName);

                byte[] fileContent = file.getBytes();

                if (fileContent == null) {
                    return new ResponseEntity<>("File empty", HttpStatus.NOT_ACCEPTABLE);
                }

                ResponseEntity<ImgurRes> response = imageService.writeToStore(fileContent);

                if (!(response.getStatusCodeValue() == 200)) {
                    return new ResponseEntity<>("Something happened while uploading image", HttpStatus.BAD_REQUEST);
                }

                String image = response.getBody().getData().getLink();

                ResponseEntity responseEntity = imageService.sendImage(this.productId,image);

                return new ResponseEntity<>("OK", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("File type not accepted", HttpStatus.BAD_REQUEST);
        }
    }
    public static ByteArrayResource getUserFileResource(byte[] bytes) throws IOException {
        return new ByteArrayResource(bytes);
    }

    @GetMapping(value="/checkProduct")
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

    @GetMapping(params = {"productId", "clientToken"})
    public ResponseEntity<Object> productExistByTokenAndId(@PathParam("productId") String productId, String clientToken){

        System.out.println(productId);
        System.out.println(clientToken);

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}