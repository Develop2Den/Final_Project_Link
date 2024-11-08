package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Post;
import com.finalProject.linkedin.entity.ReadPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadPostRepository extends JpaRepository<ReadPost, Long> {

}
