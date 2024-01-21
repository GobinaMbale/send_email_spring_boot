package com.eda.sendemail.service;

import com.eda.sendemail.dto.MailDto;

public interface EmailService {
    void sendSimpleMailMessage(MailDto mailDto);
    void sendMimeMessageWithAttachments(MailDto mailDto);
    void sendMimeMessageWithEmbeddedImages(MailDto mailDto);
    void sendHtmlEmail(MailDto mailDto);
    void sendHtmlEmailWithEmbeddedFiles(MailDto mailDto);
}
