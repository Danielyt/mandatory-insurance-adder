package com.example.MandatoryInsuranceAdder;

import com.commercetools.api.models.ResourceUpdateAction;

import java.util.List;

public class Response {

    List<ResourceUpdateAction<?>> actions;

    public List<ResourceUpdateAction<?>> getActions() {
        return actions;
    }

    public void setActions(List<ResourceUpdateAction<?>> actions) {
        this.actions = actions;
    }
}
