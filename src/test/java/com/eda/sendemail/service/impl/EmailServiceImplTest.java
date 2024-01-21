package com.eda.sendemail.service.impl;

import com.eda.sendemail.dto.MailDto;
import com.eda.sendemail.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class EmailServiceImplTest {

    @Mock
    private EmailService emailService;

    private static final String USER_HOME = "user.home";
    private final String from = "gobinambale@gmail.com";
    private final String to = "equipdevelopacademy@gmail.com";

    @Test
    void sendSimpleMailMessage() {
        MailDto mailDto = new MailDto();
        mailDto.setSubject("SEND SIMPLE MAIL MESSAGE");
        mailDto.setFrom(from);
        mailDto.setTo(to);
        mailDto.setMessage("We're delighted to welcome you to our platform! Your account has been" +
                " successfully created, and you're now ready to explore all the exciting features our" +
                " platform has to offer.");
        emailService.sendSimpleMailMessage(mailDto);
    }

    @Test
    void sendMimeMessageWithAttachments() {
        MailDto mailDto = new MailDto();
        mailDto.setSubject("SEND MIME MESSAGE WITH ATTACHMENTS");
        mailDto.setFrom(from);
        mailDto.setTo(to);
        mailDto.setMessage("We're delighted to welcome you to our platform! Your account has been" +
                " successfully created, and you're now ready to explore all the exciting features our" +
                " platform has to offer.");
        mailDto.setFiles(getFiles());

        emailService.sendMimeMessageWithAttachments(mailDto);
    }

    @Test
    void sendMimeMessageWithEmbeddedImages() {
        MailDto mailDto = new MailDto();
        mailDto.setSubject("SEND MIME MESSAGE WITH EMBEDDED IMAGES");
        mailDto.setFrom(from);
        mailDto.setTo(to);
        mailDto.setMessage("We're delighted to welcome you to our platform! Your account has been" +
                " successfully created, and you're now ready to explore all the exciting features our" +
                " platform has to offer.");
        mailDto.setFiles(getFiles());

        emailService.sendMimeMessageWithEmbeddedImages(mailDto);
    }

    @Test
    void sendHtmlEmail() {
        MailDto mailDto = new MailDto();
        mailDto.setSubject("SEND HTML EMAIL");
        mailDto.setRecipientName("EQUIP DEVELOP ACADEMY");
        mailDto.setFrom(from);
        mailDto.setTo(to);
        mailDto.setMessage("We're delighted to welcome you to our platform! Your account has been" +
                " successfully created, and you're now ready to explore all the exciting features our" +
                " platform has to offer.");
        emailService.sendHtmlEmail(mailDto);
    }

    @Test
    void sendHtmlEmailWithEmbeddedFiles() {
        MailDto mailDto = new MailDto();
        mailDto.setSubject("SEND HTML EMAIL WITH EMBEDDED FILE");
        mailDto.setFrom(from);
        mailDto.setTo(to);
        mailDto.setMessage("We're delighted to welcome you to our platform! Your account has been" +
                " successfully created, and you're now ready to explore all the exciting features our" +
                " platform has to offer.");
        mailDto.setFiles(getFiles());

        emailService.sendHtmlEmailWithEmbeddedFiles(mailDto);
    }

    private List<File> getFiles() {
        FileSystemResource woman = new FileSystemResource(new File(System.getProperty(USER_HOME) + "/Downloads/images/woman.png"));
        FileSystemResource icon = new FileSystemResource(new File(System.getProperty(USER_HOME) + "/Downloads/images/icon.png"));
        FileSystemResource data = new FileSystemResource(new File(System.getProperty(USER_HOME) + "/Downloads/images/data.docx"));

        List<File> files = new ArrayList<>();
        files.add(woman.getFile());
        files.add(icon.getFile());
        files.add(data.getFile());

        return files;
    }
}
