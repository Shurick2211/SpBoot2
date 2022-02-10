package com.sn.org.SpBoot2.controller;

import com.sn.org.SpBoot2.model.Post;
import com.sn.org.SpBoot2.model.Role;
import com.sn.org.SpBoot2.model.User;
import com.sn.org.SpBoot2.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("")
public class BlogController {
    @Autowired
    private PostRepository postRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/add")
    public String addPost(@AuthenticationPrincipal User user,
            Model model){
        model.addAttribute("i"," active");
        model.addAttribute("h","");
        model.addAttribute("a","");
        model.addAttribute("user",user);
        if(user!=null)
        if(user.getRole()== Role.ADMIN) model.addAttribute("isAdmin", true);
        return "/blog/add";
    }

    @PostMapping("/add")
    public String addPostBd(//@ModelAttribute("post") Post post,
                            @AuthenticationPrincipal User user,
                            @RequestParam("title") String title,
                            @RequestParam("anons") String anons,
                            @RequestParam("fullText") String fullText,
                            @RequestParam("file") MultipartFile file,
                            Model model) throws IOException {


       Post post=new Post(title,anons,fullText);
        post.setAuthor(user);
       uplFile(file, post);

       postRepository.save(post);

        return "redirect:/";
    }

    @GetMapping("")
    public String blog(@AuthenticationPrincipal User user,
            @ModelAttribute("search") String search, Model model){
        model.addAttribute("i","");
        model.addAttribute("h"," active");
        model.addAttribute("a","");
        model.addAttribute("user",user);
        if(user!=null)
        if(user.getRole()== Role.ADMIN) model.addAttribute("isAdmin", true);
        Iterable<Post> postsN;
        postsN=postRepository.findAll();
        List<Post> posts1=new ArrayList<>();
        postsN.forEach(p->{ if(!p.getTitle().startsWith("About"))posts1.add(p); });
        List<Post> posts=new ArrayList<>();
        if(!search.equals("")){
            posts1.stream().filter(p->p.getTitle().toLowerCase().indexOf(search.toLowerCase())>=0)
                    .forEach(p->posts.add(p));
        }else posts.addAll(posts1);
        model.addAttribute("posts", posts);
        return "/blog/home";
    }
    @GetMapping("/edit/{id}")
    public String editPost(@AuthenticationPrincipal User user,
            @PathVariable(value = "id") long id, Model model){
        if(user!=null)
        if(user.getRole()== Role.ADMIN) model.addAttribute("isAdmin", true);
        model.addAttribute("i"," active");
        model.addAttribute("h","");
        model.addAttribute("a","");
        model.addAttribute("user",user);
        Post post=postRepository.findById(id).orElse(null);
        model.addAttribute("post",post);
        return "/blog/edit";
    }
    @PostMapping("/edit/{id}")
    public String editPostDd(@PathVariable(value = "id") long id,
                             @RequestParam("title") String title,
                             @RequestParam("anons") String anons,
                             @RequestParam("fullText") String fullText,
                             @RequestParam("file") MultipartFile file,
                           //  @ModelAttribute("post") Post post,
                             Model model) throws IOException {
        Post post= postRepository.findById(id).get();
        post.setTitle(title);
        post.setFullText(fullText);
        post.setAnons(anons);
        if(!file.isEmpty())
        uplFile(file, post);
        postRepository.save(post);

        return "redirect:/blog/"+id;
    }

    @DeleteMapping("/edit/{id}")
    @PreAuthorize("hasAuthority(Role.ADMIN.name())")
    public String postDelete(@PathVariable(value = "id") long id, Model model) {

        postRepository.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/blog/{id}")
    public String blog(@AuthenticationPrincipal User user,
            @PathVariable(value = "id") long id, Model model) throws FileNotFoundException {
        if(user!=null)
        if(user.getRole()== Role.ADMIN) model.addAttribute("isAdmin", true);
        model.addAttribute("i","");
        model.addAttribute("h","");
        model.addAttribute("a","");
        model.addAttribute("user",user);
        Post post=postRepository.findById(id).orElse(null);
        int i = post.getShow();
        i++;
        post.setShow(i);
        postRepository.save(post);
        model.addAttribute("post",post);

        return "/blog/show";
    }

    @GetMapping("/about")
    public String about(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("i","");
        model.addAttribute("h","");
        model.addAttribute("a"," active");
        model.addAttribute("post", postRepository.findByTitle("About"));
        model.addAttribute("user", user);
        if(user!=null)
        if(user.getRole()== Role.ADMIN) model.addAttribute("isAdmin", true);

        return "/blog/ab";
    }

    private void uplFile(MultipartFile file, Post post) throws IOException {
        if(file!=null){

            File uploadDir= new File(uploadPath);

            if(!uploadDir.exists())  uploadDir.mkdir();
            String uuidFile= UUID.randomUUID().toString();
            String rezultFileName=uuidFile+"."+file.getOriginalFilename();


            File rezFile=new File(uploadDir+"/"+rezultFileName);

            file.transferTo(rezFile);

            post.setFileName(rezFile.getName());

        }
    }
}
