package com.sn.org.SpBoot2.controller;

import com.sn.org.SpBoot2.model.Role;
import com.sn.org.SpBoot2.model.User;
import com.sn.org.SpBoot2.repo.PostRepository;
import com.sn.org.SpBoot2.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("i","");
        model.addAttribute("h","");
        model.addAttribute("a","");
        model.addAttribute("user", new User());
        return "/user/add";
    }
    @PostMapping("/add")
    public String addUser(@ModelAttribute User user){
        if(user.getEmail().equals("shurick2211@gmail.com"))
            user.setRole(Role.ADMIN);
        else
        user.setRole(Role.USER);
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String userList(@AuthenticationPrincipal User user,
            Model model){
        if(user.getRole()== Role.ADMIN) model.addAttribute("isAdmin", true);
        model.addAttribute("i","");
        model.addAttribute("h","");
        model.addAttribute("a","");
        model.addAttribute("user",user);
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@AuthenticationPrincipal User user,
            @PathVariable long id, Model model){
        model.addAttribute("i","");
        model.addAttribute("h","");
        model.addAttribute("a","");
        if(user.getRole()== Role.ADMIN) model.addAttribute("isAdmin", true);
        model.addAttribute("user",user);
        model.addAttribute("userE",userRepository.getById(id));
        model.addAttribute("roles", Role.values());
        return "/user/edit";
    }
    @PostMapping("/edit/{id}")
    public String editPerson(@PathVariable long id,@ModelAttribute User user){
        userRepository.save(user);
        return "redirect:/user/list";
    }
    @DeleteMapping ("/edit/{id}")
    public String deletePerson(@PathVariable long id,@ModelAttribute User user){
       userRepository.delete(user);
        return "redirect:/user/list";
    }
    @GetMapping("/posts/{id}")
    public String posts(@AuthenticationPrincipal User user,
                       @PathVariable long id, Model model){
        model.addAttribute("i","");
        model.addAttribute("h","");
        model.addAttribute("a","");
        if(user.getRole()== Role.ADMIN) model.addAttribute("isAdmin", true);
        model.addAttribute("user",user);
        model.addAttribute("author", userRepository.getById(id));
        model.addAttribute("posts", postRepository.findAllByAuthorId(id));
        return "/user/posts";
    }
}
