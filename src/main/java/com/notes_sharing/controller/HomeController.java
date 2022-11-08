package com.notes_sharing.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.notes_sharing.entity.UserDtls;
import com.notes_sharing.repository.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

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
	public String saveUser(@ModelAttribute UserDtls user, Model m, HttpSession session) {
		
		user.setPasswordWithoutEncrpt(user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		System.out.println(user);
		UserDtls u = userRepo.save(user);

		if (u != null) {
			session.setAttribute("msg", "Register Sucessfully");
		} else {
			session.setAttribute("msg", "Something wrong on server");
		}

		return "redirect:/signup";
	}
}
