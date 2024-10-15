package com.finalProject.linkedin.service.serviceIR;

import com.finalProject.linkedin.dto.request.CreateProfileReq;
import com.finalProject.linkedin.dto.responce.CreateProfileResp;

/**
 * description
 *
 * @author Alexander Isai on 10.10.2024.
 */
public interface ProfileService {
    CreateProfileResp createProfile(CreateProfileReq createProfileReq);
}
