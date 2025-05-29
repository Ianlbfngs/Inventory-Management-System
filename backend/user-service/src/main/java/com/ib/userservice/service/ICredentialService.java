package com.ib.userservice.service;

import com.ib.userservice.entity.Credential;
import com.ib.userservice.response.Response;
import com.ib.userservice.response.Statuses;

public interface ICredentialService {
    Response<Statuses.CreateStatus> registerCredential(Credential credential);
    Response<Statuses.LoginStatus> login(Credential credential);
    Response<Statuses.UpdateStatus> updateCredential(Credential credential);
    Response<Statuses.LogicalDeleteStatus> logicalDelete(int id);


}
