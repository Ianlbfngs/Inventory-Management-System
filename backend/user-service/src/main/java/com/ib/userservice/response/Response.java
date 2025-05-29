package com.ib.userservice.response;

import com.ib.userservice.entity.Credential;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<Status extends IStatus> {
    private final Status status;
    private final Credential credential;
}
