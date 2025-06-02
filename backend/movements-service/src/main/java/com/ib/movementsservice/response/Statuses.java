package com.ib.movementsservice.response;

import com.ib.movementsservice.response.IStatus;

public class Statuses{
    //Movement response status
    public enum CreateMovementStatus implements IStatus {SUCCESS,BATCH_NOT_FOUND,USER_NOT_FOUND,ORIGIN_STORAGE_NOT_FOUND,TARGET_STORAGE_NOT_FOUND,SAME_STORAGE,NEGATIVE_AMOUNT}
    public enum HardDeleteMovementStatus implements IStatus{SUCCESS,NOT_FOUND}

    //Movement type response status
    public enum UpdateMovementTypeStatus implements IStatus{SUCCESS,NOT_FOUND}
    public enum HardDeleteMovementTypeStatus implements IStatus{SUCCESS,NOT_FOUND}
}
