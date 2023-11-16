package com.eda.sendemail.service.impl;

import com.eda.sendemail.domain.Confirmation;
import com.eda.sendemail.domain.User;
import com.eda.sendemail.repository.ConfirmationRepository;
import com.eda.sendemail.repository.UserRepository;
import com.eda.sendemail.service.EmailService;
import com.eda.sendemail.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    @Override
    public User saveUserAndSendSimpleMailMessage(User user) {
        Confirmation confirmation =  saveUser(user);
        // TODO send email to user with token
        emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
        return user;
    }

    @Override
    public User saveUserAndSendMimeMessageWithAttachments(User user) {
        Confirmation confirmation =  saveUser(user);
        // TODO send email to user with token
        emailService.sendMimeMessageWithAttachments(user.getName(), user.getEmail(), confirmation.getToken());
        return user;
    }

    @Override
    public User saveUserAndSendMimeMessageWithEmbeddedImages(User user) {
        Confirmation confirmation =  saveUser(user);
        // TODO send email to user with token
        emailService.sendMimeMessageWithEmbeddedImages(user.getName(), user.getEmail(), confirmation.getToken());
        return user;
    }

    @Override
    public User saveUserAndSendHtmlEmail(User user) {
        Confirmation confirmation =  saveUser(user);
        // TODO send email to user with token
        emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
        return user;
    }

    @Override
    public User saveUserAndSendHtmlEmailWithEmbeddedFiles(User user) {
        Confirmation confirmation =  saveUser(user);
        // TODO send email to user with token
        emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        return user;
    }

    private Confirmation saveUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setEnabled(false);
        userRepository.save(user);
        // TODO save confirmation token
        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);
        return  confirmation;
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
}
