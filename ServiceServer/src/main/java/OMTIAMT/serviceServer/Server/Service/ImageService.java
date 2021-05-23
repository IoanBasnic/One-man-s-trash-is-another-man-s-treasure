package OMTIAMT.serviceServer.Server.Service;

import OMTIAMT.serviceServer.Server.Model.ImgurRes;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class ImageService {

    public static ByteArrayResource getUserFileResource(byte[] bytes) throws IOException {
        return new ByteArrayResource(bytes);
    }

    public  ResponseEntity<ImgurRes> writeToStore(byte[] fileBytes) throws Exception {
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

        return response;
    }

    public ResponseEntity<Object> sendImage(String productId, String image){

        final String uri = "https://www.covidtector.tk:8080/product?productId={id}&image={image}";
        Map<String, String> vars = new HashMap<>();
        vars.put("id", productId);
        vars.put("image", image);

        System.out.println(productId);
        System.out.println(image);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity responseEntity = restTemplate.postForObject(uri, vars, ResponseEntity.class);

        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {

            if (HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {

                System.out.println("NOT FOUND");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<String> checkProduct(String productId, String clientToken){

        final String uri = "https://www.covidtector.tk:8080/product?productId={id}";
        Map<String, String> vars = new HashMap<>();
        vars.put("id", productId);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(clientToken);

        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity responseEntity;

        try {
            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class, vars);
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (HttpStatus.NOT_FOUND.equals(responseEntity.getStatusCode())) {

            System.out.println("NOT FOUND");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}