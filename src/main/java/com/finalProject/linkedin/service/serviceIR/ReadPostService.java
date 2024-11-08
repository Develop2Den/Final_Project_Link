package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.entity.ReadPost;
import org.springframework.stereotype.Service;

public interface ReadPostService {

    void saveReadPost(Long postId , Long userId);
}
