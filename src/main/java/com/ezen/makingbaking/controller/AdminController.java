package com.ezen.makingbaking.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.common.FileUtils;
import com.ezen.makingbaking.dto.ImgFileDTO;
import com.ezen.makingbaking.dto.ItemDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.service.admin.AdminService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;
	
	
	//item
	//상품리스트
	@GetMapping("/itemList")
	public ModelAndView getItemList(ItemDTO itemDTO,
			@PageableDefault(page = 0, size = 50) Pageable pageable) {
		Item item = Item.builder()
							.itemName(itemDTO.getItemName())
							.itemPrice(itemDTO.getItemPrice())
							.itemRegdate(LocalDateTime.now())
							.itemStatus(itemDTO.getItemStatus())
							.searchCondition(itemDTO.getSearchCondition())
							.searchKeyword(itemDTO.getSearchKeyword())
							.build();
		List<Item> itemList = adminService.getItemList(item);
		
		Page<Item> pageItemList = adminService.getPageItemList(item, pageable);
	      
	      Page<ItemDTO> pageItemDTOList = pageItemList.map(pageItem -> 
	                                             ItemDTO.builder()
	                                                      .itemNo(pageItem.getItemNo())
	                                                      .itemName(pageItem.getItemName())
	                                                      .itemPrice(pageItem.getItemPrice())
	                                                      .itemRegdate(
	                                                                     pageItem.getItemRegdate() == null?
	                                                               null :
	                                                               pageItem.getItemRegdate().toString())
	                                                      .itemStatus(pageItem.getItemStatus())
	                                                      .build()
	                                             );
		
		List<ItemDTO> getItemList = new ArrayList<ItemDTO>();
		for(int i = 0; i < itemList.size(); i++) {
			ItemDTO returnItem = ItemDTO.builder()
											.itemNo(itemList.get(i).getItemNo())
											.itemName(itemList.get(i).getItemName())
											.itemPrice(itemList.get(i).getItemPrice())
											.itemRegdate(
													itemList.get(i).getItemRegdate() == null ?
													null :
													itemList.get(i).getItemRegdate().toString())

											.itemStatus(itemList.get(i).getItemStatus())
											.build();

			getItemList.add(returnItem);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/itemList.html");
		mv.addObject("getItemList", pageItemDTOList);
		
		if(itemDTO.getSearchCondition() != null && !itemDTO.getSearchCondition().equals("")) {
			mv.addObject("searchCondition", itemDTO.getSearchCondition());
		}
		
		if(itemDTO.getSearchKeyword() != null && !itemDTO.getSearchKeyword().equals("")) {
			mv.addObject("searchKeyword", itemDTO.getSearchKeyword());
		}
		
		return mv;
	}
	
	
	//상품등록
	@GetMapping("/insertItemView")
	public ModelAndView insertItemView() {
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("admin/itemReg.html");
			
		return mv;
	}
	
	@PostMapping("/insertItem")
	public void insertItem(ItemDTO itemDTO, MultipartFile[] uploadFiles, 
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		System.out.println(itemDTO.getItemStatus());
		
		Item item = Item.builder()
						.itemStatus(itemDTO.getItemStatus())
						.itemStock(itemDTO.getItemStock())
						.itemCate(itemDTO.getItemCate())
						.itemName(itemDTO.getItemName())
						.itemMinName(itemDTO.getItemMinName())
						.itemDetails(itemDTO.getItemDetails())
						.itemPrice(itemDTO.getItemPrice())
						.itemExpDate(itemDTO.getItemExpDate())
						.itemOrigin(itemDTO.getItemOrigin())
						.itemAllergyInfo(itemDTO.getItemAllergyInfo())
						.build();
	
		//DB에 입력될 파일 정보 리스트
		List<ImgFile> uploadFileList = new ArrayList<ImgFile>();
		
		if(uploadFiles.length > 0) {
			String attachPath = request.getSession().getServletContext().getRealPath("/")
					+ "/item/";
			
			File directory = new File(attachPath);
			
			if(!directory.exists()) {
				directory.mkdir();
			}
			
			//multipartFile 형식의 데이터를 DB 테이블에 맞는 구조로 변경
			for(int i = 0; i < uploadFiles.length; i++) {
				MultipartFile file = uploadFiles[i];
				
				if(!file.getOriginalFilename().equals("") &&
					file.getOriginalFilename() != null) {
					ImgFile itemFile = new ImgFile();
					
					itemFile = FileUtils.parseFileInfo(file, attachPath);
					
					uploadFileList.add(itemFile);
				}
			}
		}
		
		adminService.insertItem(item, uploadFileList);
		
		response.sendRedirect("/admin/itemList");
	}
	
	
	//상품상세보기
	@GetMapping("/item/{itemNo}")
	public ModelAndView getItem(@PathVariable int itemNo) {
		Item item = adminService.getItem(itemNo);
		
		//System.out.println(item.toString());
		
		ItemDTO itemDTO = ItemDTO.builder()
									.itemNo(item.getItemNo())
									.itemName(item.getItemName())
									.itemMinName(item.getItemMinName())
									.itemDetails(item.getItemDetails())
									.itemExpDate(item.getItemExpDate())
									.itemAllergyInfo(item.getItemAllergyInfo())
									.itemOrigin(item.getItemOrigin())
									.itemPrice(item.getItemPrice())
									.itemRegdate(
											item.getItemRegdate() == null ?
											null :
											item.getItemRegdate().toString())
									.itemStatus(item.getItemStatus())
									.itemCate(item.getItemCate())
									.itemStock(item.getItemStock())
									.build();
		
		List<ImgFile> itemFileList = adminService.getItemFileList(itemNo);
		
		//imgFileDTO를 담는 List
		List<ImgFileDTO> imgFileDTOList = new ArrayList<ImgFileDTO>();
		
		for(ImgFile imgFile : itemFileList) {
			ImgFileDTO imgFileDTO = ImgFileDTO.builder()
													.fileReferNo(itemNo)
													.fileNo(itemNo)
													.fileNo(imgFile.getFileNo())
													.fileName(imgFile.getFileName())
													.fileOriginName(imgFile.getFileOriginName())
													.filePath(imgFile.getFilePath())
													.fileType(imgFile.getFileType())
													.build();
			
			imgFileDTOList.add(imgFileDTO);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/itemDetail.html");
		mv.addObject("getItem", itemDTO);
		mv.addObject("itemFileList", imgFileDTOList);
		
		return mv;
	}
	
	
	//상품 수정
	@Transactional
	@PutMapping("/updateItem")
	public ResponseEntity<?> updateItem(ItemDTO itemDTO,
			HttpServletResponse response, MultipartFile[] uploadFiles,
			MultipartFile[] changedFiles, HttpServletRequest request,
			@RequestParam("originFiles") String originFiles) throws IOException { 
		System.out.println("itemDTO.getItemPrice()==================================" + itemDTO.getItemPrice());
		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
		
		List<ImgFileDTO> originFileList = new ObjectMapper().readValue(originFiles, 
												new TypeReference<List<ImgFileDTO>>() {});
		
		for(int i = 0; i < originFileList.size(); i++) {
			System.out.println(originFileList.get(i).toString());
		}
		
		String attachPath = request.getSession().getServletContext().getRealPath("/") +
					"/item/";
		
		File directory = new File(attachPath);
		
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		//DB에서 수정, 삭제, 추가 될 파일 정보
		List<ImgFile> uFileList = new ArrayList<ImgFile>();
		
		try {
			Item item = Item.builder()
//					상품상태, 카테고리, 등록일, 이름, 소제목, 설명, 가격, 유통기한, 원산지, 알레르기정보, 첨부파일, 재고
							.itemNo(itemDTO.getItemNo())
							.itemStatus(itemDTO.getItemStatus())
							.itemCate(itemDTO.getItemCate())
							.itemRegdate(
									itemDTO.getItemRegdate() == null ||
									itemDTO.getItemRegdate().equals("") ?
											null :
												LocalDateTime.parse(itemDTO.getItemRegdate()))
							.itemName(itemDTO.getItemName())
							.itemMinName(itemDTO.getItemMinName())
							.itemDetails(itemDTO.getItemDetails())
							.itemPrice(itemDTO.getItemPrice())
							.itemExpDate(itemDTO.getItemExpDate())
							.itemOrigin(itemDTO.getItemOrigin())
							.itemAllergyInfo(itemDTO.getItemAllergyInfo())
							.itemStock(itemDTO.getItemStock())
							.build();
					
			//파일 처리
			for(int i = 0; i < originFileList.size(); i++) {
				//수정되는 파일 처리
				if(originFileList.get(i).getFileStatus().equals("U")) {
					for(int j = 0; j < changedFiles.length; j++) {
						if(originFileList.get(i).getNewFileName().equals(
								changedFiles[j].getOriginalFilename())) {
							ImgFile imgFile = new ImgFile();
							
							MultipartFile file = changedFiles[j];
							
							imgFile = FileUtils.parseFileInfo(file, attachPath);
							
							imgFile.setFileReferNo(itemDTO.getItemNo());
							imgFile.setFileNo(originFileList.get(i).getFileNo());
							imgFile.setFileStatus("U");
							imgFile.setFileType("item");
							
							uFileList.add(imgFile);
						}
					}
				//삭제되는 파일 처리
				} else if (originFileList.get(i).getFileStatus().equals("D")) {
					ImgFile imgFile = new ImgFile();
					
					//boardFile.setBoard(board);
					imgFile.setFileReferNo(itemDTO.getItemNo());
					imgFile.setFileNo(originFileList.get(i).getFileNo());
					imgFile.setFileStatus("D");
					imgFile.setFileType("item");
					
					//실제 파일 삭제
					File dFile = new File(attachPath + originFileList.get(i).getFileName());
					dFile.delete();
					
					uFileList.add(imgFile);
				}
			}
			
			//추가된 파일 처리
			if(uploadFiles.length > 0) {
				for(int i = 0; i < uploadFiles.length; i++) {
					MultipartFile file = uploadFiles[i];
					
					if(file.getOriginalFilename() != null && 
							!file.getOriginalFilename().equals("")) {
						ImgFile imgFile = new ImgFile();
						
						imgFile = FileUtils.parseFileInfo(file, attachPath);
						
						imgFile.setFileReferNo(itemDTO.getItemNo());
						imgFile.setFileStatus("I");
						imgFile.setFileType("item");
						
						uFileList.add(imgFile);
					}
				}
			}

			adminService.updateItem(item, uFileList);
			
			//board = boardService.getBoard(boardDTO.getBoardNo());
			
			ItemDTO returnItem = ItemDTO.builder()
										.itemNo(item.getItemNo())
										.itemStatus(item.getItemStatus())
										.itemCate(item.getItemCate())
										.itemRegdate(
												item.getItemRegdate() == null ?
														null :
												item.getItemRegdate().toString())
										.itemName(item.getItemName())
										.itemMinName(item.getItemMinName())
										.itemDetails(item.getItemDetails())
										.itemPrice(item.getItemPrice())
										.itemExpDate(item.getItemExpDate())
										.itemOrigin(item.getItemOrigin())
										.itemAllergyInfo(item.getItemAllergyInfo())
										.itemStock(item.getItemStock())
										.build();
			
			List<ImgFile> imgFileList = adminService.getItemFileList(item.getItemNo());
			
			List<ImgFileDTO> imgFileDTOList = new ArrayList<ImgFileDTO>();
			
			for(ImgFile imgFile : imgFileList) {
				ImgFileDTO imgFileDTO = ImgFileDTO.builder()
													.fileReferNo(item.getItemNo())
													.fileNo(imgFile.getFileNo())
													.fileName(imgFile.getFileName())
													.fileOriginName(imgFile.getFileOriginName())
													.filePath(imgFile.getFilePath())
													.fileType(imgFile.getFileType())
													.build();
				
				imgFileDTOList.add(imgFileDTO);
			}
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			returnMap.put("getItem", returnItem);
			returnMap.put("imgFileList", imgFileDTOList);
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
		
	}
	
	
	//상품수정 상세보기
	@GetMapping("/itemUpdate/{itemNo}")
	public ModelAndView getUpdateItem(@PathVariable int itemNo) {
		Item item = adminService.getItem(itemNo);
		
		ItemDTO itemDTO = ItemDTO.builder()
									.itemNo(item.getItemNo())
									.itemName(item.getItemName())
									.itemMinName(item.getItemMinName())
									.itemDetails(item.getItemDetails())
									.itemExpDate(item.getItemExpDate())
									.itemAllergyInfo(item.getItemAllergyInfo())
									.itemOrigin(item.getItemOrigin())
									.itemPrice(item.getItemPrice())
									.itemRegdate(
											item.getItemRegdate() == null ?
											null :
											item.getItemRegdate().toString())
									.itemStatus(item.getItemStatus())
									.itemCate(item.getItemCate())
									.itemStock(item.getItemStock())
									.build();
		
		List<ImgFile> itemFileList = adminService.getItemFileList(itemNo);
		
		//imgFileDTO를 담는 List
		List<ImgFileDTO> imgFileDTOList = new ArrayList<ImgFileDTO>();
		
		for(ImgFile imgFile : itemFileList) {
			ImgFileDTO imgFileDTO = ImgFileDTO.builder()
													.fileReferNo(itemNo)
													.fileNo(itemNo)
													.fileNo(imgFile.getFileNo())
													.fileName(imgFile.getFileName())
													.fileOriginName(imgFile.getFileOriginName())
													.filePath(imgFile.getFilePath())
													.fileType(imgFile.getFileType())
													.build();
			
			imgFileDTOList.add(imgFileDTO);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/itemUpdate.html");
		mv.addObject("getItem", itemDTO);
		mv.addObject("itemFileList", imgFileDTOList);
		
		return mv;
	}	
	
	
	//상품 삭제
	@DeleteMapping("/delItem")
	public void deleteItem(@RequestParam("itemNo") int itemNo) {
		adminService.deleteItem(itemNo);
	}
	
	//관리자가 게시글 삭제하는 경우 ajax를 이용해 백단에 전송
	@PostMapping("/saveItemList")
	public ResponseEntity<?> saveBoardList(@RequestParam("changeRows") String changeRows,
			@PageableDefault(page = 0, size = 50) Pageable pageable) throws JsonMappingException, JsonProcessingException {
		ResponseDTO<ItemDTO> response = new ResponseDTO<>();
		List<Map<String, Object>> changeRowsList = new ObjectMapper().readValue(changeRows, 
											new TypeReference<List<Map<String, Object>>>() {});
		
		try {
			adminService.saveItemList(changeRowsList);
			
			Item item = Item.builder()
							   .searchCondition("")
							   .searchKeyword("")
							   .build();
			
			Page<Item> pageItemList = adminService.getPageItemList(item, pageable);
			
			Page<ItemDTO> pageItemDTOList = pageItemList.map(pageItem -> 
														ItemDTO.builder()
																.itemNo(pageItem.getItemNo())
																.itemName(pageItem.getItemName())
																.itemMinName(pageItem.getItemMinName())
																.itemDetails(pageItem.getItemDetails())
																.itemExpDate(pageItem.getItemExpDate())
																.itemAllergyInfo(pageItem.getItemAllergyInfo())
																.itemOrigin(pageItem.getItemOrigin())
																.itemPrice(pageItem.getItemPrice())
																.itemRegdate(
																		pageItem.getItemRegdate() == null ?
																		null :
																		pageItem.getItemRegdate().toString())
																.itemStatus(pageItem.getItemStatus())
																.itemCate(pageItem.getItemCate())
																.itemStock(pageItem.getItemStock())
																.build()
			);
			
			response.setPageItems(pageItemDTOList);
			
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	
	
	
	//dayclass
	//상품등록
//	@GetMapping("/insertDatclassView")
//	public ModelAndView insertDayclassView() {
//		
//		ModelAndView mv = new ModelAndView();
//		
//		mv.setViewName("admin/dayclassReg.html");
//			
//		return mv;
//	}
//	
//	@PostMapping("/insertDayclass")
//	public void insertDayclass(ItemDTO itemDTO, MultipartFile[] uploadFiles, 
//			HttpServletResponse response, HttpServletRequest request) throws IOException {
//		System.out.println(itemDTO.getItemStatus());
//		
//		Item item = Item.builder()
//						.itemStatus(itemDTO.getItemStatus())
//						.itemStock(itemDTO.getItemStock())
//						.itemCate(itemDTO.getItemCate())
//						.itemName(itemDTO.getItemName())
//						.itemMinName(itemDTO.getItemMinName())
//						.itemDetails(itemDTO.getItemDetails())
//						.itemPrice(itemDTO.getItemPrice())
//						.itemExpDate(itemDTO.getItemExpDate())
//						.itemOrigin(itemDTO.getItemOrigin())
//						.itemAllergyInfo(itemDTO.getItemAllergyInfo())
//						.build();
//	
//		//DB에 입력될 파일 정보 리스트
//		List<ImgFile> uploadFileList = new ArrayList<ImgFile>();
//		
//		if(uploadFiles.length > 0) {
//			String attachPath = request.getSession().getServletContext().getRealPath("/")
//					+ "/item/";
//			
//			File directory = new File(attachPath);
//			
//			if(!directory.exists()) {
//				directory.mkdir();
//			}
//			
//			//multipartFile 형식의 데이터를 DB 테이블에 맞는 구조로 변경
//			for(int i = 0; i < uploadFiles.length; i++) {
//				MultipartFile file = uploadFiles[i];
//				
//				if(!file.getOriginalFilename().equals("") &&
//					file.getOriginalFilename() != null) {
//					ImgFile itemFile = new ImgFile();
//					
//					itemFile = FileUtils.parseFileInfo(file, attachPath);
//					
//					uploadFileList.add(itemFile);
//				}
//			}
//		}
//		
//		adminService.insertItem(item, uploadFileList);
//		
//		response.sendRedirect("/admin/itemList");
//	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//임시보기
	@GetMapping("/main")
	public ModelAndView preView() {
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("admin/main.html");
			
		return mv;
	}
	
}
