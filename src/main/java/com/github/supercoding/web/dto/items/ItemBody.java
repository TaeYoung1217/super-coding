package com.github.supercoding.web.dto.items;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemBody {
    @Schema(description = "The name of item", example = "샘플") private String name;
    @Schema(description = "The type of item", example = "스마트폰") private String type;
    @Schema(description = "The price of item", example = "1000") private Integer price;
    private Spec spec;
}
