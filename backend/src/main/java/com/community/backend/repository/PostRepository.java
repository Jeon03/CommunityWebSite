package com.community.backend.repository;

import com.community.backend.entity.Post.BoardType;
import com.community.backend.entity.Post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //특정 게시판타입에 해당하는 게시글들을 updateat->createdat 순으로 내림차순 정렬하여 추출
    List<Post> findByBoardTypeOrderByUpdatedAtDescCreatedAtDesc(BoardType boardType);

    //특정 게시판타입에 게시글중에서 최신 등록된 5개만 추출
    List<Post> findTop5ByBoardTypeOrderByCreatedAtDesc(BoardType boardType);

    //boardType == type1 AND title에 검색어가 포함됨 (대소문자 무시) or boardType == type2 AND content에 검색어가 포함됨 (대소문자 무시)
    List<Post> findByBoardTypeAndTitleContainingIgnoreCaseOrBoardTypeAndContentContainingIgnoreCase(
            BoardType type1, String title,
            BoardType type2, String content
    );

    List<Post> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}