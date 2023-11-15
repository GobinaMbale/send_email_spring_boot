package com.eda.sendemail.service.impl;

import com.eda.sendemail.service.EmailService;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;

import static com.eda.sendemail.utils.EmailUtils.getVerificationUrl;
import static com.eda.sendemail.utils.EmailUtils.getEmailMessage;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";
    private static final String ACCOUNT_VERIFICATION = "Account Verification";
    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String EMAILTEMPLATE = "emailtemplate";
    private static final String TEXT_HTML_ENCODING = "text/html";
    private static final String USER_HOME = "user.home";
    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;
    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${variable.img_1}")
    private String img1;
    @Value("${variable.img_2}")
    private String img2;
    @Value("${variable.img_logo}")
    private String imgLogo;
    @Value("${variable.document_file}")
    private String documentFile;

    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setText(getEmailMessage(name, host, token));
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = createMimeMessageHelper(message, to, ACCOUNT_VERIFICATION, fromEmail, getEmailMessage(name, host, token), false, true);
            // TODO Add attachments
            addAttachments(helper);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedImages(String name, String to, String token) {
        try {
            MimeMessage message = getMimeMessage();
            MimeMessageHelper helper = createMimeMessageHelper(message, to, NEW_USER_ACCOUNT_VERIFICATION, fromEmail, getEmailMessage(name, host, token), false, true);
            // TODO Add attachments
            addEmbeddedImages(helper);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAILTEMPLATE, context);
            MimeMessage message = getMimeMessage();
            createMimeMessageHelper(message, to, NEW_USER_ACCOUNT_VERIFICATION, fromEmail, text, true, true);
            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(String name, String to, String token) {
        try {

            MimeMessage message = getMimeMessage();
            createMimeMessageHelper(message, to, NEW_USER_ACCOUNT_VERIFICATION, fromEmail, "", false, false);

            Context context = new Context();
            context.setVariables(Map.of("name", name, "url", getVerificationUrl(host, token)));
            String text = templateEngine.process(EMAILTEMPLATE, context);
            // TODO HTML email body
            MimeMultipart mimeMultipart = addMimeMultipart(text);

            // TODO Add images to the email body
            addImageBodyPart(mimeMultipart);

            message.setContent(mimeMultipart);

            emailSender.send(message);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String filename) {
        return "<" + filename + ">";
    }

    private void addImageBodyPart(MimeMultipart mimeMultipart) throws MessagingException {
        BodyPart imageBodyPart = new MimeBodyPart();
        DataSource dataSource = new FileDataSource(System.getProperty(USER_HOME)  + imgLogo);
        imageBodyPart.setDataHandler(new DataHandler(dataSource));
        imageBodyPart.setHeader("Content-ID", "image");
        mimeMultipart.addBodyPart(imageBodyPart);
    }

    private MimeMultipart addMimeMultipart(String text) throws MessagingException {
        MimeMultipart mimeMultipart = new MimeMultipart("related");
        BodyPart messageBodyPart = new MimeBodyPart();
        mimeMultipart.addBodyPart(messageBodyPart);
        messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
        return  mimeMultipart;
    }

    private void addAttachments(MimeMessageHelper helper) throws MessagingException {
        FileSystemResource woman = new FileSystemResource(new File(System.getProperty(USER_HOME) + img1));
        FileSystemResource icon = new FileSystemResource(new File(System.getProperty(USER_HOME) + img2));
        FileSystemResource data = new FileSystemResource(new File(System.getProperty(USER_HOME) + documentFile));

        helper.addAttachment(woman.getFilename(), woman);
        helper.addAttachment(icon.getFilename(), icon);
        helper.addAttachment(data.getFilename(), data);
    }

    private void addEmbeddedImages(MimeMessageHelper helper) throws MessagingException {
        FileSystemResource woman = new FileSystemResource(new File(System.getProperty(USER_HOME) + img1));
        FileSystemResource icon = new FileSystemResource(new File(System.getProperty(USER_HOME) + img2));
        FileSystemResource data = new FileSystemResource(new File(System.getProperty(USER_HOME) + documentFile));

        helper.addInline(getContentId(woman.getFilename()), woman);
        helper.addInline(getContentId(icon.getFilename()), icon);
        helper.addInline(getContentId(data.getFilename()), data);
    }

    private MimeMessageHelper createMimeMessageHelper(MimeMessage message, String to, String subject, String from, String text, Boolean value, Boolean textIsUse) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
        helper.setPriority(1);
        helper.setSubject(subject);
        helper.setFrom(from);
        helper.setTo(to);
        if(Boolean.TRUE.equals(textIsUse)) {
            helper.setText(text, value);
        }

        return helper;
    }
}
