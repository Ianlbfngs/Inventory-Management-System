package com.ib.storageservice.repository;

import com.ib.storageservice.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<Storage,Integer> {
    List<Storage> findAllByActive(boolean active);

    Optional<Storage> findByIdAndActive(int id, boolean active);

    boolean existsStorageById(int id);
}
