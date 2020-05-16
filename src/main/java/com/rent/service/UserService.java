package com.rent.service;

import com.rent.entity.User;

public interface UserService {

    public boolean registerUser(User user);

    public User findByEmail(String email);

    public User userActivation(String code);

    public void registerMaster(User adminToRegister);
    
    public boolean enabledMasterExists();
    
    public boolean notValidatedMasterExists();
    
    public void deleteNotValidatedMaster();
    
    public boolean isMaster(User user);
    
}
