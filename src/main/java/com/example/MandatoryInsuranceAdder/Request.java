package com.example.MandatoryInsuranceAdder;

import com.commercetools.api.models.common.Reference;

public class Request {

    private String action;

    private Reference resource;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Reference getResource() {
        return resource;
    }

    public void setResource(Reference resource) {
        this.resource = resource;
    }
}
