package com.cmc.monggeul.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserMatchingCodeRes {
    String matchingCode;
    Long familyId;
}
