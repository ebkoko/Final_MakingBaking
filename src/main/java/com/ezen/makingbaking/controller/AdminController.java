package com.ezen.makingbaking.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.common.FileUtils;
import com.ezen.makingbaking.dto.ItemDTO;
import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.service.admin.AdminService;



@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;
	
	
	//item
	//상품리스트
	@GetMapping("/itemList")
	public ModelAndView getItemList(ItemDTO itemDTO,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		Item item = Item.builder()
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
	
	
	//insert
	@GetMapping("/insertItemView")
	public ModelAndView insertItemView() {
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("admin/itemReg.html");
			
		return mv;
	}
	
	@PostMapping("/insertItem")
	public void insertItem(ItemDTO itemDTO, MultipartFile[] uploadFiles, 
			HttpServletResponse response, HttpServletRequest request) throws IOException {
		Item item = Item.builder()
						.itemStatus(itemDTO.getItemStatus())
						.itemCate(itemDTO.getItemCate())
						.itemName(itemDTO.getItemName())
						.itemDetails(itemDTO.getItemDetails())
						.itemPrice(itemDTO.getItemPrice())
						.itemStatus(itemDTO.getItemStatus())
						.itemRegdate(LocalDateTime.now())
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
					
					//C:\Users\EZEN\AppData\Local\Temp\tomcat.9090.542208420639967599에서 이미지 확인 가능.
					//src > main에서 webapp 폴더 생성. webapp 폴더 안에 upload 폴더 생성을 한다면,
					//깃공유시, 조원들과 이미지를 함께 볼 수 있게됨.
				}
			}
		}
		
		adminService.insertItem(item, uploadFileList);
		
		response.sendRedirect("/item/itemList");
	}
	
	
	
	
	
	
	@PutMapping("/updateItem")
//	public ResponseEntity<?> updateItem(ItemDTO itemDTO, HttpServletResponse response)
	
	@DeleteMapping("/delItem")
	public void deleteItem(@RequestParam("itemNo") int itemNo) {
		adminService.deleteItem(itemNo);
	}
	
	
	
	
	
	//dayclass
	//원데이클래스 리스트
//	@GetMapping("/dayclassList")
//	public ModelAndView getDayclassList(DayclassDTO dayclassDTO,
//			@PageableDefault(page = 0, size = 10) Pageable pageable) {
//		Dayclass dayclass = Dayclass.builder()
//									.searchCondition(dayclassDTO.getSearchCondition())
//									.searchKeyword(dayclassDTO.getSearchKeyword())
//									.build();
//		List<Dayclass> dayclassList = adminService.getDayclassList(dayclass);
//		
//		Page<Dayclass> pageDayclassList = adminService.getPageItemList(item, pageable);
//	      
//	      Page<ItemDTO> pageItemDTOList = pageItemList.map(pageItem -> 
//	                                             ItemDTO.builder()
//	                                                      .itemNo(pageItem.getItemNo())
//	                                                      .itemName(pageItem.getItemName())
//	                                                      .itemPrice(pageItem.getItemPrice())
//	                                                      .itemRegdate(
//	                                                                     pageItem.getItemRegdate() == null?
//	                                                               null :
//	                                                               pageItem.getItemRegdate().toString())
//	                                                      .itemStatus(pageItem.getItemStatus())
//	                                                      .build()
//	                                             );
//		
//		List<ItemDTO> getItemList = new ArrayList<ItemDTO>();
//		for(int i = 0; i < itemList.size(); i++) {
//			ItemDTO returnItem = ItemDTO.builder()
//											.itemNo(itemList.get(i).getItemNo())
//											.itemName(itemList.get(i).getItemName())
//											.itemPrice(itemList.get(i).getItemPrice())
//											.itemRegdate(
//													itemList.get(i).getItemRegdate() == null ?
//													null :
//													itemList.get(i).getItemRegdate().toString())
//
//											.itemStatus(itemList.get(i).getItemStatus())
//											.build();
//
//			getItemList.add(returnItem);
//		}
//		
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("admin/itemList.html");
//		mv.addObject("getItemList", pageItemDTOList);
//		
//		if(itemDTO.getSearchCondition() != null && !itemDTO.getSearchCondition().equals("")) {
//			mv.addObject("searchCondition", itemDTO.getSearchCondition());
//		}
//		
//		if(itemDTO.getSearchKeyword() != null && !itemDTO.getSearchKeyword().equals("")) {
//			mv.addObject("searchKeyword", itemDTO.getSearchKeyword());
//		}
//		
//		return mv;
//	}
//	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//@GetMapping("/")
	//public ModelAndView getItemFileList(Page pageable) { 
		//admin
	//}
}
