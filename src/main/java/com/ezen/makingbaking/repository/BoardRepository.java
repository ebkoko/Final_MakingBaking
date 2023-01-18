package com.ezen.makingbaking.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.ibatis.annotations.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Board;
import com.ezen.makingbaking.entity.Review;

@Transactional
public interface BoardRepository extends JpaRepository<Board, Integer> {
	// 카테고리 코드로 리스트 검색
	Page<Board> findByCateCodeOrderByBoardNoDesc(int cateCode, Pageable pageable);

	@Modifying
	@Query(value="UPDATE T_MB_BOARD SET BOARD_CNT = BOARD_CNT + 1 WHERE BOARD_NO = :boardNo", nativeQuery=true)
	void updateBoardCnt(@Param(value = "boardNo") int boardNo);
	
	@Modifying
	@Query(value="UPDATE T_MB_BOARD SET BOARD_REPLY = :boardReply, BOARD_REPLY_REGDATE = now() WHERE BOARD_NO = :boardNo", nativeQuery=true)
	void updateAnswer(@Param(value="boardNo") int boardNo, @Param(value="boardReply") String boardReply);

	@Query(value="SELECT C.*\r\n"
			+ "   FROM (\r\n"
			+ "         SELECT @rownum \\:=@rownum+1 AS ROWNUM\r\n"
			+ "             , A.*\r\n"
			+ "            FROM T_MB_BOARD A\r\n"
			+ "               , (SELECT @ROWNUM \\:= 0 ) B\r\n"
			+ "                WHERE (A.CATE_CODE = 1\r\n"
			+ "                OR A.CATE_CODE = 2)\r\n"
			+ "                AND A.BOARD_REPLY IS NOT NULL\r\n"
			+ "                ORDER BY A.BOARD_CNT DESC\r\n"
			+ "      ) C\r\n"
			+ "   WHERE C.ROWNUM < 4", nativeQuery=true)
	List<Board> selectFaqList(Board board);

	@Modifying
	@Query(value="UPDATE T_MB_BOARD SET BOARD_TITLE = :boardTitle, BOARD_CONTENT = :boardContent, CATE_CODE = :cateCode WHERE BOARD_NO = :boardNo", nativeQuery=true)
	void updateQna(@Param(value="boardTitle") String boardTitle, @Param(value="boardContent") String boardContent, @Param(value="cateCode") int cateCode, @Param(value="boardNo") int boardNo);
	
	@Modifying
	@Query(value="UPDATE T_MB_BOARD SET BOARD_TITLE = :boardTitle, BOARD_CONTENT = :boardContent WHERE BOARD_NO = :boardNo", nativeQuery=true)
	void updateBoard(@Param(value="boardTitle") String boardTitle, @Param(value="boardContent") String boardContent, @Param(value="boardNo") int boardNo);

	// myPage에서 내가 작성한 Qna 보기
	@Query(value="SELECT *\r\n"
			+ "	   FROM t_mb_board\r\n"
			+ "    WHERE (cate_code = 1 or cate_code = 2)\r\n"
			+ "    and board_writer = :boardWriter\r\n"
			+ "    order by board_no desc", nativeQuery=true)
	Page<Board> getMyQnaList(@Param("boardWriter") String boardWriter, Pageable pageable);
	
