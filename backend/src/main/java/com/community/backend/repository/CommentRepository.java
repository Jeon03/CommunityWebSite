package com.community.backend.repository;

import com.community.backend.entity.Post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


//c.post.id  c는 Comment, post는 Post 객체, id는 Post의 필드
//Comment 엔티티들 중에서, post라는 필드로 연결된 Post 객체의 id가 postId인 모든 댓글(Comment) 을 가져온다.
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //댓글 엔티티 중에서 연관된 게시글 의 id가 postId인 것만 List<Comment> 형태로 조회
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);

    List<Comment> findByUserId(Long writerId);
    void deleteByUserId(Long userId);
}