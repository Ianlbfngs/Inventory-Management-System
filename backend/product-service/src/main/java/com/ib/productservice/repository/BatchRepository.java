package com.ib.productservice.repository;

import com.ib.productservice.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {
    Optional<Batch> findBatchByBatchCode(String batchCode);

    boolean existsBatchByBatchCode(String batchCode);

    boolean existsBatchById(int id);

    Optional<Batch> findBatchByBatchCodeAndActive(String batchCode, boolean active);

    Optional<Batch> findByIdAndActive(int id, boolean active);

    List<Batch> findAllByActive(boolean active);

    boolean existsBatchByIdAndActive(int id, boolean active);
}
