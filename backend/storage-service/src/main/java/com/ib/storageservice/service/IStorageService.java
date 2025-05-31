package com.ib.storageservice.service;

import com.ib.storageservice.entity.Storage;
import com.ib.storageservice.response.Response;
import com.ib.storageservice.response.Statuses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface IStorageService {
    List<Storage> obtainAll();

    Optional<Storage> obtainSpecificStorage(int id);

    Response<Statuses.CreateStorageStatus> createStorage(Storage storage);

    Response<Statuses.UpdateStorageStatus> updateStatus(int id, Storage storage);

    Response<Statuses.SoftDeleteStorageStatus> softDeleteStorage(int id);

    Response<Statuses.HardDeleteStorageStatus> hardDeleteStorage(int id);
}
