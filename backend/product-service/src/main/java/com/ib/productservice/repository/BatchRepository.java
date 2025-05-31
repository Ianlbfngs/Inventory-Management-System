package com.ib.productservice.repository;

import com.ib.productservice.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {
    Optional<Batch> findBatchByBatchCode(String batchCode);

    boolean existsBatchByBatchCode(String batchCode);

    boolean existsBatchById(int id);
}
