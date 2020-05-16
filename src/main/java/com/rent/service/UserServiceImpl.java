package com.rent.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rent.entity.Role;
import com.rent.entity.User;
import com.rent.repo.RoleRepository;
import com.rent.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final String USER_ROLE = "USER";
    private final String ADMIN_ROLE = "ADMIN";
    private final String MASTER_ROLE = "MASTER";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean registerUser(User userToRegister) {
        boolean registered = register(userToRegister, new String[]{USER_ROLE});

        if (registered) {
            emailService.sendMessage(userToRegister);
        }

        return registered;
    }

    @Override
    public void registerMaster(User masterToRegister) {
        register(masterToRegister, new String[]{USER_ROLE, ADMIN_ROLE, MASTER_ROLE});
        emailService.sendMessage(masterToRegister);
    }

    private boolean register(User user, String[] roles) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        }

        for (String roleName : roles) {
            Role role = roleRepository.findByRole(roleName);
            if (role != null) {
                user.getRoles().add(role);
            } else {
                user.addRoles(roleName);
            }
        }

        user.setEnabled(false);
        user.setActivation(generateKey());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return true;
    }

    public String generateKey() {
        String key = "";
        Random random = new Random();
        char[] word = new char[16];
        for (int j = 0; j < word.length; j++) {
            word[j] = (char) ('a' + random.nextInt(26));
        }
        return new String(word);
    }

    @Override
    public User userActivation(String code) {
        User user = userRepository.findByActivation(code);
        if (user != null) {
            user.setEnabled(true);
            user.setActivation("");
            userRepository.save(user);
        }

        return user;
    }

    @Override
    public boolean enabledMasterExists() {
        User user = userRepository.findMaster();
        return user != null && user.getEnabled();
    }
    
    @Override
    public boolean notValidatedMasterExists() {
        User user = userRepository.findMaster();
        return user != null && !user.getEnabled();
    }

    @Override
    public void deleteNotValidatedMaster() {
        User user = userRepository.findMaster();
        if (user != null && !user.getEnabled()) {
            user.getRoles().clear();
            userRepository.delete(user);
        }
    }

    @Override
    public boolean isMaster(User user) {
        return user!= null && user.getRoles().contains(roleRepository.findByRole(MASTER_ROLE));
    }


}
