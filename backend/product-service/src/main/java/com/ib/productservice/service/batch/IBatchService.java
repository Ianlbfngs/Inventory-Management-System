package com.ib.productservice.service.batch;

import com.ib.productservice.entity.Batch;
import com.ib.productservice.response.Response;
import com.ib.productservice.response.Statuses;

import java.util.List;
import java.util.Optional;

public interface IBatchService {

    List<Batch> obtainAll();

    Response<Statuses.CreateBatchStatus, Batch> createBatch(Batch batch);

    Response<Statuses.UpdateBatchStatus, Batch> updateBatch(int id, Batch batch);

    Response<Statuses.HardDeleteBatchStatus, Batch> hardDeleteBatch(int id);

    Optional<Batch> obtainSpecificBatchWithId(int id);

    Optional<Batch> obtainSpecificBatchWithCode(String batch);

    Response<Statuses.SoftDeleteBatchStatus, Batch> softDeleteBatch(int id);
}