	@Query(value="select \r\n"
			+ "	*\r\n"
			+ "from\r\n"
			+ "	(\r\n"
			+ "		select\r\n"
			+ "			itemlike.item_no as like_no,\r\n"
			+ "            itemlike.user_id as user_id,\r\n"
			+ "            'item' as CATE_NAME,\r\n"
			+ "            item.item_name as like_name,\r\n"
			+ "            mbfile.file_name as file_name\r\n"
			+ "		from \r\n"
			+ "			t_mb_item as item,\r\n"
			+ "			t_mb_item_like as itemlike,\r\n"
			+ "            t_mb_file as mbfile            \r\n"
			+ "		where \r\n"
			+ "				item.item_no = itemlike.item_no\r\n"
			+ "            and \r\n"
			+ "				mbfile.file_refer_no = itemlike.item_no\r\n"
			+ "			and\r\n"
			+ "				mbfile.file_no = '1'\r\n"
			+ "			and\r\n"
			+ "				mbfile.file_type = 'item'                \r\n"
			+ "		union all\r\n"
			+ "		select\r\n"
			+ "			dayclasslike.dayclass_no as like_no,\r\n"
			+ "			dayclasslike.user_id as user_id,\r\n"
			+ "            'class' as CATE_NAME,\r\n"
			+ "			dayclass.dayclass_name as like_name,\r\n"
			+ "			mbfile.file_name\r\n"
			+ "		from \r\n"
			+ "			t_mb_dayclass as dayclass,\r\n"
			+ "			t_mb_dayclass_like as dayclasslike,\r\n"
			+ "			t_mb_file as mbfile\r\n"
			+ "		where \r\n"
			+ "				dayclass.dayclass_no = dayclasslike.dayclass_no\r\n"
			+ "			and\r\n"
			+ "				mbfile.file_refer_no = dayclasslike.dayclass_no\r\n"
			+ "			and\r\n"
			+ "				mbfile.file_no = '1'\r\n"
			+ "			and\r\n"
			+ "				mbfile.file_type = 'class'   \r\n"
			+ "    ) as A\r\n"
			+ "where\r\n"
			+ "	A.user_id = :userId" 
			, countQuery="select count(*)\r\n"
					+ "from \r\n"
					+ "(select \r\n"
					+ "	*\r\n"
					+ "from\r\n"
					+ "	(\r\n"
					+ "		select\r\n"
					+ "			itemlike.item_no as like_no,\r\n"
					+ "            itemlike.user_id as user_id,\r\n"
					+ "            'item' as CATE_NAME,\r\n"
					+ "            item.item_name as like_name,\r\n"
					+ "            mbfile.file_name as file_name\r\n"
					+ "		from \r\n"
					+ "			t_mb_item as item,\r\n"
					+ "			t_mb_item_like as itemlike,\r\n"
					+ "            t_mb_file as mbfile            \r\n"
					+ "		where \r\n"
					+ "				item.item_no = itemlike.item_no\r\n"
					+ "            and \r\n"
					+ "				mbfile.file_refer_no = itemlike.item_no\r\n"
					+ "			and\r\n"
					+ "				mbfile.file_no = '1'\r\n"
					+ "			and\r\n"
					+ "				mbfile.file_type = 'item'                \r\n"
					+ "		union all\r\n"
					+ "		select\r\n"
					+ "			dayclasslike.dayclass_no as like_no,\r\n"
					+ "			dayclasslike.user_id as user_id,\r\n"
					+ "            'class' as CATE_NAME,\r\n"
					+ "			dayclass.dayclass_name as like_name,\r\n"
					+ "			mbfile.file_name\r\n"
					+ "		from \r\n"
					+ "			t_mb_dayclass as dayclass,\r\n"
					+ "			t_mb_dayclass_like as dayclasslike,\r\n"
					+ "			t_mb_file as mbfile\r\n"
					+ "		where \r\n"
					+ "				dayclass.dayclass_no = dayclasslike.dayclass_no\r\n"
					+ "			and\r\n"
					+ "				mbfile.file_refer_no = dayclasslike.dayclass_no\r\n"
					+ "			and\r\n"
					+ "				mbfile.file_no = '1'\r\n"
					+ "			and\r\n"
					+ "				mbfile.file_type = 'class'   \r\n"
					+ "    ) as A\r\n"
					+ "where\r\n"
					+ "	A.user_id = :userId) T"
			, nativeQuery=true)	
	Page<CamelHashMap> getMyLikeList(@Param("userId") String userId, Pageable pageable);
	
	@Modifying
	@Query(value="DELETE FROM T_MB_DAYCLASS_LIKE WHERE USER_ID = :userId AND DAYCLASS_NO = :likeNo", nativeQuery=true)
	void classUnlike(@Param("likeNo") int likeNo, @Param("userId") String userId);
	
	@Modifying
	@Query(value="DELETE FROM T_MB_ITEM_LIKE WHERE USER_ID = :userId AND ITEM_NO = :likeNo", nativeQuery=true)
	void itemUnlike(@Param("likeNo") int likeNo, @Param("userId") String userId);
	
	//개인 리뷰팝업_선민
	Page<Board> findByCateCodeAndBoardWriter(@Param("cateCod") int cateCode, @Param("boardWirter") String boardWriter, Pageable pageable); //item, class
	Page<Board> findByBoardWriter(@Param("boardWriter") String boardWriter, Pageable pageable);

	
}
