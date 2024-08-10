package com.github.supercoding.service;

import com.github.supercoding.repository.items.ElectronicStoreItemJpaRepository;
import com.github.supercoding.repository.items.ItemEntity;
import com.github.supercoding.repository.storeSales.StoreSales;
import com.github.supercoding.repository.storeSales.StoreSalesJpaRepository;
import com.github.supercoding.repository.storeSales.StoreSalesRepository;
import com.github.supercoding.service.exceptions.NotFoundException;
import com.github.supercoding.service.mapper.ItemMapper;
import com.github.supercoding.web.dto.items.BuyOrder;
import com.github.supercoding.web.dto.items.Item;
import com.github.supercoding.web.dto.items.ItemBody;
import com.github.supercoding.web.dto.items.StoreInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElectronicStoreItemService {
    private final ElectronicStoreItemJpaRepository electronicStoreItemJpaRepository;
    //private final StoreSalesRepository storeSalesRepository;
    private final StoreSalesJpaRepository storeSalesJpaRepository;

    @Cacheable(value = "items",key = "#root.methodName")
    public List<Item> findAllItem() {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll();
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
        //24.08.02 : ItemMapper에서 매핑 할때 처음에 오류 났었음, itemEntities까지는 잘 받아오는데
        //return 할때 보니까 null 모든 필드에 null값이 들어갔었음
        //원인 : Item 클래스에 getter만 있고 setter가 없었음
        //해결 : Item 클래스에 lombok이용 setter 추가
    }

    @CacheEvict(value = "items",allEntries = true) // 메소드 실행 시 저장된 캐시 삭제하기
    public Integer registerItem(ItemBody itemBody) {
        ItemEntity itemEntity = ItemMapper.INSTANCE.idAndItemBodyToItemEntity(null, itemBody);
        ItemEntity itemEntityCreated;
        try {
            itemEntityCreated = electronicStoreItemJpaRepository.save(itemEntity);
        }catch (RuntimeException e) {
            throw new NotFoundException("item을 저장하는 도중 error가 발생했습니다.");
        }
        return itemEntityCreated.getId();
    }

    @Cacheable(value = "items", key = "#id")
    public Item findItemById(String id) {
        Integer idInt = Integer.parseInt(id);
        ItemEntity itemEntity = electronicStoreItemJpaRepository
                .findById(idInt)
                .orElseThrow(()->new NotFoundException("해당 ID : "+idInt+"의 아이템을 찾을 수 없습니다."));
        return ItemMapper.INSTANCE.itemEntityToItem(itemEntity);
    }

    @Cacheable(value = "items", key = "#ids")
    public List<Item> findItemByQuery(List<String> ids) {
        List<ItemEntity> itemsFound = electronicStoreItemJpaRepository.findAll();
        if(itemsFound.isEmpty()) throw new NotFoundException("아이템을 찾을 수 없습니다");
        return itemsFound
                .stream()
                .map(ItemMapper.INSTANCE::itemEntityToItem)
                .filter((item->ids.contains(item.getId())))
                .collect(Collectors.toList());
    }

    @CacheEvict(value = "items",allEntries = true) // 메소드 실행 시 저장된 캐시 삭제하기
    public void deleteItemById(String id) {
        Integer idInt = Integer.parseInt(id);
        electronicStoreItemJpaRepository.deleteById(idInt);
    }

    @CacheEvict(value = "items",allEntries = true) // 메소드 실행 시 저장된 캐시 삭제하기
    @Transactional(transactionManager = "tmJpa1")
    public Item updateItem(String id, ItemBody itemBody) {
        Integer idInt = Integer.parseInt(id);
        ItemEntity itemEntityUpdated = electronicStoreItemJpaRepository.findById(idInt)
                .orElseThrow(()->new NotFoundException("해당 ID : "+idInt+"의 아이템을 찾을 수 없습니다."));
        itemEntityUpdated.setItemBody(itemBody);

        return ItemMapper.INSTANCE.itemEntityToItem(itemEntityUpdated);
    }

    @CacheEvict(value = "items",allEntries = true) // 메소드 실행 시 저장된 캐시 삭제하기
    @Transactional(transactionManager = "tmJpa1") //트랜잭션의 원자성 확보
    public Integer buyItems(BuyOrder buyOrder) {
        Integer itemId = buyOrder.getItemId();
        Integer itemNums = buyOrder.getItemNums();

        ItemEntity itemEntity = electronicStoreItemJpaRepository.findById(itemId).orElseThrow(()->new NotFoundException("해당 아이템 "+itemId+"을 찾을 수 없습니다"));
        if(itemEntity.getStoreSales() == null) throw new RuntimeException("매장을 찾을 수 없습니다");
        if(itemEntity.getStock() <=0) throw new RuntimeException("재고가 없습니다");

        Integer successBuyItemNums;
        if(itemNums>=itemEntity.getStock())
            successBuyItemNums = itemEntity.getStock();
        else successBuyItemNums = itemNums;

        Integer totalPrice = successBuyItemNums * itemEntity.getPrice();
        itemEntity.setStock(itemEntity.getStock() - successBuyItemNums);

        StoreSales storeSales = itemEntity.getStoreSales().
                orElseThrow(()->new NotFoundException("요청하신 Store에 해당하는 StoreSale이 없습니다."));
        storeSales.setAmount(storeSales.getAmount()+totalPrice); //매상 추가
        return successBuyItemNums;
    }

    public List<Item> findItemByTypes(List<String> types) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findItemEntitiesByTypeIn(types);
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    public List<Item> findItemsOrderByPrices(Integer maxValue) {
        List<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findItemEntitiesByPriceLessThanEqualOrderByPriceAsc(maxValue);
        return itemEntities.stream().map(ItemMapper.INSTANCE::itemEntityToItem).collect(Collectors.toList());
    }

    public Page<Item> findAllWithPageable(Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAll(pageable);
        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);
    }

    public Page<Item> findAllWithPageable(List<String> types, Pageable pageable) {
        Page<ItemEntity> itemEntities = electronicStoreItemJpaRepository.findAllByTypeIn(types,pageable);
        return itemEntities.map(ItemMapper.INSTANCE::itemEntityToItem);
    }


    @Transactional(transactionManager = "tmJpa1")
    public List<StoreInfo> findAllStoreInfo() {
        //List<StoreSales> storeSales = storeSalesJpaRepository.findAll(); //N+1 문제 발생
        List<StoreSales> storeSales = storeSalesJpaRepository.findAllFetchJoin(); //JOIN 으로 N+1문제 해결
        return storeSales.stream().map(StoreInfo::new).collect(Collectors.toList());
    }
}
