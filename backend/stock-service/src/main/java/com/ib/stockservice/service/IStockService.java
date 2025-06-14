package com.ib.stockservice.service;

import com.ib.stockservice.entity.Stock;
import com.ib.stockservice.response.Response;
import com.ib.stockservice.response.Statuses;

import java.util.List;
import java.util.Optional;

public interface IStockService {
    List<Stock> obtainAll();

    Response<Statuses.CreateStockStatus> createStock(Stock stock, String jwtToken);

    Optional<Stock> obtainSpecificStock(int id);

    Response<Statuses.UpdateStockStatus> updateStock(int id,Stock stock );


    Response<Statuses.HardDeleteStockStatus> hardDeleteStock(int id);

    Response<Statuses.SoftDeleteStockStatus> softDeleteStock(int id);
}
