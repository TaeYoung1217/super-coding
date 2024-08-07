package com.github.supercoding.web.controller;

import com.github.supercoding.repository.items.ElectronicStoreItemJpaRepository;
import com.github.supercoding.service.ElectronicStoreItemService;
import com.github.supercoding.web.dto.items.BuyOrder;
import com.github.supercoding.web.dto.items.Item;
import com.github.supercoding.web.dto.items.ItemBody;
import com.github.supercoding.web.dto.items.StoreInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor //Bean 주입을 위한 어노테이션, final, notNull이 붙은 필드의 생성자를 자동 생성
@Slf4j
@Tag(name ="ElectronicStoreController", description = "전자상가 물품 구매")
@CrossOrigin
public class ElectronicStoreController {
    private final ElectronicStoreItemService electronicStoreItemService;
    private final ElectronicStoreItemJpaRepository electronicStoreItemJpaRepository;

    @Operation(
            summary = "findAllItem",
            description = "모든 아이템을 검색합니다"
    )
    @ApiResponse(
            responseCode = "200",
            description = "모든 아이템을 검색했습니다"
    )
    @GetMapping("/items")
    public List<Item> findAllItem(){
        log.info("findAllItem");
        List<Item> items = electronicStoreItemService.findAllItem();
        log.info(items.toString());
        return electronicStoreItemService.findAllItem();
    }

    @Operation(
            summary = "아이템 등록"
    )
    @ApiResponse(
            responseCode = "200", description = "success", content = @Content(schema = @Schema(example = " \"ID : 1 has been registered\""))
    )
    @PostMapping("/items") //오류 수정 필요 -> UnsupportedOperationException 에러 발생 -> Arrays.asList()로 리스트를 만들면 추가, 수정, 삭제가 불가함.
                                // new ArrayList<>(Arrays.asList())로 해결함
    public String registerItem(@RequestBody ItemBody itemBody){
        Integer itemId = electronicStoreItemService.registerItem(itemBody);
        return "ID : "+ itemId + " has been registered";
    }

    @Operation(
            summary = "아이디로 아이템 검색"
    )
    @GetMapping("/items/{id}")
    public Item findItemById(
            @Parameter(description = "아이템의 아이디", required = true, example = "1")
            @PathVariable String id){
        return electronicStoreItemService.findItemById(id);
    }

    @Operation(
            summary = "아이디 쿼리로 아이템 검색"
    )
    @GetMapping("/items-prices")
    public List<Item> findItemByPrice(@RequestParam("max") Integer maxValue){
        return electronicStoreItemService.findItemsOrderByPrices(maxValue);
    }

    @Operation(
            summary = "아이디 쿼리로 아이템 검색"
    )
    @GetMapping("/items-query")
    public Item findItemByQueryId(@RequestParam("id") String id){
        return electronicStoreItemService.findItemById(id);
    }

    @Operation(
            summary = "여러 아이템 검색"
    )
    @GetMapping("/items-queries")
    public List<Item> getItemByQueryId(@RequestParam("id") List<String> ids) {
        log.info("getItemByQueryId");
        List<Item> items = electronicStoreItemService.findItemByQuery(ids);
        log.info(items.toString());
        return items;
    }

    @Operation(
            summary = "여러 아이템 타입 검색"
    )
    @GetMapping("/items-types")
    public List<Item> getItemByType(@RequestParam("type") List<String> types) {
        log.info("getItemByTypes");
        List<Item> items = electronicStoreItemService.findItemByTypes(types);
        log.info(items.toString());
        return items;
    }



    @Operation(
            summary = "아이템 삭제"
    )
    @DeleteMapping("/items/{id}")
    public String deleteItemByPathId(@PathVariable String id){
        electronicStoreItemService.deleteItemById(id);
        return "Object with id="+id+" has been deleted";
    }

    @Operation(
            summary = "아이템 수정"
    )
    @PutMapping("/items/{id}")
    public Item updateItem (
            @Parameter(description = "수정할 아이템의 아이디", required = true, example ="1")
            @PathVariable String id,@RequestBody ItemBody itemBody){
        //SQL UPDATE 사용
        return electronicStoreItemService.updateItem(id,itemBody);
    }

    @Operation(
            summary = "아이템 구입"
    )
    @PostMapping("/items/buy")
    public String buyItem(
            @Parameter(description = "아이템 구입", example = "111")
            @RequestBody BuyOrder buyOrder){
        Integer OrderItemNums = electronicStoreItemService.buyItems(buyOrder);
        return "요청하신 Item 중 " + OrderItemNums +"개를 구매하였습니다.";
    }

    @Operation(summary = "pagination 지원")
    @GetMapping("/items-pages")
    public Page<Item> findItemsPagination(Pageable pageable){
        return electronicStoreItemService.findAllWithPageable(pageable);
    }

    @Operation(summary = "pagination 지원2")
    @GetMapping("/items-types-pages")
    public Page<Item> findItemsPagination(@RequestParam("type") List<String> types, Pageable pageable){
        return electronicStoreItemService.findAllWithPageable(types,pageable);
    }

    @Operation(summary = "전체 stores 정보 검색")
    @GetMapping("/stores")
    public List<StoreInfo> findAllStoreInfo(){
        return electronicStoreItemService.findAllStoreInfo();
    }
}
