package com.ib.userservice.response;

import com.ib.userservice.entity.Credential;


public record Response<Status extends IStatus>(Status status, Credential credential) {
}
