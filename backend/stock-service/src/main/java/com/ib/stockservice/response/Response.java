package com.ib.stockservice.response;

import com.ib.stockservice.entity.Stock;

public record Response<Status extends IStatus>(Status status, Stock stock) {
}
