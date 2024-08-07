package com.github.supercoding.web.dto.items;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Spec {
    @Schema(description = "The cpu of item", example = "Intel i5") private String cpu;
    @Schema(description = "The capacity of item", example = "256GB") private String capacity;
}
