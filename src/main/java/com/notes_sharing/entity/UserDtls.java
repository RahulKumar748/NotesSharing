package com.notes_sharing.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionListenable.WithoutBatchStrategy;

@Data
@Entity
public class UserDtls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "full_name")
	private String name;
	private String email;
	private String password;
	private String passwordWithoutEncrpt;
	private String about;
	private String role;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPasswordWithoutEncrpt(String password) {
		this.passwordWithoutEncrpt = password;
	}

	public String getPasswordWithoutEncrpt() {
		return passwordWithoutEncrpt;
	}

	public UserDtls() {
		// TODO Auto-generated constructor stub
	}

}
