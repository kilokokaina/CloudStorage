package com.work.cloudstorage.service;

import com.work.cloudstorage.model.User;

public interface UserService {

    User save(User user);
    User findById(Long userId);
    User findByUsername(String username);
    User delete(User user);
    void deleteById(Long userId);

}
