package com.ib.storageservice.service;

import com.ib.storageservice.entity.Storage;
import com.ib.storageservice.repository.StorageRepository;
import com.ib.storageservice.response.Response;
import com.ib.storageservice.response.Statuses;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StorageService implements IStorageService {

    private final StorageRepository storageRepository;

    public StorageService(StorageRepository storageRepository){
        this.storageRepository = storageRepository;
    }

    @Override
    public List<Storage> obtainAll() {
        return storageRepository.findAllByActive(true);
    }

    @Override
    public Optional<Storage> obtainSpecificStorage(int id) {
        return storageRepository.findByIdAndActive(id,true);
    }

    @Override
    public Response<Statuses.CreateStorageStatus> createStorage(Storage storage) {
        storage.setActive(true);
        return new Response<>(Statuses.CreateStorageStatus.SUCCESS,storageRepository.save(storage));
    }

    @Override
    public Response<Statuses.UpdateStorageStatus> updateStatus(int id, Storage storage) {
        storage.setId(id);
        Optional<Storage> originalStorage = storageRepository.findById(storage.getId());
        if(originalStorage.isEmpty()) return new Response<>(Statuses.UpdateStorageStatus.NOT_FOUND,null);
        if(!originalStorage.get().isActive()) return new Response<>(Statuses.UpdateStorageStatus.SOFT_DELETED,null);
        storage.setActive(true);
        return new Response<>(Statuses.UpdateStorageStatus.SUCCESS,storageRepository.save(storage));
    }

    @Override
    public Response<Statuses.SoftDeleteStorageStatus> softDeleteStorage(int id) {
        Optional<Storage> toSoftDeleteProduct = storageRepository.findById(id);
        if(toSoftDeleteProduct.isEmpty()) return new Response<>(Statuses.SoftDeleteStorageStatus.NOT_FOUND,null);
        if(!toSoftDeleteProduct.get().isActive()) return new Response<>(Statuses.SoftDeleteStorageStatus.ALREADY_SOFT_DELETED,null);
        toSoftDeleteProduct.get().setActive(false);
        return new Response<>(Statuses.SoftDeleteStorageStatus.SUCCESS,storageRepository.save(toSoftDeleteProduct.get()));
    }

    @Override
    public Response<Statuses.HardDeleteStorageStatus> hardDeleteStorage(int id) {
        if(!storageRepository.existsStorageById(id)) return new Response<>(Statuses.HardDeleteStorageStatus.NOT_FOUND,null);
        storageRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteStorageStatus.SUCCESS,null);
    }
}
