package com.ezen.makingbaking.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.entity.Review;

@Transactional
public interface ItemRepository extends JpaRepository<Item, Integer> {

	//관리자 상품 검색_선민
	Page<Item> findByItemNameContaining(String searchKeyword, Pageable pageable); //이름
	Page<Item> findByItemCateContaining(String searchKeyword, Pageable pageable); //카테
	Page<Item> findByItemStatus(char searchKeyword, Pageable pageable); //상태
	Page<Item> findByItemNameContainingOrItemCateContainingOrItemStatus(String searchKeyword1, String searchKeyword2, char searchKeyword3, Pageable pageable);
	
	// 관리자 상품리스트&이미지파일 조인_선민
	@Query(value="SELECT COUNT(*) FROM (SELECT A.*, B.* FROM A.ITEM_NO = B.FILE_NO AND FILE_NO =:fileNo) C", 
			countQuery="" ,nativeQuery=true)
	public Page<CamelHashMap> getItemFileList(@Param("fileNo") int fileNo, Pageable pageable);
	
	// 장바구니에서 수량 변경할 때 재고보다 많이 변경할 수 없도록 itemNo의 itemStock 조회_은별
	@Query(value=" SELECT I.ITEM_STOCK FROM T_MB_ITEM I WHERE I.ITEM_NO = :itemNo", nativeQuery = true)
	int findItemStockByItemNo(@Param("itemNo") int itemNo);
	
	
	// 상품이미지 불러오는 쿼리_래중
	@Query(value="SELECT A.*"
			+ "		   , B.FILE_NO"
			+ "		   , B.FILE_NAME"
			+ "		   , B.FILE_ORIGIN_NAME"
			+ "		   , B.FILE_PATH"
			+ "		FROM T_MB_ITEM A"
			+ "		LEFT OUTER JOIN T_MB_FILE B"
			+ "		ON A.ITEM_NO = B.FILE_REFER_NO"
			+ "		AND B.FILE_TYPE = 'item'"
			+ "		AND B.FILE_NO = 1",
			countQuery = "SELECT COUNT(*)"
					+ "		FROM ("
					+ "					SELECT A.*"
					+ "						 , B.FILE_NAME"
					+ "						 , B.FILE_ORIGIN_NAME"
					+ "						 , B.FILE_PATH"
					+ "						FROM T_MB_ITEM A"
					+ "						LEFT OUTER JOIN T_MB_FILE B"
					+ "						ON A.ITEM_NO = B.FILE_REFER_NO"
					+ "						AND B.FILE_TYPE = 'item'"
					+ "						AND B.FILE_NO = 1"
					+ "			 ) C",
			nativeQuery = true)
	Page<CamelHashMap> findItemAndFile(Pageable pageable);
	
	@Query(value="SELECT A.*"
			+ "		   , B.FILE_NO"
			+ "		   , B.FILE_NAME"
			+ "		   , B.FILE_ORIGIN_NAME"
			+ "		   , B.FILE_PATH"
			+ "		FROM T_MB_ITEM A"
			+ "		LEFT OUTER JOIN T_MB_FILE B"
			+ "		ON A.ITEM_NO = B.FILE_REFER_NO"
			+ "		AND B.FILE_TYPE = 'item'"
			+ "		AND B.FILE_NO = 1"
			+ "		WHERE A.ITEM_CATE = :itemCate",
			countQuery = "SELECT COUNT(*)"
					+ "		FROM ("
					+ "					SELECT A.*"
					+ "						 , B.FILE_NAME"
					+ "						 , B.FILE_ORIGIN_NAME"
					+ "						 , B.FILE_PATH"
					+ "						FROM T_MB_ITEM A"
					+ "						LEFT OUTER JOIN T_MB_FILE B"
					+ "						ON A.ITEM_NO = B.FILE_REFER_NO"
					+ "						AND B.FILE_TYPE = 'item'"
					+ "						AND B.FILE_NO = 1"
					+ "						WHERE A.ITEM_CATE = :itemCate"
					+ "			 ) C",
			nativeQuery = true)
	Page<CamelHashMap> findItemAndFile(Pageable pageable, @Param("itemCate") String itemCate);
	
	@Query(value="SELECT COUNT(*) FROM T_MB_ITEM", nativeQuery=true)
	int getItemListCnt();
	
	@Query(value="SELECT COUNT(*) FROM T_MB_ITEM WHERE ITEM_CATE = :itemCate", nativeQuery=true)
	int getItemListCnt(@Param("itemCate") String itemCate);
	
	//Page<Review> findByItemNoAndPageble(int itemNo, Pageable pageable);
	
	
	
}
