package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.ReadPost.SaveReadPost;
import com.finalProject.linkedin.entity.ReadPost;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.repository.PostRepository;
import com.finalProject.linkedin.repository.ReadPostRepository;
import com.finalProject.linkedin.service.serviceIR.ReadPostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadPostServiceImpl implements ReadPostService {

    private final ReadPostRepository readPostRepository;
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;

    @Override
    public void saveReadPost(Long postId, Long userId) {

        if (!postRepository.existsById(postId)){
          throw new NotFoundException("Post not found with id " + postId);
        }
        SaveReadPost saveReadPost = new SaveReadPost(postId, userId);
        ReadPost readPost = modelMapper.map(saveReadPost, ReadPost.class);

        readPostRepository.save(readPost);
    }
}
