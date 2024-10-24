package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.comment.CreateCommentReq;
import com.finalProject.linkedin.dto.responce.comment.CreateCommentRes;
import com.finalProject.linkedin.entity.Comment;
import com.finalProject.linkedin.repository.CommentRepository;
import com.finalProject.linkedin.service.serviceIR.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final ModelMapper modelMapper;
    private final CommentRepository commentRepository;

    @Override
    public CreateCommentRes create(CreateCommentReq createCommentReq) {
        Comment comment = modelMapper.map(createCommentReq, Comment.class);
        if (getIdFromEntity(comment) == 0L) {
            Comment savedComment = commentRepository.save(comment);
            return modelMapper.map(savedComment, CreateCommentRes.class);
        }
        throw new RuntimeException("Chat already exist");
    }

    @Override
    public boolean deleteById(Long id) {
        if (commentRepository.existsById(id)) {
            Comment comment = getOne(id);
            comment.setDeletedAt(LocalDateTime.now());
            commentRepository.save(comment);
            return true;
        }
        return false;
    }

    @Override
    public Comment getOne(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<CreateCommentRes> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable).map(comment -> modelMapper.map(comment, CreateCommentRes.class)).toList();
    }



    public long getIdFromEntity(Comment comment) {
        Optional<Comment> chat1 = commentRepository.findAll().stream().filter(c -> c.equals(comment)).findFirst();
        return chat1.map(Comment::getCommentId).orElse(0L);
    }

}
