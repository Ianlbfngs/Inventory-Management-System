package com.ib.stockservice.response;

public class Statuses{
    public enum CreateStockStatus implements IStatus{SUCCESS,STORAGE_NOT_FOUND,BATCH_NOT_FOUND,NEGATIVE_AVAILABLE_STOCK,NEGATIVE_PENDING_STOCK,STOCK_ALREADY_EXISTS}
    public enum UpdateStockStatus implements IStatus{SUCCESS,STOCK_NOT_FOUND,NEGATIVE_AVAILABLE_STOCK,NEGATIVE_PENDING_STOCK,NOTHING_TO_UPDATE,NEGATIVE_STOCK,NOT_ENOUGH_STOCK}
    public enum HardDeleteStockStatus implements IStatus{SUCCESS,NOT_FOUND}

}
