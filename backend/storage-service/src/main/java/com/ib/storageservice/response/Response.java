package com.ib.storageservice.response;

import com.ib.storageservice.entity.Storage;
import com.ib.storageservice.response.IStatus;

public record Response<Status extends IStatus>(Status status, Storage storage) {
}
