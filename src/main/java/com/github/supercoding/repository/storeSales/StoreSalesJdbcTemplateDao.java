package com.github.supercoding.repository.storeSales;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class StoreSalesJdbcTemplateDao implements StoreSalesRepository{
    private final JdbcTemplate jdbcTemplate;
    static RowMapper<StoreSales> storeSalesRowMapper = ((rs, rowNum) ->
        new StoreSales.StoreSalesBuilder()
                .id(rs.getInt("id"))
                .storeName(rs.getNString("store_name"))
                .amount(rs.getInt("amount"))
                .build()
    );

    public StoreSalesJdbcTemplateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public StoreSales findStoreSalesById(Integer storeId) {
        return jdbcTemplate.queryForObject("SELECT * FROM store_sales WHERE id = ?",
                storeSalesRowMapper,
                storeId
        );
    }

    @Override
    public void updateSalesAmount(Integer storeId, Integer salesAmount) {
        jdbcTemplate.update("UPDATE store_sales SET amount = ? WHERE id = ?",
                salesAmount,
                storeId
        );
    }
}
