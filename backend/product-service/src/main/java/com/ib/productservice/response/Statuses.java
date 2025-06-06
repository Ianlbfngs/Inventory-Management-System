package com.ib.productservice.response;

public class Statuses{
    //Product response status
    public enum CreateProductStatus implements IStatus {SUCCESS,SKU_IN_USE,NEGATIVE_WEIGHT}
    public enum UpdateProductStatus implements IStatus{SUCCESS,SOFT_DELETED,NOT_FOUND,SKU_IN_USE,NEGATIVE_WEIGHT}
    public enum SoftDeleteProductStatus implements IStatus{SUCCESS,ALREADY_SOFT_DELETED,NOT_FOUND}
    public enum HardDeleteProductStatus implements IStatus{SUCCESS,NOT_FOUND}
    //Batch response status
    public enum CreateBatchStatus implements IStatus {SUCCESS,CODE_IN_USE}
    public enum UpdateBatchStatus implements IStatus{SUCCESS,NOT_FOUND,CODE_IN_USE}
    public enum SoftDeleteBatchStatus implements IStatus{SUCCESS,NOT_FOUND,ALREADY_SOFT_DELETED}
    public enum HardDeleteBatchStatus implements IStatus{SUCCESS,NOT_FOUND}
    //Category response status
    public enum UpdateCategoryStatus implements IStatus{SUCCESS,NOT_FOUND}
    public enum HardDeleteCategoryStatus implements IStatus{SUCCESS,NOT_FOUND}
}
