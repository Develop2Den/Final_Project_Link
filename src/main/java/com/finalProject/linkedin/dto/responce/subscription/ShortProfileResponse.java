package com.finalProject.linkedin.dto.responce.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortProfileResponse {
    Integer userId;
    String name ;
    String surname ;
    String headerPhotoUrl;



}
