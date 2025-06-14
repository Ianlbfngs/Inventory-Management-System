package com.ib.stockservice.response;

public class Statuses{
    public enum CreateStockStatus implements IStatus{SUCCESS,STORAGE_NOT_FOUND,BATCH_NOT_FOUND,NEGATIVE_AVAILABLE_STOCK,NEGATIVE_PENDING_STOCK,STOCK_ALREADY_EXISTS}
    public enum UpdateStockStatus implements IStatus{SUCCESS,STOCK_NOT_FOUND,NEGATIVE_AVAILABLE_STOCK,NEGATIVE_PENDING_STOCK,NOTHING_TO_UPDATE,NEGATIVE_STOCK,NOT_ENOUGH_STOCK}
    public enum SoftDeleteStockStatus implements IStatus{SUCCESS,STOCK_NOT_EMPTY,NOT_FOUND,ALREADY_SOFT_DELETED}
    public enum HardDeleteStockStatus implements IStatus{SUCCESS,STOCK_NOT_EMPTY,NOT_FOUND}

}
