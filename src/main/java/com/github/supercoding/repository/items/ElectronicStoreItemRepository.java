package com.github.supercoding.repository.items;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ElectronicStoreItemRepository {
    List<ItemEntity> findAllItems();

    Integer registerItem(ItemEntity itemEntity);

    ItemEntity updateItemEntity(Integer integer, ItemEntity itemEntity);

    Optional<ItemEntity> findItemById(String id);

    List<ItemEntity> findItemByQuery(Set<String> idSet);

    Integer deleteItemById(String id);

    void updateItemStock(Integer itemId, Integer stock);
}
