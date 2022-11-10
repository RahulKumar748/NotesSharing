package com.notes_sharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.notes_sharing.entity.Notes;
import com.notes_sharing.entity.ProfilePic;

public interface ProfilePicRepository extends JpaRepository<ProfilePic, Integer>{
	@Query("from ProfilePic as p where p.userDtls.id=:uid")
	ProfilePic findPicByUserId(@Param("uid") int uid);
}
