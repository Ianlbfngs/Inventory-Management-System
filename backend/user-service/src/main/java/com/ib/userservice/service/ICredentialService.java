package com.ib.userservice.service;

import com.ib.userservice.entity.Credential;
import com.ib.userservice.response.Response;
import com.ib.userservice.response.Statuses;

public interface ICredentialService {
    Response<Statuses.CreateStatus> registerCredential(Credential credential);
    Response<Statuses.LoginStatus> loginCredential(Credential credential);
    Response<Statuses.UpdateStatus> updateCredential(Credential credential);
    Response<Statuses.SoftDeleteStatus> softDeleteCredential(int id);
    Response<Statuses.HardDeleteStatus> hardDeleteStatus(int id);
}
