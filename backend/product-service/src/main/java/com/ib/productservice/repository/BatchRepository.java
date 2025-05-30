package com.ib.productservice.repository;

import com.ib.productservice.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {
    boolean findBatchByBatchCode(String batchCode);

    boolean existsBatchByBatchCode(String batchCode);

    boolean existsBatchById(int id);
}
