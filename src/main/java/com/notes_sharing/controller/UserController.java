package com.notes_sharing.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.notes_sharing.entity.File;
import com.notes_sharing.entity.Notes;
import com.notes_sharing.entity.ProfilePic;
import com.notes_sharing.entity.UserDtls;
import com.notes_sharing.repository.NotesRepository;
import com.notes_sharing.repository.ProfilePicRepository;
import com.notes_sharing.repository.UserRepository;
import com.notes_sharing.utils.ImageUtils;
import com.notes_sharing.repository.FileRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private ProfilePicRepository profilePicRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/addNotes")
	public String home() {
		return "user/add_notes";
	}

	@ModelAttribute
	public void addCommnData(Principal p, Model m) {
		String email = p.getName();
		UserDtls user = userRepository.findByEmail(email);
		m.addAttribute("user", user);
		ProfilePic profilePic = profilePicRepository.findPicByUserId(user.getId());
		m.addAttribute("profile", profilePic);
		m.addAttribute("imgUtil", new ImageUtils());
	}

	@GetMapping("/viewNotes/{page}")
	public String viewNotes(@PathVariable int page, Model m, Principal p) {

		String email = p.getName();
		UserDtls user = userRepository.findByEmail(email);

		Pageable pageable = PageRequest.of(page, 2, Sort.by("id").descending());
		Page<Notes> notes = notesRepository.findyNotesByUser(user.getId(), pageable);

		m.addAttribute("pageNo", page);
		m.addAttribute("totalPage", notes.getTotalPages());
		m.addAttribute("Notes", notes);
		m.addAttribute("totalElement", notes.getTotalElements());

		return "user/view_notes";
	}

	@GetMapping("/editNotes/{id}")
	public String editNotes(@PathVariable int id, Model m) {

		Optional<Notes> n = notesRepository.findById(id);
		Optional<File> f = Optional.ofNullable(fileRepository.findFileByNotesId(n.get().getId()));
		if (n != null) {
			Notes notes = n.get();
			m.addAttribute("notes", notes);
		}
		if (f != null) {
			File file = f.get();
			m.addAttribute("file", file);
		}

		return "user/edit_notes";
	}

	@PostMapping("/updateNotes")
	public String updateNotes(@ModelAttribute Notes notes, HttpSession session, Principal p) {
		String email = p.getName();
		UserDtls user = userRepository.findByEmail(email);

		notes.setUserDtls(user);

		Notes updateNotes = notesRepository.save(notes);

		if (updateNotes != null) {
			session.setAttribute("msg", "Notes Update Sucessfully");
		} else {
			session.setAttribute("msg", "Something wrong on server");
		}

		System.out.println(notes);

		return "redirect:/user/viewNotes/0";
	}

	@GetMapping("/deleteNotes/{id}")
	public String deleteNotes(@PathVariable int id, HttpSession session) {

		Optional<Notes> notes = notesRepository.findById(id);
		if (notes != null) {
			notesRepository.delete(notes.get());
			session.setAttribute("msg", "Notes Delete Successfully");
		}

		return "redirect:/user/viewNotes/0";
	}

	@GetMapping("/viewProfile")
	public String viewProfile() {
		return "user/view_profile";
	}

	@PostMapping("/saveNotes")
	public String saveNotes(@ModelAttribute Notes notes, HttpSession session, Principal p,
			@RequestParam("filename") MultipartFile file) throws IOException {
		String email = p.getName();
		UserDtls u = userRepository.findByEmail(email);
		notes.setUserDtls(u);

		Notes n = notesRepository.save(notes);
		if (file != null) {
			System.out.println("inside File");
			File setFile = new File();
			setFile.setNotes(notes);
			setFile.setName(StringUtils.cleanPath(file.getOriginalFilename()));
			setFile.setType(file.getContentType());
			setFile.setFileData(file.getBytes());
			fileRepository.save(setFile);

		}

		if (n != null) {
			session.setAttribute("msg", "Notes Added Sucessfully");
		} else {
			session.setAttribute("msg", "Something wrong on server");
		}

		return "redirect:/user/addNotes";
	}

	@PostMapping("/updateUser")
	public String updateUser(@ModelAttribute UserDtls user, HttpSession session, Model m) {
		Optional<UserDtls> Olduser = userRepository.findById(user.getId());
		if (Olduser != null) {
			user.setRole(Olduser.get().getRole());
			user.setEmail(Olduser.get().getEmail());

			UserDtls updateUser = userRepository.save(user);
			if (updateUser != null) {
				m.addAttribute("user", updateUser);
				session.setAttribute("msg", "Profile Update Sucessfully..");
			}

		}

		return "redirect:/user/viewProfile";
	}

	@GetMapping("/downloadFile/{id}")
	public ResponseEntity getFile(@PathVariable int id) {
		File fileDownloadFile = fileRepository.getById(id);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileDownloadFile.getType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDownloadFile.getName() + "\"")
				.body(fileDownloadFile.getFileData());
	}

	@GetMapping("/openFile/{id}")
	public ResponseEntity openFile(@PathVariable int id) {
		File fileDownloadFile = fileRepository.getById(id);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileDownloadFile.getType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDownloadFile.getName() + "\"")
				.body(fileDownloadFile.getFileData());
	}

}
