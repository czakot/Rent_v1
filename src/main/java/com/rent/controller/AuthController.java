package com.rent.controller;

import com.rent.RentApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rent.entity.User;
import com.rent.repo.RoleRepository;
import com.rent.service.UserService;
import static java.lang.Thread.sleep;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @Qualifier("UserServiceImpl") // not nessessary, UserService unique here
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/masterreg")
    public String masterReg(Model model) {
        if (userService.notValidatedMasterExists()) {
            return "auth/activatemastermsg";
        } else {
            model.addAttribute("user", new User());
            return "auth/masterregform";
        }
    }

    @PostMapping("/masterregdo")
    public String masterReg(@ModelAttribute User masterToRegister) {
        userService.registerMaster(masterToRegister);

        return "auth/activatemastermsg";
    }

    @RequestMapping(value = "/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "auth/registration";
    }

//	@RequestMapping(value = "/reg", method = RequestMethod.POST)
    @PostMapping("/registration")
    public String reg(@ModelAttribute User userToRegister, Model model) {
        if (!userService.registerUser(userToRegister)) {
            User newUser = new User();
            newUser.setEmail(userToRegister.getEmail());
            model.addAttribute("user", newUser);
            model.addAttribute("result", "already_exists");
            return "auth/registration";
        } else {
            model.addAttribute("result", "registered");
        }
        return "auth/login";
    }

    @RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
    public String activation(@PathVariable("code") String code, Model model) {
        User activatedUser = userService.userActivation(code);
        if (userService.isMaster(activatedUser)) {
            Thread thread = new Thread(() -> {
                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    /* nothing to do */ }
                RentApplication.restart();
            });

            thread.setDaemon(false);
            thread.start();

            return "auth/restartmsg";
        }
        if (userService.enabledMasterExists()) {
            model.addAttribute("result", activatedUser != null ? "activated" : "notactivated");
            return "auth/login";
        }
        
        return "auth/activatemastermsg";

    }
}
