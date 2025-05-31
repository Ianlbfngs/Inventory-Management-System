package com.ib.stockservice.service;

import com.ib.stockservice.entity.Stock;
import com.ib.stockservice.repository.StockRepository;
import com.ib.stockservice.response.Response;
import com.ib.stockservice.response.Statuses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class StockService implements IStockService{
    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    private final RestTemplate restTemplate;
    private final StockRepository stockRepository;

    public StockService(RestTemplate restTemplate, StockRepository stockRepository){
        this.restTemplate = restTemplate;
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> obtainAll() {
        return stockRepository.findAll();
    }

    @Override
    public Optional<Stock> obtainSpecificStock(int id) {
        return stockRepository.findById(id);
    }

    @Override
    public Response<Statuses.CreateStockStatus> createStock(Stock stock) {
        try{
            ResponseEntity<Void> responseBatch = restTemplate.getForEntity("http://localhost:8080/api/batches/" + stock.getBatchCode(), Void.class);
            if(responseBatch.getStatusCode().is2xxSuccessful()) return new Response<>(Statuses.CreateStockStatus.BATCH_NOT_FOUND,null);
            try{
                ResponseEntity<Void> responseStorage = restTemplate.getForEntity("http://localhost:8080/api/storage/" + stock.getStorageId(), Void.class);
                if(responseStorage.getStatusCode().is2xxSuccessful()) return new Response<>(Statuses.CreateStockStatus.STORAGE_NOT_FOUND,null);

                return new Response<>(Statuses.CreateStockStatus.SUCCESS,stockRepository.save(stock));


            }catch(Exception e){
                logger.error("Error requesting the batch: {}",e.getMessage(),e);
                throw e;
            }
        }catch(Exception e){
            logger.error("Error requesting the storage: {}",e.getMessage(),e);
           throw e;
        }
    }

    @Override
    public Response<Statuses.UpdateStockStatus> updateStock(int id, Stock stock) {
        if(!stockRepository.existsStockById(id)) return new Response<>(Statuses.UpdateStockStatus.STOCK_NOT_FOUND,null);
        stock.setId(id);
        try{
            ResponseEntity<Void> responseBatch = restTemplate.getForEntity("http://localhost:8080/api/batches/" + stock.getBatchCode(), Void.class);
            if(responseBatch.getStatusCode().is2xxSuccessful()) return new Response<>(Statuses.UpdateStockStatus.BATCH_NOT_FOUND,null);
            try{
                ResponseEntity<Void> responseStorage = restTemplate.getForEntity("http://localhost:8080/api/storage/" + stock.getStorageId(), Void.class);
                if(responseStorage.getStatusCode().is2xxSuccessful()) return new Response<>(Statuses.UpdateStockStatus.STORAGE_NOT_FOUND,null);

                return new Response<>(Statuses.UpdateStockStatus.SUCCESS,stockRepository.save(stock));
            }catch(Exception e){
                logger.error("Error requesting the batch: {}",e.getMessage(),e);
                throw e;
            }
        }catch(Exception e){
            logger.error("Error requesting the storage: {}",e.getMessage(),e);
            throw e;
        }
    }

    @Override
    public Response<Statuses.HardDeleteStockStatus> hardDeleteStock(int id) {
        if(!stockRepository.existsStockById(id)) return new Response<>(Statuses.HardDeleteStockStatus.NOT_FOUND,null);
        stockRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteStockStatus.SUCCESS,null);
    }


}
