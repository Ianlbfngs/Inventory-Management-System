package com.ib.stockservice.repository;

import com.ib.stockservice.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock,Integer> {
    boolean existsStockById(int id);

    boolean existsStockByBatchCodeAndStorageId(String batchCode, int storageId);

    Optional<Stock> getStockById(int id);

    Optional<Stock> getStockByStorageIdAndBatchCode(int storageId, String batchCode);
}
