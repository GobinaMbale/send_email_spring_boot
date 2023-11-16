package com.eda.sendemail.resource;

import com.eda.sendemail.domain.HttpResponse;
import com.eda.sendemail.domain.User;
import com.eda.sendemail.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @PostMapping("/createUserAndSendSimpleMailMessage")
    public ResponseEntity<HttpResponse> createUserAndSendSimpleMailMessage(@RequestBody User user) {
        User newUser = userService.saveUserAndSendSimpleMailMessage(user);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "createUserAndSendSimpleMailMessage", "user",
                        newUser, HttpStatus.CREATED, HttpStatus.CREATED.value())
        );
    }

    @PostMapping("/createUserAndSendHtmlEmail")
    public ResponseEntity<HttpResponse> createUserAndSendHtmlEmail(@RequestBody User user) {
        User newUser = userService.saveUserAndSendHtmlEmail(user);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "createUserAndSendHtmlEmail", "user",
                        newUser, HttpStatus.CREATED, HttpStatus.CREATED.value())
        );
    }

    @PostMapping("/createUserAndSendHtmlEmailWithEmbeddedFiles")
    public ResponseEntity<HttpResponse> createUserAndSendHtmlEmailWithEmbeddedFiles(@RequestBody User user) {
        User newUser = userService.saveUserAndSendHtmlEmailWithEmbeddedFiles(user);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "createUserAndSendHtmlEmailWithEmbeddedFiles", "user",
                        newUser, HttpStatus.CREATED, HttpStatus.CREATED.value())
        );
    }

    @PostMapping("/createUserAndSendMimeMessageWithAttachments")
    public ResponseEntity<HttpResponse> createUserAndSendMimeMessageWithAttachments(@RequestBody User user) {
        User newUser = userService.saveUserAndSendMimeMessageWithAttachments(user);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "createUserAndSendMimeMessageWithAttachments", "user",
                        newUser, HttpStatus.CREATED, HttpStatus.CREATED.value())
        );
    }

    @PostMapping("/createUserAndSendMimeMessageWithEmbeddedImages")
    public ResponseEntity<HttpResponse> createUserAndSendMimeMessageWithEmbeddedImages(@RequestBody User user) {
        User newUser = userService.saveUserAndSendMimeMessageWithEmbeddedImages(user);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "createUserAndSendMimeMessageWithEmbeddedImages", "user",
                        newUser, HttpStatus.CREATED, HttpStatus.CREATED.value())
        );
    }

    @GetMapping("/confirmUserAccount")
    public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam("token") String token) {
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok().body(
                responseApi( "Account  Verified", "success",
                        isSuccess, HttpStatus.OK, HttpStatus.OK.value())
        );
    }

    private HttpResponse responseApi(String message, String mapObject, Object object, HttpStatus httpStatus, int statusCode) {
        return HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(Map.of(mapObject, object))
                .message(message)
                .status(httpStatus)
                .statusCode(statusCode)
                .build();
    }
}
