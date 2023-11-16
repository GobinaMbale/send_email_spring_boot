package com.eda.sendemail.service;

import com.eda.sendemail.domain.User;

public interface UserService {
    User saveUserAndSendSimpleMailMessage(User user);
    User saveUserAndSendMimeMessageWithAttachments(User user);
    User saveUserAndSendMimeMessageWithEmbeddedImages(User user);
    User saveUserAndSendHtmlEmail(User user);
    User saveUserAndSendHtmlEmailWithEmbeddedFiles(User user);
    Boolean verifyToken(String token);
}
