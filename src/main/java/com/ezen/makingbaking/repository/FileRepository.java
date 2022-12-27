package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.entity.FileId;
import com.ezen.makingbaking.entity.ImgFile;

public interface FileRepository extends JpaRepository<ImgFile, FileId> {
	
	@Query(value="SELECT IFNULL(MAX(F.FILE_NO), 0) + 1 FROM T_MB_FILE F WHERE F.FILE_NO=:fileNo", nativeQuery=true)
	int getMaxFileNo(@Param("fileNo") int fileNo);
}
