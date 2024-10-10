package com.Final_Project.Linkedin.service.serviceImpl;

import com.Final_Project.Linkedin.dto.request.CreateProfileReq;
import com.Final_Project.Linkedin.dto.responce.CreateProfileResp;
import com.Final_Project.Linkedin.service.serviceIR.ProfileService;
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
