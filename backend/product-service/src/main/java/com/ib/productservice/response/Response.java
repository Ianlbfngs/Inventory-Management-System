package com.ib.productservice.response;

import com.ib.productservice.entity.IEntity;
import com.ib.productservice.entity.Product;

public record Response<Status extends IStatus, Entity extends IEntity>(Status status, Entity entity) {
}
