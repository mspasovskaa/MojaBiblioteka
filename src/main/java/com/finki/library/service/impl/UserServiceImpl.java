package com.finki.library.service.impl;

import com.finki.library.model.User;
import com.finki.library.model.enums.Role;
import com.finki.library.model.exceptions.UserNotFoundException;
import com.finki.library.repository.UserRepository;
import com.finki.library.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserServiceImpl implements UserService , UserDetailsService {
    private final UserRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(Long id) {
        return this.clientRepository.findById(id).orElseThrow(()->new UserNotFoundException());
    }

    @Override
    public User create(String name, String username, String password, String email, Role role) {
        User client=new User(name,username,passwordEncoder.encode(password),email,role);
        return this.clientRepository.save(client);
    }
    @Override
    public User updatePassword(String name,String username,String password,String email,Role role)
    {
        User user=this.clientRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException());
        user.setName(name);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(Role.ROLE_USER);
        return this.clientRepository.save(user);
    }

    @Override
    public User delete(Long id) {
        User client=this.findById(id);
        this.clientRepository.delete(client);
        return client;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=this.clientRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException());
        UserDetails userDetails=new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(), Stream.of(new SimpleGrantedAuthority(user.getRole().toString())).collect(Collectors.toList())
        );
        return userDetails;
    }

    @Override
    public User findByUsername(String username) {
        User user=this.clientRepository.findByUsername(username).orElseThrow(()->new UserNotFoundException());
        return user;
    }

    @Override
    public User update(Long id,String name,String username, String email) {
        User user=this.clientRepository.findById(id).orElseThrow(()-> new UserNotFoundException());
        user.setEmail(email);
        user.setName(name);
        user.setUsername(username);

        return this.clientRepository.save(user);
    }

    @Override
    public List<User> findAllByRole(Role role) {
        return this.clientRepository.findAllByRole(role);
    }


}
