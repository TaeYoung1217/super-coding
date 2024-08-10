package com.github.supercoding.repository.storeSales;

import org.springframework.stereotype.Repository;

@Repository
public interface StoreSalesRepository {
    StoreSales findStoreSalesById(Integer storeId);

    void updateSalesAmount(Integer storeId, Integer salesAmount);
}
