//package com.ezen.makingbaking.common;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.UUID;
//
//import org.springframework.web.multipart.MultipartFile;
//
//
//public class FileUtils {
//		//Map<String, String> => 파일 업로드 기능이 여러군데에서 사용될 때 범용성을 높이기 위해
//		//Map을 사용한다. Map을 사용할 경우 매퍼까지 Map으로 보내준다.
//		public static File parseFileInfo(MultipartFile file, 
//				String attachPath) throws IOException {
//			File boardFile = new File();
//			
//			String boardOriginFileNm = file.getOriginalFilename();
//			
//			//같은 파일명을 업로드했을 때 덮어써지지 않게 하기위한 실제 업로드되는 파일명 설정
//			SimpleDateFormat formmater = new SimpleDateFormat("yyyyMMddHHmmss");
//			Date nowDate = new Date();
//			String nowDateStr = formmater.format(nowDate);
//			UUID uuid = UUID.randomUUID();
//			
//			String fileNm = nowDateStr + "_" + uuid.toString() + "_" + fileOriginName;
//			
//			String filePath = attachPath;
//			
//			//이미지인지 다른 파일형태인지 검사
//			File checkFile = new File(boardOriginFileNm);
//			//업로드한 파일의 형식 가져옴(이미지파일들은 image/jpg, image/png ...)
//			String type = Files.probeContentType(checkFile.toPath());
//			
//			if(type != null) {
//				if(type.startsWith("image")) {
//					boardFile.setBoardFileCate("img");
//				} else {
//					boardFile.setBoardFileCate("etc");
//				}
//			} else {
//				boardFile.setBoardFileCate("etc");
//			}
//			
//			boardFile.setBoardFileNm(boardFileNm);
//			boardFile.setBoardOriginFileNm(boardOriginFileNm);
//			boardFile.setBoardFilePath(boardFilePath);
//
//			//실제 파일 업로드
//			File uploadFile = new File(attachPath + boardFileNm);
//			//매개변수는 업로드될 폴더와 파일명을 파일객체 형태로 넣어준다.
//			//파일업로드 시 IOException 처리
//			file.transferTo(uploadFile);
//			
//			return boardFile;
//		}
//}
