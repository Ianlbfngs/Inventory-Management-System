package com.ib.movementsservice.response;

import com.ib.movementsservice.response.IStatus;

public class Statuses{
    //Movement response status
    public enum CreateMovementStatus implements IStatus {SUCCESS,SAME_STOCK,INVALID_STOCK_AMOUNT,NOT_ENOUGH_STOCK,TARGET_STOCK_NOT_FOUND,ORIGIN_STOCK_NOT_FOUND,ERROR_UPDATING_STOCK}
    public enum MovementReceptionStatus implements IStatus{SUCCESS,MOVEMENT_NOT_FOUND,TARGET_STOCK_NOT_FOUND,ORIGIN_STOCK_NOT_FOUND,ERROR_UPDATING_STOCK}
    public enum HardDeleteMovementStatus implements IStatus{SUCCESS,NOT_FOUND}

    //Movement type response status
    public enum UpdateMovementTypeStatus implements IStatus{SUCCESS,NOT_FOUND}
    public enum HardDeleteMovementTypeStatus implements IStatus{SUCCESS,NOT_FOUND}
}
