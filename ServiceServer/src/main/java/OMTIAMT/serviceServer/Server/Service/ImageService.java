package OMTIAMT.serviceServer.Server.Service;

import OMTIAMT.serviceServer.Server.Model.ImgurRes;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.http.HttpResponse;
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

    public Boolean sendImage(String productId, String image){

        final String uri = "https://www.covidtector.tk:8080/product?productId={id}&image={image}";
        Map<String, String> vars = new HashMap<>();
        vars.put("id", productId);
        vars.put("image", image);

        System.out.println(productId);
        System.out.println(image);

        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.postForObject(uri, null,  ResponseEntity.class, vars);
        } catch (HttpMessageConversionException e) {
            System.out.println(e.toString());
            return true;
        } catch (Exception e){
            System.out.println("Something bad happened in post to Middleware");
            return false;
        }
        return true;
    }

    public ResponseEntity<String> checkProduct(String productId, String clientToken){

        final String uri = "https://www.covidtector.tk:8080/product?productId={id}&clientToken={token}";
        Map<String, String> vars = new HashMap<>();
        vars.put("id", productId);
        vars.put("token", clientToken);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(clientToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity <HttpResponse> entity = new HttpEntity<HttpResponse>(headers);

        try {
//            System.out.println("Gonna try to apelate endpoint");
//
//            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                    .path(uri)
//                    .buildAndExpand(vars)
//                    .toUri();
//
//            ResponseEntity.status(CREATED).header(HttpHeaders.AUTHORIZATION, clientToken).build();


            ResponseEntity responseEntity = restTemplate.getForEntity(uri, String.class, vars);

//            responseEntity = restTemplate.exchange(uri, HttpMethod.GET, entity, HttpResponse.class, vars);
//            System.out.println("apelated endpoint");

        }catch(HttpStatusCodeException e) {
            System.out.println(e);
            return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders())
                .body(e.getResponseBodyAsString());
        }

        return new ResponseEntity(HttpStatus.OK);
    }

}