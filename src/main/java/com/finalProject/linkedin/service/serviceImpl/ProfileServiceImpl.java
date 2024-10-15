package com.finalProject.linkedin.service.serviceImpl;

import com.finalProject.linkedin.dto.request.CreateProfileReq;
import com.finalProject.linkedin.dto.responce.CreateProfileResp;
import com.finalProject.linkedin.service.serviceIR.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * description
 *
 * @author Alexander Isai on 10.10.2024.
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    @Override
    public CreateProfileResp createProfile(CreateProfileReq createProfileReq) {
        return null;
    }
}
