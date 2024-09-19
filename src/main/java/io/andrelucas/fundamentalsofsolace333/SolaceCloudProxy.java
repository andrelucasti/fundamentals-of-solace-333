package io.andrelucas.fundamentalsofsolace333;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class SolaceCloudProxy {

    @Value("${solace.rest.host}")
    private String host;

    @Value("${solace.username}")
    private String username;

    @Value("${solace.password}")
    private String password;


    private HttpHeaders httpHeaders;

    @PostConstruct
    public void init(){
        httpHeaders = new HttpHeaders() {{
            var auth = username + ":" + password;
            var encode = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
            var authHeader = "Basic " + new String(encode);
            set("Authorization", authHeader);
            set("Content-Type", "application/json");
        }};
    }


    @PostMapping("/solace/cloud/proxy")
    @ResponseBody
    public ResponseEntity<Void> login(@RequestBody SolaceUser solaceUser) {
        var restTemplate = new RestTemplate();

        var request = new HttpEntity<>(solaceUser, httpHeaders);

        restTemplate.postForObject(host + "/LOGIN/MSG/REQUEST", request, String.class);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
