package com.ezen.makingbaking.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.entity.Board;

@Transactional
public interface BoardRepository extends JpaRepository<Board, Integer> {
	// 카테고리 코드로 리스트 검색
	Page<Board> findByCateCode(int cateCode, Pageable pageable);

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
	
}
