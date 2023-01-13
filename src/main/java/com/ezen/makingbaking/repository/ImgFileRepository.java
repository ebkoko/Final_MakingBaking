package com.ezen.makingbaking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.entity.FileId;
import com.ezen.makingbaking.entity.ImgFile;

public interface ImgFileRepository extends JpaRepository<ImgFile, FileId> {
	
	//item
	@Query(value="SELECT IFNULL(MAX(F.FILE_NO), 0) + 1 FROM T_MB_FILE F WHERE F.FILE_REFER_NO =:itemNo AND F.FILE_TYPE = 'item'", nativeQuery=true)
	int getItemMaxFileNo(@Param("itemNo") int itemNo);
	
	List<ImgFile> findByFileReferNoAndFileType(int itemNo, String fileType);
	
	//dayclass
	@Query(value="SELECT IFNULL(MAX(F.FILE_NO), 0) + 1 FROM T_MB_FILE F WHERE F.FILE_REFER_NO =:dayclassNo AND F.FILE_TYPE = 'class'", 
			nativeQuery=true)
	int getDayclassMaxFileNo(@Param("dayclassNo") int dayclassNo);

}
