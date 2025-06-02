package com.ib.stockservice.response;

public class Statuses{
    public enum CreateAndUpdateStockStatus implements IStatus{SUCCESS,STORAGE_NOT_FOUND,BATCH_NOT_FOUND,STOCK_NOT_FOUND,STOCK_ALREADY_EXISTS,NEGATIVE_QUANTITY}
    public enum HardDeleteStockStatus implements IStatus{SUCCESS,NOT_FOUND}

}
