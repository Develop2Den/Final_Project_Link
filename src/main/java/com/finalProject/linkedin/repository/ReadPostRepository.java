package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.ReadPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadPostRepository extends JpaRepository<ReadPost, Long> {

}
