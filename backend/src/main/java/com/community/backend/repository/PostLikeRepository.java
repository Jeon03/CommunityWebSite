package com.community.backend.repository;


import com.community.backend.entity.Post.Post;
import com.community.backend.entity.Post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUserId(Post post, Long userId);

    List<PostLike> findByUserId(Long userId);
    void deleteByUserId(Long userId);

}