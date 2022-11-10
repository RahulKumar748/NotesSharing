package com.notes_sharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.notes_sharing.entity.File;

public interface FileRepository extends JpaRepository<File, Integer>{
	@Query("from File as f where f.notes.id=:uid")
	File findFileByNotesId(@Param("uid") int uid);
}
