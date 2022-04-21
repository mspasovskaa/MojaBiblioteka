package com.finki.library.service;

import com.finki.library.model.User;
import com.finki.library.model.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

public interface UserService {
    User findById(Long id);
    User create(String name, String username, String password, String email, Role role);
    User delete(Long id);
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
    User findByUsername(String username);
    User update(Long id,String name,String username,String email);
    List<User> findAllByRole(Role role);
    User updatePassword(String name,String username,String password,String email,Role role);
}
