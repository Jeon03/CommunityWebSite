package com.community.backend.service.Post;

import com.community.backend.dto.Post.CommentRequest;
import com.community.backend.dto.Post.CommentResponse;
import com.community.backend.entity.Post.Comment;
import com.community.backend.entity.Post.Post;
import com.community.backend.repository.CommentRepository;
import com.community.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment createComment(CommentRequest request) {
        Comment comment = new Comment();
        comment.setUserId(request.getUserId());
        comment.setNickname(request.getNickname());
        comment.setContent(request.getContent());

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
        comment.setPost(post);

        if (request.getParentId() != null) {
            Comment parentComment = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글이 존재하지 않습니다."));
            comment.setParent(parentComment);
        }

        return commentRepository.save(comment);
    }

    //목록조회
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getNickname(),
                        comment.getUserId(),
                        comment.getCreatedAt(),
                        comment.getParent() != null ? comment.getParent().getId() : null
                ))
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}