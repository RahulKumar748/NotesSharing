package com.notes_sharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NotesSharingApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesSharingApplication.class, args);
	}

}
