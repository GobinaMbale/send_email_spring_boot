package com.eda.sendemail.service.impl;

import com.eda.sendemail.dto.MailDto;
import com.eda.sendemail.dto.MimeMessageHelperDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final String UTF_8_ENCODING = "UTF-8";
    private static final String EMAIL_TEMPLATE = "emailTemplate";
    private static final String TEXT_HTML_ENCODING = "text/html";
    private final TemplateEngine templateEngine;
    private final JavaMailSender emailSender;
    @Value("${spring.mail.verify.host}")
    private String host;

    @Override
    @Async
    public void sendSimpleMailMessage(MailDto mailDto) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(mailDto.getSubject());
            simpleMailMessage.setFrom(mailDto.getFrom());
            simpleMailMessage.setTo(mailDto.getTo());
            simpleMailMessage.setText(mailDto.getMessage());
            emailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithAttachments(MailDto mailDto) {
        try {
            MimeMessage mimeMessage = getMimeMessage();
            MimeMessageHelper helper = createMimeMessageHelper(mimeMessage, new MimeMessageHelperDto(mailDto.getTo(), mailDto.getSubject(), mailDto.getFrom(),
                    mailDto.getMessage(), false, true));
            // TODO Add attachments
            addAttachments(helper, mailDto.getFiles(), false);
            emailSender.send(mimeMessage);
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithEmbeddedImages(MailDto mailDto) {
        try {
            MimeMessage mimeMessage = getMimeMessage();
            MimeMessageHelper helper = createMimeMessageHelper(mimeMessage, new MimeMessageHelperDto(mailDto.getTo(), mailDto.getSubject(), mailDto.getFrom(),
                    mailDto.getMessage(), false, true));
            // TODO Add attachments
            addAttachments(helper, mailDto.getFiles(), true);
            emailSender.send(mimeMessage);
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(MailDto mailDto) {
        try {
            Context context = new Context();
            context.setVariables(Map.of("recipientName", mailDto.getRecipientName(), "url", mailDto.getMessage()));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            MimeMessage message = getMimeMessage();
            createMimeMessageHelper(message, new MimeMessageHelperDto(mailDto.getTo(), mailDto.getSubject(), mailDto.getFrom(), text, true, true));
            emailSender.send(message);
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmailWithEmbeddedFiles(MailDto mailDto) {
        try {

            MimeMessage mimeMessage = getMimeMessage();
            createMimeMessageHelper(mimeMessage, new MimeMessageHelperDto(mailDto.getTo(), mailDto.getSubject(), mailDto.getFrom(), "", false, false));

            Context context = new Context();
            context.setVariables(Map.of("recipientName", mailDto.getRecipientName(), "url", mailDto.getMessage()));
            String text = templateEngine.process(EMAIL_TEMPLATE, context);
            // TODO HTML email body
            MimeMultipart mimeMultipart = addMimeMultipart(text);

            // TODO Add images to the email body
            addImageBodyParts(mimeMultipart, mailDto.getFiles());

            mimeMessage.setContent(mimeMultipart);

            emailSender.send(mimeMessage);
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    private MimeMessage getMimeMessage() {
        return emailSender.createMimeMessage();
    }

    private String getContentId(String filename) {
        return "<" + filename + ">";
    }

    private void addImageBodyParts(MimeMultipart mimeMultipart, List<File> files) throws MessagingException {
        for(File file: files) {
            BodyPart imageBodyPart = new MimeBodyPart();
            DataSource dataSource = new FileDataSource(file.getPath());
            imageBodyPart.setDataHandler(new DataHandler(dataSource));
            imageBodyPart.setHeader("Content-ID", "image");
            mimeMultipart.addBodyPart(imageBodyPart);
        }
    }

    private MimeMultipart addMimeMultipart(String text) throws MessagingException {
        MimeMultipart mimeMultipart = new MimeMultipart("related");
        BodyPart messageBodyPart = new MimeBodyPart();
        mimeMultipart.addBodyPart(messageBodyPart);
        messageBodyPart.setContent(text, TEXT_HTML_ENCODING);
        return  mimeMultipart;
    }

    private void addAttachments(MimeMessageHelper helper, List<File> files, Boolean isUseOnlyImages) throws MessagingException {
        if(!isUseOnlyImages) {
            for (File file : files) {
                helper.addAttachment(file.getName(), file);
            }
        } else {
            for(File file : files) {
                helper.addInline(getContentId(file.getName()), file);
            }
        }
    }

    private MimeMessageHelper createMimeMessageHelper(MimeMessage message, MimeMessageHelperDto mimeMessageHelperDto) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);
        helper.setPriority(1);
        helper.setSubject(mimeMessageHelperDto.getSubject());
        helper.setFrom(mimeMessageHelperDto.getFrom());
        helper.setTo(mimeMessageHelperDto.getTo());
        if(Boolean.TRUE.equals(mimeMessageHelperDto.getTextIsUse())) {
            helper.setText(mimeMessageHelperDto.getText(), mimeMessageHelperDto.getIsUseHtml());
        }

        return helper;
    }
}
