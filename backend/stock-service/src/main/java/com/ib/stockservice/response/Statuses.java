package com.ib.stockservice.response;

import com.ib.stockservice.response.IStatus;

public class Statuses{
    public enum CreateStockStatus implements IStatus {SUCCESS,STORAGE_NOT_FOUND,BATCH_NOT_FOUND}
    public enum UpdateStockStatus implements IStatus{SUCCESS,STORAGE_NOT_FOUND,BATCH_NOT_FOUND,STOCK_NOT_FOUND}
    public enum HardDeleteStockStatus implements IStatus{SUCCESS,NOT_FOUND}

}
