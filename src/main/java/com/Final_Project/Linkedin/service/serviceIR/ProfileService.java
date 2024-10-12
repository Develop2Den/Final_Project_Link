package com.Final_Project.Linkedin.service.serviceIR;

import com.Final_Project.Linkedin.dto.request.CreateProfileReq;
import com.Final_Project.Linkedin.dto.responce.CreateProfileResp;

/**
 * description
 *
 * @author Alexander Isai on 10.10.2024.
 */
public interface ProfileService {
    CreateProfileResp createProfile(CreateProfileReq createProfileReq);
}
