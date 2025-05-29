package com.ib.userservice.response;

public class Statuses{
    public enum CreateStatus implements IStatus{SUCCESS,USER_IN_USE};
    public enum LoginStatus implements IStatus{OK,NOT_ACTIVE,NOT_FOUND,DENIED};
    public enum UpdateStatus implements IStatus{SUCCESS,NOT_ACTIVE,NOT_FOUND,USER_IN_USE};
    public enum LogicalDeleteStatus implements IStatus{SUCCESS,NOT_FOUND,ALREADY_DELETED};
}
