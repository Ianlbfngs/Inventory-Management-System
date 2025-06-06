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

    @Override
    public Response<Statuses.CreateStockStatus> createStock(Stock stock, String jwtToken) {
        if(stockRepository.existsStockByBatchCodeAndStorageId(stock.getBatchCode(),stock.getStorageId())) return new Response<>(Statuses.CreateStockStatus.STOCK_ALREADY_EXISTS,null);
        if(stock.getPendingStock() ==null) stock.setPendingStock(0);
        if(stock.getAvailableStock() ==null) stock.setAvailableStock(0);

        if(stock.getAvailableStock() <0) return new Response<>(Statuses.CreateStockStatus.NEGATIVE_AVAILABLE_STOCK,null);
        if(stock.getPendingStock() <0)return new Response<>(Statuses.CreateStockStatus.NEGATIVE_PENDING_STOCK,null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try{
            ResponseEntity<Void> responseBatch = restTemplate.exchange("http://localhost:8080/api/batches/code/" + stock.getBatchCode(), HttpMethod.GET,entity, Void.class);
            try{
                ResponseEntity<Void> responseStorage = restTemplate.exchange("http://localhost:8080/api/storage/" + stock.getStorageId(), HttpMethod.GET,entity, Void.class);

                return new Response<>(Statuses.CreateStockStatus.SUCCESS,stockRepository.save(stock));
            }catch(HttpClientErrorException.NotFound e){

                logger.error("Error requesting the storage: {}",e.getMessage(),e);
                return new Response<>(Statuses.CreateStockStatus.STORAGE_NOT_FOUND,null);
            }
        }catch(HttpClientErrorException.NotFound e){
            logger.error("Error requesting the batch: {}",e.getMessage(),e);
            return new Response<>(Statuses.CreateStockStatus.BATCH_NOT_FOUND,null);
        }
    }

    @Override
    public Response<Statuses.UpdateStockStatus> updateStock(int id, Stock stock){
        if(stock.getAvailableStock() == null && stock.getPendingStock()==null) return new Response<>(Statuses.UpdateStockStatus.NOTHING_TO_UPDATE,null);

        Optional<Stock> originalStock = stockRepository.getStockById(id);
        if(originalStock.isEmpty()) return new Response<>(Statuses.UpdateStockStatus.STOCK_NOT_FOUND,null);

        if(stock.getAvailableStock() == null){
            if(stock.getPendingStock() <0) return new Response<>(Statuses.UpdateStockStatus.NEGATIVE_PENDING_STOCK,null);
            originalStock.get().setPendingStock(stock.getPendingStock());

        }else if(stock.getPendingStock() == null){
            if(stock.getAvailableStock()<0) return new Response<>(Statuses.UpdateStockStatus.NEGATIVE_AVAILABLE_STOCK,null);
            originalStock.get().setAvailableStock(stock.getAvailableStock());


        }else{
            if(stock.getPendingStock()<0 || stock.getAvailableStock() <0) return new Response<>(Statuses.UpdateStockStatus.NEGATIVE_STOCK,null);
            originalStock.get().setAvailableStock(stock.getAvailableStock());
            originalStock.get().setPendingStock(stock.getPendingStock());
        }

        return new Response<>(Statuses.UpdateStockStatus.SUCCESS,stockRepository.save(originalStock.get()));
    }

    /*
    @Override
    public Response<Statuses.UpdateStockStatus> updateStock(int storageId,String batchCode, int stockAmountRequested){
        Optional<Stock> stock = stockRepository.getStockByStorageIdAndBatchCode(storageId,batchCode);
        if(stock.isEmpty()) return new Response<>(Statuses.UpdateStockStatus.STOCK_NOT_FOUND,null);

        if(stock.get().getAvailableStock() - stockAmountRequested <0) return new Response<>(Statuses.UpdateStockStatus.NOT_ENOUGH_STOCK,null);

        stock.get().setAvailableStock(stock.get().getAvailableStock()-stockAmountRequested);
        stock.get().setPendingStock(stockAmountRequested);

        return new Response<>(Statuses.UpdateStockStatus.SUCCESS,stockRepository.save(stock.get()));
    }
*/

    @Override
    public Response<Statuses.HardDeleteStockStatus> hardDeleteStock(int id) {
        if(!stockRepository.existsStockById(id)) return new Response<>(Statuses.HardDeleteStockStatus.NOT_FOUND,null);
        stockRepository.deleteById(id);
        return new Response<>(Statuses.HardDeleteStockStatus.SUCCESS,null);
    }


}
