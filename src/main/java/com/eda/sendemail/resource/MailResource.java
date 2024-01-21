package com.eda.sendemail.resource;

import com.eda.sendemail.dto.HttpResponse;
import com.eda.sendemail.dto.MailDto;
import com.eda.sendemail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailResource {
    private final EmailService emailService;

    @PostMapping("/sendSimpleMailMessage")
    public ResponseEntity<HttpResponse> sendSimpleMailMessage(@RequestBody MailDto mailDto) {
        emailService.sendSimpleMailMessage(mailDto);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "sendSimpleMailMessage",
                        "EMAIL SEND WITH SUCCESS")
        );
    }

    @PostMapping("/sendHtmlEmail")
    public ResponseEntity<HttpResponse> sendHtmlEmail(@RequestBody MailDto mailDto) {
        emailService.sendHtmlEmail(mailDto);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "sendHtmlEmail",
                        " HTML EMAIL SEND WITH SUCCESS")
        );
    }

    @PostMapping("/sendHtmlEmailWithEmbeddedFiles")
    public ResponseEntity<HttpResponse> sendHtmlEmailWithEmbeddedFiles(@RequestBody MailDto mailDto) {
        emailService.sendHtmlEmailWithEmbeddedFiles(mailDto);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "sendHtmlEmailWithEmbeddedFiles",
                        "EMAIL WITH EMBEDDED FILES SEND WITH SUCCESS")
        );
    }

    @PostMapping("/sendMimeMessageWithAttachments")
    public ResponseEntity<HttpResponse> sendMimeMessageWithAttachments(@RequestBody MailDto mailDto) {
        emailService.sendMimeMessageWithAttachments(mailDto);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "sendMimeMessageWithAttachments",
                        "EMAIL WITH ATTACHMENTS SEND WITH SUCCESS")
        );
    }

    @PostMapping("/sendMimeMessageWithEmbeddedImages")
    public ResponseEntity<HttpResponse> sendMimeMessageWithEmbeddedImages(@RequestBody MailDto mailDto) {
        emailService.sendMimeMessageWithEmbeddedImages(mailDto);
        return ResponseEntity.created(URI.create("")).body(
                responseApi( "sendMimeMessageWithEmbeddedImages",
                        "EMAIL WITH EMBEDDED IMAGES SEND WITH SUCCESS")
        );
    }

    private HttpResponse responseApi(String message, Object object) {
        return HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(Map.of("user", object))
                .message(message)
                .status(HttpStatus.OK)
                .statusCode(HttpStatus.OK.value())
                .build();
    }
}
