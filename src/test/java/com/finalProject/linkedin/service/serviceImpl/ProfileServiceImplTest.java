package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.profile.CreateProfileReq;
import com.finalProject.linkedin.dto.responce.profile.CreateProfileResp;
import com.finalProject.linkedin.entity.Profile;
import com.finalProject.linkedin.exception.NotFoundException;
import com.finalProject.linkedin.mapper.ProfileMapper;
import com.finalProject.linkedin.repository.ProfileRepository;
import com.finalProject.linkedin.service.serviceImpl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProfile_shouldReturnCreatedProfile() {
        CreateProfileReq createProfileReq = new CreateProfileReq(1L, "John", "Doe", LocalDateTime.of(1990, 1, 1, 0, 0), "Active", "http://example.com/photo.jpg", "Developer", "123 Main St");
        Profile profile = new Profile();
        Profile savedProfile = new Profile();
        CreateProfileResp createProfileResp = new CreateProfileResp(1L, 1L, "John", "Doe", LocalDateTime.of(1990, 1, 1, 0, 0), "Active", "http://example.com/photo.jpg", "Developer", "123 Main St", LocalDateTime.now());

        when(profileMapper.toProfile(createProfileReq)).thenReturn(profile);
        when(profileRepository.save(profile)).thenReturn(savedProfile);
        when(profileMapper.toCreateProfileResp(savedProfile)).thenReturn(createProfileResp);

        CreateProfileResp result = profileService.createProfile(createProfileReq);

        assertNotNull(result);
        assertEquals("John", result.name());
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    void getProfileById_shouldReturnProfile() {
        Profile profile = new Profile();
        profile.setProfileId(1L);
        profile.setName("John");
        CreateProfileResp createProfileResp = new CreateProfileResp(1L, 1L, "John", "Doe", LocalDateTime.of(1990, 1, 1, 0, 0), "Active", "http://example.com/photo.jpg", "Developer", "123 Main St", LocalDateTime.now());

        when(profileRepository.findByProfileIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(profile));
        when(profileMapper.toCreateProfileResp(profile)).thenReturn(createProfileResp);

        CreateProfileResp result = profileService.getProfileById(1L);

        assertNotNull(result);
        assertEquals("John", result.name());
        verify(profileRepository, times(1)).findByProfileIdAndDeletedAtIsNull(1L);
    }

    @Test
    void getProfileById_shouldThrowNotFoundException() {
        when(profileRepository.findByProfileIdAndDeletedAtIsNull(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> profileService.getProfileById(1L));
        verify(profileRepository, times(1)).findByProfileIdAndDeletedAtIsNull(1L);
    }

    @Test
    void updateProfile_shouldReturnUpdatedProfile() {
        CreateProfileReq createProfileReq = new CreateProfileReq(1L, "John", "Smith", LocalDateTime.of(1990, 1, 1, 0, 0), "Updated", "http://example.com/photo-updated.jpg", "Manager", "456 Another St");
        Profile existingProfile = new Profile();
        existingProfile.setProfileId(1L);
        Profile updatedProfile = new Profile();
        CreateProfileResp createProfileResp = new CreateProfileResp(1L, 1L, "John", "Smith", LocalDateTime.of(1990, 1, 1, 0, 0), "Updated", "http://example.com/photo-updated.jpg", "Manager", "456 Another St", LocalDateTime.now());

        when(profileRepository.findById(1L)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(existingProfile)).thenReturn(updatedProfile);
        when(profileMapper.toCreateProfileResp(updatedProfile)).thenReturn(createProfileResp);

        CreateProfileResp result = profileService.updateProfile(1L, createProfileReq);

        assertNotNull(result);
        assertEquals("Smith", result.surname());
        verify(profileRepository, times(1)).findById(1L);
        verify(profileRepository, times(1)).save(existingProfile);
    }

    @Test
    void deleteProfile_shouldSetDeletedAt() {
        Profile profile = new Profile();
        profile.setProfileId(1L);

        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        profileService.deleteProfile(1L);

        assertNotNull(profile.getDeletedAt());
        verify(profileRepository, times(1)).findById(1L);
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    void getAllProfiles_shouldReturnProfiles() {
        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        List<Profile> profiles = List.of(profile1, profile2);
        Page<Profile> profilePage = new PageImpl<>(profiles);

        when(profileRepository.findAll(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "profileId")))).thenReturn(profilePage);
        when(profileMapper.toCreateProfileResp(profile1)).thenReturn(new CreateProfileResp(1L, 1L, "John", "Doe", LocalDateTime.of(1990, 1, 1, 0, 0), "Active", "http://example.com/photo.jpg", "Developer", "123 Main St", LocalDateTime.now()));
        when(profileMapper.toCreateProfileResp(profile2)).thenReturn(new CreateProfileResp(2L, 2L, "Jane", "Smith", LocalDateTime.of(1991, 2, 2, 0, 0), "Active", "http://example.com/photo2.jpg", "Manager", "789 Main St", LocalDateTime.now()));

        List<CreateProfileResp> result = profileService.getAllProfiles(0, 2, null, null, null);

        assertEquals(2, result.size());
        verify(profileRepository, times(1)).findAll(PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "profileId")));
    }
}
