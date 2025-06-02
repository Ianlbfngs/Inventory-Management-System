package com.ib.stockservice.service;

import com.ib.stockservice.entity.Stock;
import com.ib.stockservice.repository.StockRepository;
import com.ib.stockservice.response.Response;
import com.ib.stockservice.response.Statuses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class StockService implements IStockService{
    private static final Logger logger = LoggerFactory.getLogger(StockService.class);

    private final RestTemplate restTemplate;
    private final StockRepository stockRepository;

    @Autowired
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

    private Response<Statuses.CreateAndUpdateStockStatus> stockSaveFunction(Stock stock, String jwtToken){
        if(stockRepository.existsStockByBatchCodeAndStorageId(stock.getBatchCode(),stock.getStorageId())) return new Response<>(Statuses.CreateAndUpdateStockStatus.STOCK_ALREADY_EXISTS,null);
        if(stock.getQuantity() <=0) return new Response<>(Statuses.CreateAndUpdateStockStatus.NEGATIVE_QUANTITY,null);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try{
            ResponseEntity<Void> responseBatch = restTemplate.exchange("http://localhost:8080/api/batches/code/" + stock.getBatchCode(), HttpMethod.GET,entity, Void.class);
            try{
                ResponseEntity<Void> responseStorage = restTemplate.exchange("http://localhost:8080/api/storage/" + stock.getStorageId(), HttpMethod.GET,entity, Void.class);

                return new Response<>(Statuses.CreateAndUpdateStockStatus.SUCCESS,stockRepository.save(stock));
            }catch(HttpClientErrorException.NotFound e){

                logger.error("Error requesting the batch: {}",e.getMessage(),e);
                return new Response<>(Statuses.CreateAndUpdateStockStatus.BATCH_NOT_FOUND,null);
            }
        }catch(HttpClientErrorException.NotFound e){
            logger.error("Error requesting the storage: {}",e.getMessage(),e);
            return new Response<>(Statuses.CreateAndUpdateStockStatus.STORAGE_NOT_FOUND,null);
        }
    }

    @Override
    public Response<Statuses.CreateAndUpdateStockStatus> createStock(Stock stock, String jwtToken) {
        return stockSaveFunction(stock,jwtToken);
    }

    @Override
    public Response<Statuses.CreateAndUpdateStockStatus> updateStock(int id, Stock stock, String jwtToken) {
        if(!stockRepository.existsStockById(id)) return new Response<>(Statuses.CreateAndUpdateStockStatus.STOCK_NOT_FOUND,null);
        stock.setId(id);
        return stockSaveFunction(stock,jwtToken);

    }

    @Override
    public Response<Statuses.HardDeleteStockStatus> hardDeleteStock(int id) {
        if(!stockRepository.existsStockById(id)) return new Response<>(Statuses.HardDeleteStockStatus.NOT_FOUND,null);
        stockRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteStockStatus.SUCCESS,null);
    }


}
