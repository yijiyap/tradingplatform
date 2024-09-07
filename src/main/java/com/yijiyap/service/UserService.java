package com.yijiyap.service;

import com.yijiyap.modal.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findUserProfileByJwt(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
    User findUserById(Long userId) throws Exception;
    User updatePassword(User user, String newPassword);

}
