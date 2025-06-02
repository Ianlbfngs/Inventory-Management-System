package com.ib.movementsservice.response;

import com.ib.movementsservice.entity.IEntity;
import com.ib.movementsservice.response.IStatus;

public record Response<Status extends IStatus, Entity extends IEntity>(Status status, Entity entity) {
}
