package com.finalProject.linkedin.repository;

import com.finalProject.linkedin.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * description
 *
 * @author Alexander Isai on 10.10.2024.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {

    Optional<Profile> findByUserIdAndDeletedAtIsNull(Long id);


}
