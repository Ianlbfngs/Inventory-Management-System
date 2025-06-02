package com.ib.productservice.service.batch;

import com.ib.productservice.entity.Batch;
import com.ib.productservice.repository.BatchRepository;
import com.ib.productservice.response.Response;
import com.ib.productservice.response.Statuses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatchService implements IBatchService{

    private final BatchRepository batchRepository;

    @Autowired
    public BatchService(BatchRepository batchRepository){
        this.batchRepository = batchRepository;
    }

    @Override
    public List<Batch> obtainAll() {
        return batchRepository.findAll();
    }


    @Override
    public Optional<Batch> obtainSpecificBatchWithId(int id) {
        return batchRepository.findById(id);
    }

    @Override
    public Optional<Batch> obtainSpecificBatchWithCode(String code) {
        return batchRepository.findBatchByBatchCode(code);
    }

    @Override
    public Response<Statuses.CreateBatchStatus, Batch> createBatch(Batch batch) {
        if(batch.getAmount() <=0) return new Response<>(Statuses.CreateBatchStatus.NEGATIVE_AMOUNT,null);
        if(batchRepository.existsBatchByBatchCode(batch.getBatchCode())) return new Response<>(Statuses.CreateBatchStatus.CODE_IN_USE,null);
        return new Response<>(Statuses.CreateBatchStatus.SUCCESS,batchRepository.save(batch));
    }

    @Override
    public Response<Statuses.UpdateBatchStatus, Batch> updateBatch(int id, Batch batch) {
        if(batch.getAmount() <=0) return new Response<>(Statuses.UpdateBatchStatus.NEGATIVE_AMOUNT,null);
        batch.setId(id);
        Optional<Batch> originalBatch = batchRepository.findById(id);
        if(originalBatch.isEmpty()) return new Response<>(Statuses.UpdateBatchStatus.NOT_FOUND,null);
        if(!originalBatch.get().getBatchCode().equals(batch.getBatchCode()) && batchRepository.existsBatchByBatchCode(batch.getBatchCode())) return new Response<>(Statuses.UpdateBatchStatus.CODE_IN_USE,null);
        return new Response<>(Statuses.UpdateBatchStatus.SUCCESS,batchRepository.save(batch));
    }


    @Override
    public Response<Statuses.HardDeleteBatchStatus, Batch> hardDeleteBatch(int id) {
        if(!batchRepository.existsBatchById(id)) return new Response<>(Statuses.HardDeleteBatchStatus.NOT_FOUND,null);
        batchRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteBatchStatus.SUCCESS,null);
    }

}
