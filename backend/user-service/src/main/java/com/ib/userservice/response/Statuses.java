package com.ib.userservice.response;

public class Statuses{
    public enum CreateStatus implements IStatus{SUCCESS,USER_IN_USE};
    public enum LoginStatus implements IStatus{OK, SOFT_DELETED,NOT_FOUND,DENIED};
    public enum UpdateStatus implements IStatus{SUCCESS, SOFT_DELETED,NOT_FOUND,USER_IN_USE};
    public enum SoftDeleteStatus implements IStatus{SUCCESS,NOT_FOUND, ALREADY_SOFT_DELETED};
    public enum HardDeleteStatus implements IStatus{SUCCESS,NOT_FOUND};

}
