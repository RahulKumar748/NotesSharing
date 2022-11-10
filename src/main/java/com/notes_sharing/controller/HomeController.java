package com.notes_sharing.controller;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.notes_sharing.entity.ProfilePic;
import com.notes_sharing.entity.UserDtls;
import com.notes_sharing.repository.ProfilePicRepository;
import com.notes_sharing.repository.UserRepository;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private ProfilePicRepository profilePicRepos;

	@GetMapping("/")
	public String home() {
		return "home";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute UserDtls user, Model m, HttpSession session,
			@RequestParam("filename") MultipartFile file) throws IOException {

		user.setPasswordWithoutEncrpt(user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		System.out.println(user);
		UserDtls u = userRepo.save(user);
		if (file != null) {
			System.out.println("inside File");
			ProfilePic setFile = new ProfilePic();
			setFile.setUserDtls(user);
			setFile.setName(StringUtils.cleanPath(file.getOriginalFilename()));
			setFile.setType(file.getContentType());
			setFile.setFileData(file.getBytes());
			profilePicRepos.save(setFile);

		}

		if (u != null) {
			session.setAttribute("msg", "Register Sucessfully");
		} else {
			session.setAttribute("msg", "Something wrong on server");
		}

		return "redirect:/signup";
	}
}
