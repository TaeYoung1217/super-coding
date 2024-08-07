package com.github.supercoding.web.controller.sample;

import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.items.Item;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/sample")
@RequiredArgsConstructor
@Slf4j
public class SampleController {
    private final ElectronicStoreItemService electronicStoreItemService;

    @Operation(summary = "가격이 저렴한 순으로 정렬")
    @GetMapping("/items-prices")
    public List<Item> findItemsByPricing(HttpServletRequest httpServletRequest){
        Integer maxPrice = Integer.valueOf(httpServletRequest.getParameter("max"));
        return electronicStoreItemService.findItemsOrderByPrices(maxPrice);
    }

    @Operation(
            summary = "아이템 삭제"
    )
    @DeleteMapping("/items/{id}")
    public void deleteItemByPathId(
            @Parameter(name = "id",description = "item Id",example = "1") @PathVariable String id,
            HttpServletResponse httpServletResponse) throws IOException {
        electronicStoreItemService.deleteItemById(id);
        httpServletResponse.getOutputStream().println("Object with id="+id+" has been deleted");
    }
}
