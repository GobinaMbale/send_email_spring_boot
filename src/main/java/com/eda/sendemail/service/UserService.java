package com.eda.sendemail.service;

import com.eda.sendemail.domain.User;

public interface UserService {
    User saveUser(User user);
    Boolean verifyToken(String token);
}
