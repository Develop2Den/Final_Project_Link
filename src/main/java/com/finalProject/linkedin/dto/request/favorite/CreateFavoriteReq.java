package com.finalProject.linkedin.dto.request.favorite;

import com.finalProject.linkedin.utils.enums.TargetType;
import lombok.Data;

@Data
public class CreateFavoriteReq {
    private Long userId;
    private Long targetId;
    private TargetType targetType;
    private Boolean isPositive;
}
