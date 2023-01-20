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
import com.ezen.makingbaking.dto.BoardDTO;
import com.ezen.makingbaking.dto.DayclassDTO;
import com.ezen.makingbaking.dto.ImgFileDTO;
import com.ezen.makingbaking.dto.ItemDTO;
import com.ezen.makingbaking.dto.OrderDTO;
import com.ezen.makingbaking.dto.ReserDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.dto.ReviewDTO;
import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.Board;
import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.entity.User;
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
	
	
	//관리자 메인페이지
	@GetMapping("/adminMain")
	public ModelAndView adminMain() {
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("admin/adminMain.html");
		
		return mv;
	}
	
	
//////////////////////////////item//////////////////////////////
	//상품리스트
	@GetMapping("/itemList")
	public ModelAndView getItemList(ItemDTO itemDTO,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		Item item = Item.builder()
							.searchCondition(itemDTO.getSearchCondition())
							.searchKeyword(itemDTO.getSearchKeyword())
							.build();
		
		Page<Item> pageItemList = adminService.getPageItemList(item, pageable);
	      
	    Page<ItemDTO> pageItemDTOList = pageItemList.map(pageItem -> 
	                                             ItemDTO.builder()
	                                                      .itemNo(pageItem.getItemNo())
	                                                      .itemName(pageItem.getItemName())
	                                                      .itemPrice(pageItem.getItemPrice())
	                                                      .itemRegdate(pageItem.getItemRegdate() == null?
	                                                               null :
	                                                               pageItem.getItemRegdate().toString())
	                                                      .itemStatus(pageItem.getItemStatus())
	                                                      .searchCondition(pageItem.getSearchCondition())
	                                                      .searchKeyword(pageItem.getSearchKeyword())
	                                                      .build()
	                                             );
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/itemList.html");
		mv.addObject("pageItemList", pageItemDTOList);
		
		if(itemDTO.getSearchCondition() != null && !itemDTO.getSearchCondition().equals("")) {
			mv.addObject("searchCondition", itemDTO.getSearchCondition());
		}
		
		if(itemDTO.getSearchKeyword() != null && !itemDTO.getSearchKeyword().equals("")) {
			mv.addObject("searchKeyword", itemDTO.getSearchKeyword());
		}
		
		return mv;
	}
	
	//상품리스트 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/itemList")
	public ResponseEntity<?> getItemPageList(ItemDTO itemDTO,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		ResponseDTO<ItemDTO> response = new ResponseDTO<>();
		try {
			Item item = Item.builder()
							.searchCondition(itemDTO.getSearchCondition())
							.searchKeyword(itemDTO.getSearchKeyword())
							.build();
			Page<Item> pageItemList = adminService.getPageItemList(item, pageable);
			
			Page<ItemDTO> pageItemDTOList = pageItemList.map(pageItem -> 
											            ItemDTO.builder()
													            .itemNo(pageItem.getItemNo())
													            .itemName(pageItem.getItemName())
													            .itemPrice(pageItem.getItemPrice())
													            .itemRegdate(pageItem.getItemRegdate() == null?
													                     null :
													                     pageItem.getItemRegdate().toString())
													            .itemStatus(pageItem.getItemStatus())
													            .searchCondition(pageItem.getSearchCondition())
													            .searchKeyword(pageItem.getSearchKeyword())
													            .build()
											   				);
			response.setPageItems(pageItemDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
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
			MultipartFile[] uploadFiles, MultipartFile[] changedFiles,
			HttpServletRequest request, @RequestParam("originFiles") String originFiles) throws IOException { 
		
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
					//상품상태, 카테고리, 등록일, 이름, 소제목, 설명, 가격, 유통기한, 원산지, 알레르기정보, 첨부파일, 재고
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
	
	//관리자가 상품을 삭제하는 경우 ajax를 이용해 백단에 전송
	@PostMapping("/saveItemList")
	public ResponseEntity<?> saveItemList(@RequestParam("changeRows") String changeRows,
			@PageableDefault(page = 0, size = 10) Pageable pageable) throws JsonMappingException, JsonProcessingException {
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


//////////////////////////////dayclass//////////////////////////////
	//데이클래스 리스트
	@GetMapping("/dayclassList")
	public ModelAndView getDayclassList(DayclassDTO dayclassDTO,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Dayclass dayclass = Dayclass.builder()
									.searchCondition(dayclassDTO.getSearchCondition())
									.searchKeyword(dayclassDTO.getSearchKeyword())
									.build();
		
		Page<Dayclass> pageDayclassList = adminService.getPageDayclassList(dayclass, pageable);
		
		Page<DayclassDTO> pageDayclassDTOList = pageDayclassList.map(pageDayclass -> 
	                                             						DayclassDTO.builder()
				                                             						.dayclassNo(pageDayclass.getDayclassNo())
				                                             						.dayclassName(pageDayclass.getDayclassName())
				                                             						.dayclassPrice(pageDayclass.getDayclassPrice())
				                                             						.dayclassTime(pageDayclass.getDayclassTime())
				                                             						.dayclassUseYn(pageDayclass.getDayclassUseYn())
				                                             						.build()
	                                             					);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/dayclassList.html");
		mv.addObject("pageDayclassList", pageDayclassDTOList);
		
		if(dayclassDTO.getSearchCondition() != null && !dayclassDTO.getSearchCondition().equals("")) {
			mv.addObject("searchCondition", dayclassDTO.getSearchCondition());
		}
		
		if(dayclassDTO.getSearchKeyword() != null && !dayclassDTO.getSearchKeyword().equals("")) {
			mv.addObject("searchKeyword", dayclassDTO.getSearchKeyword());
		}
		
		return mv;
	}
	
	//데이클래스리스트 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/dayclassList")
	public ResponseEntity<?> getDayclassPageList(DayclassDTO dayclassDTO,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		ResponseDTO<DayclassDTO> response = new ResponseDTO<>();
		try {
			Dayclass dayclass = Dayclass.builder()
										.searchCondition(dayclassDTO.getSearchCondition())
										.searchKeyword(dayclassDTO.getSearchKeyword())
										.build();
			
			Page<Dayclass> pageDayclassList = adminService.getPageDayclassList(dayclass, pageable);
			
			Page<DayclassDTO> pageDayclassDTOList = pageDayclassList.map(pageDayclass -> 
													            DayclassDTO.builder()
																            .dayclassNo(pageDayclass.getDayclassNo())
					                                 						.dayclassName(pageDayclass.getDayclassName())
					                                 						.dayclassPrice(pageDayclass.getDayclassPrice())
					                                 						.dayclassTime(pageDayclass.getDayclassTime())
					                                 						.dayclassUseYn(pageDayclass.getDayclassUseYn())
					                                 						.build()
											   				);
			response.setPageItems(pageDayclassDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	//데이클래스 등록
	@GetMapping("/insertDayclassView")
	public ModelAndView insertDayclassView() {
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("admin/dayclassReg.html");
			
		return mv;
	}
	
	@PostMapping("/insertDayclass")
	public void insertDayclass(DayclassDTO dayclassDTO, MultipartFile[] uploadFiles, 
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		
		Dayclass dayclass = Dayclass.builder()
									.dayclassTime(dayclassDTO.getDayclassTime())
									.dayclassUseYn(dayclassDTO.getDayclassUseYn())
									.dayclassName(dayclassDTO.getDayclassName())
									.dayclassMinName(dayclassDTO.getDayclassMinName())
									.dayclassDurationTime(dayclassDTO.getDayclassDurationTime())
									.dayclassPrice(dayclassDTO.getDayclassPrice())
									.dayclassDetails(dayclassDTO.getDayclassDetails())
									.build();
	
		//DB에 입력될 파일 정보 리스트
		List<ImgFile> uploadFileList = new ArrayList<ImgFile>();
		
		if(uploadFiles.length > 0) {
			String attachPath = request.getSession().getServletContext().getRealPath("/")
					+ "/dayclass/";
			
			File directory = new File(attachPath);
			
			if(!directory.exists()) {
				directory.mkdir();
			}
			
			//multipartFile 형식의 데이터를 DB 테이블에 맞는 구조로 변경
			for(int i = 0; i < uploadFiles.length; i++) {
				MultipartFile file = uploadFiles[i];
				
				if(!file.getOriginalFilename().equals("") &&
					file.getOriginalFilename() != null) {
					ImgFile dayclassFile = new ImgFile();
					
					dayclassFile = FileUtils.parseFileInfo(file, attachPath);
					
					uploadFileList.add(dayclassFile);
				}
			}
		}
		
		adminService.insertDayclass(dayclass, uploadFileList);
		
		response.sendRedirect("/admin/dayclassList");
	}

	//데이클래스 상세보기
	@GetMapping("/dayclass/{dayclassNo}")
	public ModelAndView getDayclass(@PathVariable int dayclassNo) {
		Dayclass dayclass = adminService.getDayclass(dayclassNo);
		
		DayclassDTO dayclassDTO = DayclassDTO.builder()
											.dayclassNo(dayclass.getDayclassNo())											
											.dayclassTime(dayclass.getDayclassTime())
											.dayclassUseYn(dayclass.getDayclassUseYn())
											.dayclassName(dayclass.getDayclassName())
											.dayclassMinName(dayclass.getDayclassMinName())
											.dayclassDurationTime(dayclass.getDayclassDurationTime())
											.dayclassPrice(dayclass.getDayclassPrice())
											.dayclassDetails(dayclass.getDayclassDetails())
											.build();
		
		List<ImgFile> dayclassFileList = adminService.getDayclassFileList(dayclassNo);
		
		//imgFileDTO를 담는 List
		List<ImgFileDTO> imgFileDTOList = new ArrayList<ImgFileDTO>();
		
		for(ImgFile imgFile : dayclassFileList) {
			ImgFileDTO imgFileDTO = ImgFileDTO.builder()
													.fileReferNo(dayclassNo)
													.fileNo(dayclassNo)
													.fileNo(imgFile.getFileNo())
													.fileName(imgFile.getFileName())
													.fileOriginName(imgFile.getFileOriginName())
													.filePath(imgFile.getFilePath())
													.fileType(imgFile.getFileType())
													.build();
			
			imgFileDTOList.add(imgFileDTO);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/dayclassDetail.html");
		mv.addObject("getDayclass", dayclassDTO);
		mv.addObject("dayclassFileList", imgFileDTOList);
		
		return mv;
	}
	
	//데이클래스 수정
	@Transactional
	@PutMapping("/updateDayclass")
	public ResponseEntity<?> updateDayclass(DayclassDTO dayclassDTO,
			MultipartFile[] uploadFiles, MultipartFile[] changedFiles,
			HttpServletRequest request, @RequestParam("originFiles") String originFiles) throws IOException { 
		
		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
		
		List<ImgFileDTO> originFileList = new ObjectMapper().readValue(originFiles, 
												new TypeReference<List<ImgFileDTO>>() {});
		
		for(int i = 0; i < originFileList.size(); i++) {
			System.out.println(originFileList.get(i).toString());
		}
		
		String attachPath = request.getSession().getServletContext().getRealPath("/") +
					"/dayclass/";
		
		File directory = new File(attachPath);
		
		if(!directory.exists()) {
			directory.mkdir();
		}
		
		//DB에서 수정, 삭제, 추가 될 파일 정보
		List<ImgFile> uFileList = new ArrayList<ImgFile>();
		
		try {
			Dayclass dayclass = Dayclass.builder()
										.dayclassNo(dayclassDTO.getDayclassNo())
										.dayclassTime(dayclassDTO.getDayclassTime())
										.dayclassUseYn(dayclassDTO.getDayclassUseYn())
										.dayclassName(dayclassDTO.getDayclassName())
										.dayclassMinName(dayclassDTO.getDayclassMinName())
										.dayclassDurationTime(dayclassDTO.getDayclassDurationTime())
										.dayclassPrice(dayclassDTO.getDayclassPrice())
										.dayclassDetails(dayclassDTO.getDayclassDetails())
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
							
							imgFile.setFileReferNo(dayclassDTO.getDayclassNo());
							imgFile.setFileNo(originFileList.get(i).getFileNo());
							imgFile.setFileStatus("U");
							imgFile.setFileType("class");
							
							uFileList.add(imgFile);
						}
					}
				//삭제되는 파일 처리
				} else if (originFileList.get(i).getFileStatus().equals("D")) {
					ImgFile imgFile = new ImgFile();
					
					imgFile.setFileReferNo(dayclassDTO.getDayclassNo());
					imgFile.setFileNo(originFileList.get(i).getFileNo());
					imgFile.setFileStatus("D");
					imgFile.setFileType("class");
					
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
						
						imgFile.setFileReferNo(dayclassDTO.getDayclassNo());
						imgFile.setFileStatus("I");
						imgFile.setFileType("class");
						
						uFileList.add(imgFile);
					}
				}
			}

			adminService.updateDayclass(dayclass, uFileList);
			
			DayclassDTO returnDayclass = DayclassDTO.builder()
													.dayclassNo(dayclass.getDayclassNo())
													.dayclassTime(dayclass.getDayclassTime())
													.dayclassUseYn(dayclass.getDayclassUseYn())
													.dayclassName(dayclass.getDayclassName())
													.dayclassMinName(dayclass.getDayclassMinName())
													.dayclassDurationTime(dayclass.getDayclassDurationTime())
													.dayclassPrice(dayclass.getDayclassPrice())
													.dayclassDetails(dayclass.getDayclassDetails())
													.build();
			
			List<ImgFile> imgFileList = adminService.getDayclassFileList(dayclass.getDayclassNo());
			
			List<ImgFileDTO> imgFileDTOList = new ArrayList<ImgFileDTO>();
			
			for(ImgFile imgFile : imgFileList) {
				ImgFileDTO imgFileDTO = ImgFileDTO.builder()
													.fileReferNo(dayclass.getDayclassNo())
													.fileNo(imgFile.getFileNo())
													.fileName(imgFile.getFileName())
													.fileOriginName(imgFile.getFileOriginName())
													.filePath(imgFile.getFilePath())
													.fileType(imgFile.getFileType())
													.build();
				
				imgFileDTOList.add(imgFileDTO);
			}
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			returnMap.put("getDayclass", returnDayclass);
			returnMap.put("imgFileList", imgFileDTOList);
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
		
	}
	
	//클래스수정 상세보기
	@GetMapping("/dayclassUpdate/{dayclassNo}")
	public ModelAndView getUpdateDayclass(@PathVariable int dayclassNo) {
		Dayclass dayclass = adminService.getDayclass(dayclassNo);
		
		DayclassDTO dayclassDTO = DayclassDTO.builder()
												.dayclassNo(dayclass.getDayclassNo())
												.dayclassTime(dayclass.getDayclassTime())
												.dayclassUseYn(dayclass.getDayclassUseYn())
												.dayclassName(dayclass.getDayclassName())
												.dayclassMinName(dayclass.getDayclassMinName())
												.dayclassDurationTime(dayclass.getDayclassDurationTime())
												.dayclassPrice(dayclass.getDayclassPrice())
												.dayclassDetails(dayclass.getDayclassDetails())
												.build();
		
		List<ImgFile> dayclassFileList = adminService.getDayclassFileList(dayclassNo);
		
		//imgFileDTO를 담는 List
		List<ImgFileDTO> imgFileDTOList = new ArrayList<ImgFileDTO>();
		
		for(ImgFile imgFile : dayclassFileList) {
			ImgFileDTO imgFileDTO = ImgFileDTO.builder()
													.fileReferNo(dayclassNo)
													.fileNo(dayclassNo)
													.fileNo(imgFile.getFileNo())
													.fileName(imgFile.getFileName())
													.fileOriginName(imgFile.getFileOriginName())
													.filePath(imgFile.getFilePath())
													.fileType(imgFile.getFileType())
													.build();
			
			imgFileDTOList.add(imgFileDTO);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/dayclassUpdate.html");
		mv.addObject("getDayclass", dayclassDTO);
		mv.addObject("dayclassFileList", imgFileDTOList);
		
		return mv;
	}
	
	//클래스 삭제
	@DeleteMapping("/delDayclass")
	public void deleteDayclass(@RequestParam("dayclassNo") int dayclassNo) {
		adminService.deleteDayclass(dayclassNo);
	}
	
	//관리자가 클래스를 삭제하는 경우 ajax를 이용해 백단에 전송
	@PostMapping("/saveDayclassList")
	public ResponseEntity<?> saveDayclassList(@RequestParam("changeRows") String changeRows,
			@PageableDefault(page = 0, size = 10) Pageable pageable) throws JsonMappingException, JsonProcessingException {
		ResponseDTO<DayclassDTO> response = new ResponseDTO<>();
		List<Map<String, Object>> changeRowsList = new ObjectMapper().readValue(changeRows, 
											new TypeReference<List<Map<String, Object>>>() {});
		
		try {
			adminService.saveDayclassList(changeRowsList);
			
			Dayclass dayclass = Dayclass.builder()
										.searchCondition("")
										.searchKeyword("")
										.build();

			Page<Dayclass> pageDayclassList = adminService.getPageDayclassList(dayclass, pageable);
			
			Page<DayclassDTO> pageDayclassDTOList = pageDayclassList.map(pageDayclass ->
														DayclassDTO.builder()
																	.dayclassNo(pageDayclass.getDayclassNo())
																	.dayclassNo(pageDayclass.getDayclassNo())											
																	.dayclassTime(pageDayclass.getDayclassTime())
																	.dayclassUseYn(pageDayclass.getDayclassUseYn())
																	.dayclassName(pageDayclass.getDayclassName())
																	.dayclassMinName(pageDayclass.getDayclassMinName())
																	.dayclassDurationTime(pageDayclass.getDayclassDurationTime())
																	.dayclassPrice(pageDayclass.getDayclassPrice())
																	.dayclassDetails(pageDayclass.getDayclassDetails())
																	.build()
			);
			
			response.setPageItems(pageDayclassDTOList);
			
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
//////////////////////////////user//////////////////////////////
	//회원 리스트
	@GetMapping("/userList")
	public ModelAndView getUserList(UserDTO userDTO,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		User user = User.builder()
						.userName(userDTO.getUserName())
						.userId(userDTO.getUserId())
						.userRegdate(LocalDateTime.now())
						.searchCondition(userDTO.getSearchCondition())
						.searchKeyword(userDTO.getSearchKeyword())
						.build();
		
		Page<User> pageUserList = adminService.getPageUserList(user, pageable);
		
		Page<UserDTO> pageUserDTOList = pageUserList.map(pageUser -> 
	                             						UserDTO.builder()
	                             								.userNo(pageUser.getUserNo())
	                             								.userName(pageUser.getUserName())
	                             								.userId(pageUser.getUserId())
	                             								.userRegdate(pageUser.getUserRegdate() == null?
	 	                                                               null :
	 		                                                               pageUser.getUserRegdate().toString())
	                                         					.build()
                                         					);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/userList.html");
		mv.addObject("pageUserList", pageUserDTOList);
		
		if(userDTO.getSearchCondition() != null && !userDTO.getSearchCondition().equals("")) {
			mv.addObject("searchCondition", userDTO.getSearchCondition());
		}
		
		if(userDTO.getSearchKeyword() != null && !userDTO.getSearchKeyword().equals("")) {
			mv.addObject("searchKeyword", userDTO.getSearchKeyword());
		}
		
		return mv;
	}
	
	//회원리스트 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/userList")
	public ResponseEntity<?> getUserPageList(UserDTO userDTO,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		ResponseDTO<UserDTO> response = new ResponseDTO<>();
		try {
			User user = User.builder()
							.searchCondition(userDTO.getSearchCondition())
							.searchKeyword(userDTO.getSearchKeyword())
							.build();

			Page<User> pageUserList = adminService.getPageUserList(user, pageable);
			
			Page<UserDTO> pageUserDTOList = pageUserList.map(pageUser -> 
													            UserDTO.builder()
															            .userNo(pageUser.getUserNo())
			                             								.userName(pageUser.getUserName())
			                             								.userId(pageUser.getUserId())
			                             								.userRegdate(pageUser.getUserRegdate() == null?
			 	                                                               null :
			 		                                                               pageUser.getUserRegdate().toString())
			                                         					.build()
											   				);
			response.setPageItems(pageUserDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	//관리자가 회원을 삭제하는 경우 ajax를 이용해 백단에 전송
	@PostMapping("/saveUserList")
	public ResponseEntity<?> saveUserList(@RequestParam("changeRows") String changeRows,
			@PageableDefault(page = 0, size = 10) Pageable pageable) throws JsonMappingException, JsonProcessingException {
		ResponseDTO<UserDTO> response = new ResponseDTO<>();
		List<Map<String, Object>> changeRowsList = new ObjectMapper().readValue(changeRows, 
											new TypeReference<List<Map<String, Object>>>() {});
		
		try {
			adminService.saveUserList(changeRowsList);
			
			User user = User.builder()
							.searchCondition("")
							.searchKeyword("")
							.build();

			Page<User> pageUserList = adminService.getPageUserList(user, pageable);
			
			Page<UserDTO> pageUserDTOList = pageUserList.map(pageUser ->
														UserDTO.builder()
																.userNo(pageUser.getUserNo())
		                         								.userName(pageUser.getUserName())
		                         								.userId(pageUser.getUserId())
		                         								.userRegdate(pageUser.getUserRegdate() == null?
			                                                               null :
				                                                               pageUser.getUserRegdate().toString())
		                                     					.build()
			);
			
			response.setPageItems(pageUserDTOList);
			
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
//////////////////////////////user-팝업창//////////////////////////////
	//회원상세보기_팝업
	@GetMapping("/userInfoCheck/{userId}")
	public ModelAndView getUserInfoCheck(@PathVariable String userId) {
		User user = adminService.getUserInfoCheck(userId);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/userInfoCheck.html");
		mv.addObject("getUserInfoCheck", user);
		
		return mv;
	}
	
	//회원 리스트_리뷰팝업
	@GetMapping("/userRvwList")
	public ModelAndView getUserRvwList(ReviewDTO reviewDTO,
			@PageableDefault(page = 0, size = 3) Pageable pageable) {
		Review reviewParam = Review.builder()
							  .rvwWriter(reviewDTO.getRvwWriter())
							  .rvwType(reviewDTO.getRvwType())
							  .build();
		
		Page<Review> reviewList = adminService.getUserRvwPageList(reviewParam, pageable);
		
		Page<ReviewDTO> reviewListDTO = reviewList.map(review -> 
															ReviewDTO.builder()
																	.rvwNo(review.getRvwNo())
																	.rvwReferNo(review.getRvwReferNo())
																	.rvwType(review.getRvwType())
																	.rvwContent(review.getRvwContent())
																	.rvwWriter(review.getRvwWriter())
																	.rvwRegdate(review.getRvwRegdate().toString())
																	.rvwScore(review.getRvwScore())
																	.build()
																	);
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/userRvwList.html");
		mv.addObject("getUserRvwPageList", reviewListDTO);
		mv.addObject("rvwType", reviewDTO.getRvwType());
		
		return mv;
	}
	
	//회원 리스트_리뷰팝업 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/userRvwList")
	public ResponseEntity<?> getUserRvwPageList(ReviewDTO reviewDTO,
			@PageableDefault(page = 0, size = 3) Pageable pageable) {
		ResponseDTO<ReviewDTO> response = new ResponseDTO<>();
		try {
			Review reviewParam = Review.builder()
					  .rvwWriter(reviewDTO.getRvwWriter())
					  .rvwType(reviewDTO.getRvwType())
					  .build();

			Page<Review> pageUserReviewList = adminService.getUserRvwPageList(reviewParam, pageable);
			
			Page<ReviewDTO> pageUserReviewDTOList = pageUserReviewList.map(pageReview -> 
																			ReviewDTO.builder()
																					.rvwNo(pageReview.getRvwNo())
																					.rvwReferNo(pageReview.getRvwReferNo())
																					.rvwType(pageReview.getRvwType())
																					.rvwContent(pageReview.getRvwContent())
																					.rvwWriter(pageReview.getRvwWriter())
																					.rvwRegdate(pageReview.getRvwRegdate().toString())
																					.rvwScore(pageReview.getRvwScore())
																					.build()

											   				);
			response.setPageItems(pageUserReviewDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	//////////////////////////////QnA팝업
	//회원 리스트_QnA팝업
	@GetMapping("/userQnAList")
	public ModelAndView getUserQnAList(BoardDTO boardDTO,
			@PageableDefault(page = 0, size = 3) Pageable pageable) {
		Board boardParam = Board.builder()
							  .boardWriter(boardDTO.getBoardWriter())
							  .cateCode(boardDTO.getCateCode())
							  .build();
		
		Page<Board> qnaList = adminService.getUserQnAPageList(boardParam, pageable);
		
		Page<BoardDTO> qnaListDTO = qnaList.map(qna -> 
													BoardDTO.builder()
															.boardTitle(qna.getBoardTitle())
															.boardWriter(qna.getBoardWriter())
															.boardReply(qna.getBoardReply())
															.boardRegdate(qna.getBoardRegdate() == null?
	 	                                                               null :
	 	                                                            	  qna.getBoardRegdate().toString())
															.boardCnt(qna.getBoardCnt())
															.cateCode(qna.getCateCode())
															.build()
													);
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/userQnAList.html");
		mv.addObject("getUserQnAPageList", qnaListDTO);
		
		return mv;
	}
		
	//회원 리스트_QnA팝업 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/userQnAList")
	public ResponseEntity<?> getUserQnAPageList(BoardDTO boardDTO,
			@PageableDefault(page = 0, size = 3) Pageable pageable) {
		System.out.println("boardDTO.getCateCode()==================================" + boardDTO.getCateCode());
		ResponseDTO<BoardDTO> response = new ResponseDTO<>();
		try {
			Board boardParam = Board.builder()
					  .boardWriter(boardDTO.getBoardWriter())
					  .cateCode(boardDTO.getCateCode())
					  .build();

			Page<Board> pageUserQnAList = adminService.getUserQnAPageList(boardParam, pageable);
			
			Page<BoardDTO> pageUserQnADTOList = pageUserQnAList.map(pageQnA -> 
																		BoardDTO.builder()
																		.boardTitle(pageQnA.getBoardTitle())
																		.boardWriter(pageQnA.getBoardWriter())
																		.boardReply(pageQnA.getBoardReply())
																		.boardRegdate(pageQnA.getBoardRegdate() == null?
				 	                                                               null :
				 	                                                            	  pageQnA.getBoardRegdate().toString())
																		.boardCnt(pageQnA.getBoardCnt())
																		.cateCode(pageQnA.getCateCode())
																		.build()

											   				);
			response.setPageItems(pageUserQnADTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}



	
	
//////////////////////////////주문(order) 및 예약(reser)관리//////////////////////////////
	//reser_dayclass 리스트
	@GetMapping("/reserDayclassList")
	public ModelAndView getReserDayclassList(ReserDTO reserDTO,
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		Reser reser = Reser.builder()
							.searchCondition(reserDTO.getSearchCondition())
							.searchKeyword(reserDTO.getSearchKeyword())
							.build();
		
		Page<Reser> pageReserList = adminService.getPageReserList(reser, pageable);
		
		Page<ReserDTO> pageReserDTOList = pageReserList.map(pageReser -> 
                                             						ReserDTO.builder()
                                             								.reserNo(pageReser.getReserNo())
                                             								.partiName(pageReser.getPartiName())
                                             								.userId(pageReser.getUserId())
                                             								.classNo(pageReser.getClassNo())
                                             								.partiDate(pageReser.getPartiDate())
                                             								.partiTime(pageReser.getPartiTime())
                                             								.reserStatus(pageReser.getReserStatus())
                                             								.partiStatus(pageReser.getPartiStatus())
                                             								.build()
	                                             					);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/reserDayclassList.html");
		mv.addObject("pageReserList", pageReserDTOList);
		
		if(reserDTO.getSearchCondition() != null && !reserDTO.getSearchCondition().equals("")) {
			mv.addObject("searchCondition", reserDTO.getSearchCondition());
		}
		
		if(reserDTO.getSearchKeyword() != null && !reserDTO.getSearchKeyword().equals("")) {
			mv.addObject("searchKeyword", reserDTO.getSearchKeyword());
		}
		
		return mv;
	}
	
	//클래스 예약 리스트 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/reserDayclassList")
	public ResponseEntity<?> getReserDayclassPageList(ReserDTO reserDTO,
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		ResponseDTO<ReserDTO> response = new ResponseDTO<>();
		try {
			Reser reser = Reser.builder()
							.searchCondition(reserDTO.getSearchCondition())
							.searchKeyword(reserDTO.getSearchKeyword())
							.build();
			Page<Reser> pageReserList = adminService.getPageReserList(reser, pageable);
			
			Page<ReserDTO> pageReserDTOList = pageReserList.map(pageReser -> 
											            ReserDTO.builder()
													            .reserNo(pageReser.getReserNo())
		                         								.partiName(pageReser.getPartiName())
		                         								.userId(pageReser.getUserId())
		                         								.classNo(pageReser.getClassNo())
		                         								.partiDate(pageReser.getPartiDate())
		                         								.partiTime(pageReser.getPartiTime())
		                         								.reserStatus(pageReser.getReserStatus())
		                         								.partiStatus(pageReser.getPartiStatus())
		                         								.build()
											   				);
			response.setPageItems(pageReserDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	//클래스 예약관리_참여현황 수정
	@PutMapping("/updatePartiStatus")
	public ResponseEntity<?> updatePartiStatus(ReserDTO reserDTO,
			HttpServletRequest request) throws IOException { 
		System.out.println("reserDTO==============" + reserDTO.toString());
		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
		
		try {
			Reser reser = Reser.builder()
					.reserNo(reserDTO.getReserNo())
					.partiStatus(reserDTO.getPartiStatus())
					.build();
					

			adminService.updatePartiStatus(reser);
			
			ReserDTO returnPartiStatus = ReserDTO.builder()	
													.reserNo(reser.getReserNo())
													.reserDate(reser.getReserDate() == null ?
															null :
																reser.getReserDate().toString())
													.reserStatus(reser.getReserStatus())
													.partiName(reser.getPartiName())
													.partiTel(reser.getPartiTel())
													.partiTime(reser.getPartiTime())
													.classNo(reser.getClassNo())
													.reserPersonCnt(reser.getReserPersonCnt())
													.orderName(reser.getOrderName())
													.orderTel(reser.getOrderTel())
													.request(reser.getRequest())
													.reserPayment(reser.getReserPayment())
													.depositor(reser.getDepositor())
													.reserTotalPrice(reser.getReserTotalPrice())
													.partiDate(reser.getPartiDate())
													.classPrice(reser.getClassPrice())
													.partiStatus(reser.getPartiStatus())
													.build();
			
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			returnMap.put("updatePartiStatus", returnPartiStatus);
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
		
	}
	
	//주문 및 예약관리
	//order_item 리스트
	@GetMapping("/orderItemList")
	public ModelAndView getOrderItemList(OrderDTO orderDTO,
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		Order order = Order.builder()
							.searchCondition(orderDTO.getSearchCondition())
							.searchKeyword(orderDTO.getSearchKeyword())
							.build();
		
		Page<Order> pageOrderList = adminService.getPageOrderList(order, pageable);
		
		Page<OrderDTO> pageOrderDTOList = pageOrderList.map(pageOrder -> 
                                             						OrderDTO.builder()
                                             								.orderNo(pageOrder.getOrderNo())
                                             								.userId(pageOrder.getUserId())
                                             								.orderName(pageOrder.getOrderName())
                                             								.orderTotalPrice(pageOrder.getOrderTotalPrice())
                                             								.orderPayment(pageOrder.getOrderPayment())
                                             								.orderDate(pageOrder.getOrderDate() == null ?
                                             										null :
                                             										pageOrder.getOrderDate().toString())
                                             								.orderStatus(pageOrder.getOrderStatus())
                                             								.build()
	                                             					);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/orderItemList.html");
		mv.addObject("getOrderList", pageOrderDTOList);
		
		if(orderDTO.getSearchCondition() != null && !orderDTO.getSearchCondition().equals("")) {
			mv.addObject("searchCondition", orderDTO.getSearchCondition());
		}
		
		if(orderDTO.getSearchKeyword() != null && !orderDTO.getSearchKeyword().equals("")) {
			mv.addObject("searchKeyword", orderDTO.getSearchKeyword());
		}
		
		return mv;
	}
	
	//상품 주문 리스트 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/orderItemList")
	public ResponseEntity<?> getOrderItemPageList(OrderDTO orderDTO,
			@PageableDefault(page = 0, size = 5) Pageable pageable) {
		ResponseDTO<OrderDTO> response = new ResponseDTO<>();
		try {
			Order order = Order.builder()
								.searchCondition(orderDTO.getSearchCondition())
								.searchKeyword(orderDTO.getSearchKeyword())
								.build();
			Page<Order> pageOrderList = adminService.getPageOrderList(order, pageable);
			
			Page<OrderDTO> pageOrderDTOList = pageOrderList.map(pageOrder -> 
											            OrderDTO.builder()
													            .orderNo(pageOrder.getOrderNo())
		                         								.userId(pageOrder.getUserId())
		                         								.orderName(pageOrder.getOrderName())
		                         								.orderTotalPrice(pageOrder.getOrderTotalPrice())
		                         								.orderPayment(pageOrder.getOrderPayment())
		                         								.orderDate(pageOrder.getOrderDate() == null ?
		                         										null :
		                         										pageOrder.getOrderDate().toString())
		                         								.orderStatus(pageOrder.getOrderStatus())
		                         								.build()
											   				);
			response.setPageItems(pageOrderDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	//상품 주문관리_주문상태 수정
	@PutMapping("/updateOrderStatus")
	public ResponseEntity<?> updateOrderStatus(OrderDTO orderDTO,
			HttpServletRequest request) throws IOException { 
		System.out.println("orderDTO==============" + orderDTO.toString());
		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
		
		try {
			Order order = Order.builder()
					.orderNo(orderDTO.getOrderNo())
					.orderStatus(orderDTO.getOrderStatus())
					.build();
					

			adminService.updateOrderStatus(order);
			
			OrderDTO returnOrderStatus = OrderDTO.builder()
												.userId(order.getUserId())
												.orderNo(order.getOrderNo())
												.orderDate(order.getOrderDate() == null ?
														null :
															order.getOrderDate().toString())
												.orderStatus(order.getOrderStatus())
												.orderName(order.getOrderName())
												.orderTel(order.getOrderTel())
												.shippingAddr1(order.getShippingAddr1())
												.shippingAddr2(order.getShippingAddr2())
												.shippingAddr3(order.getShippingAddr3())
												.orderDeliFee(order.getOrderDeliFee())
												.orderTotalPrice(order.getOrderTotalPrice())
												.orderPayment(order.getOrderPayment())
												.reciName(order.getReciName())
												.reciTel(order.getReciTel())
												.orderMail(order.getOrderMail())
												.orderMessage(order.getOrderMessage())
												.depositor(order.getDepositor())
												.orderTotalPayPrice(order.getOrderTotalPayPrice())
												.build();
			
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			returnMap.put("updateOrderStatus", returnOrderStatus);
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
		
	}
	
	
//////////////////////////////review//////////////////////////////
	//리뷰리스트
	@GetMapping("/reviewList")
	public ModelAndView getReviewList(ReviewDTO reviewDTO,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		Review review = Review.builder()
							.searchCondition(reviewDTO.getSearchCondition())
							.searchKeyword(reviewDTO.getSearchKeyword())
							.build();
		
		Page<Review> pageReviewList = adminService.getPageReviewList(review, pageable);
	      
	    Page<ReviewDTO> pageReviewDTOList = pageReviewList.map(pageReview -> 
																    ReviewDTO.builder()
																				.rvwNo(pageReview.getRvwNo())
																				.rvwReferNo(pageReview.getRvwReferNo())
																				.rvwType(pageReview.getRvwType())
																				.rvwContent(pageReview.getRvwContent())
																				.rvwWriter(pageReview.getRvwWriter())
																				.rvwRegdate(pageReview.getRvwRegdate() == null ?
																						null :
																						pageReview.getRvwRegdate().toString())
																				.rvwScore(pageReview.getRvwScore())
																				.build()
	                                             );
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("admin/reviewList.html");
		mv.addObject("getPageReviewList", pageReviewDTOList);
		
		if(reviewDTO.getSearchCondition() != null && !reviewDTO.getSearchCondition().equals("")) {
			mv.addObject("searchCondition", reviewDTO.getSearchCondition());
		}
		
		if(reviewDTO.getSearchKeyword() != null && !reviewDTO.getSearchKeyword().equals("")) {
			mv.addObject("searchKeyword", reviewDTO.getSearchKeyword());
		}
		
		return mv;
	}
	
	//리뷰 리스트 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/reviewList")
	public ResponseEntity<?> getReviewPageList(ReviewDTO reviewDTO,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		ResponseDTO<ReviewDTO> response = new ResponseDTO<>();
		try {
			Review review = Review.builder()
								.searchCondition(reviewDTO.getSearchCondition())
								.searchKeyword(reviewDTO.getSearchKeyword())
								.build();
			Page<Review> pageReviewList = adminService.getPageReviewList(review, pageable);
			
			Page<ReviewDTO> pageReviewDTOList = pageReviewList.map(pageReview -> 
															            ReviewDTO.builder()
																	            .rvwNo(pageReview.getRvwNo())
																				.rvwReferNo(pageReview.getRvwReferNo())
																				.rvwType(pageReview.getRvwType())
																				.rvwContent(pageReview.getRvwContent())
																				.rvwWriter(pageReview.getRvwWriter())
																				.rvwRegdate(pageReview.getRvwRegdate() == null ?
																						null :
																						pageReview.getRvwRegdate().toString())
																				.rvwScore(pageReview.getRvwScore())
																				.build()
											   				);
			response.setPageItems(pageReviewDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	//관리자가 리뷰를 삭제하는 경우 ajax를 이용해 백단에 전송
	@PostMapping("/saveReviewList")
	public ResponseEntity<?> saveReviewList(@RequestParam("changeRows") String changeRows,
			@PageableDefault(page = 0, size = 10) Pageable pageable) throws JsonMappingException, JsonProcessingException {
		ResponseDTO<ReviewDTO> response = new ResponseDTO<>();
		List<Map<String, Object>> changeRowsList = new ObjectMapper().readValue(changeRows, 
											new TypeReference<List<Map<String, Object>>>() {});
		
		try {
			adminService.saveReviewList(changeRowsList);
			
			Review review = Review.builder()
										.searchCondition("")
										.searchKeyword("")
										.build();

			Page<Review> pageReviewList = adminService.getPageReviewList(review, pageable);
			System.out.println(pageReviewList);
			Page<ReviewDTO> pageReviewDTOList = pageReviewList.map(pageReview ->
																		ReviewDTO.builder()
																			.rvwNo(pageReview.getRvwNo())
																			.rvwReferNo(pageReview.getRvwReferNo())
																			.rvwType(pageReview.getRvwType())
																			.rvwContent(pageReview.getRvwContent())
																			.rvwWriter(pageReview.getRvwWriter())
																			.rvwRegdate(pageReview.getRvwRegdate() == null ?
																					null :
																					pageReview.getRvwRegdate().toString())
																			.rvwScore(pageReview.getRvwScore())
																			.build()
			);
			
			response.setPageItems(pageReviewDTOList);
			
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
}
