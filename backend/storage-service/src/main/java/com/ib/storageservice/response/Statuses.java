package com.ib.storageservice.response;

import com.ib.storageservice.response.IStatus;

public class Statuses{

    public enum CreateStorageStatus implements IStatus {SUCCESS,NEGATIVE_CAPACITY, NAME_IN_USE}
    public enum UpdateStorageStatus implements IStatus{SUCCESS,SOFT_DELETED,NOT_FOUND,NEGATIVE_CAPACITY,NAME_IN_USE}
    public enum SoftDeleteStorageStatus implements IStatus{SUCCESS,ALREADY_SOFT_DELETED,NOT_FOUND}
    public enum HardDeleteStorageStatus implements IStatus{SUCCESS,NOT_FOUND}

}
