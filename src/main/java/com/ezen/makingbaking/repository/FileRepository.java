package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.File;
import com.ezen.makingbaking.entity.FileId;

public interface FileRepository extends JpaRepository<File, FileId> {

}
