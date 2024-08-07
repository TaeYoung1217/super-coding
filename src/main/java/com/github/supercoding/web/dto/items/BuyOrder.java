package com.github.supercoding.web.dto.items;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BuyOrder {
    @Schema(description = "The id of item", example = "5") private Integer itemId;
    @Schema(description = "The amount of item", example = "1") private Integer itemNums;
}
