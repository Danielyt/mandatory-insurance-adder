package com.example.MandatoryInsuranceAdder;

import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.common.LocalizedString;
import com.commercetools.api.models.common.Money;
import com.commercetools.api.models.tax_category.TaxCategory;
import com.commercetools.api.models.tax_category.TaxCategoryResourceIdentifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@RestController
public class InsuranceController {

    public static final String MANDATORY_INSURANCE = "mandatory-insurance";

    @Value("${tax.category.id}")
    private String taxCategoryId;

    @Value("${price.threshold}")
    private Long priceThreshold;

    @Value("${insurance.cent.amount}")
    private Long insuranceCentAmount;

    @PostMapping(value = "addMandatoryInsurance", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Response addMandatoryInsurance(@RequestBody final Request request) {
        Response response = new Response();
        Cart cart = ((CartReference) request.getResource()).getObj();
        boolean cartRequiresInsurance = cart.getLineItems()
                .stream()
                .anyMatch(lineItem -> lineItem.getTotalPrice()
                        .getCentAmount() > priceThreshold);
        Optional<CustomLineItem> insuranceItem = cart.getCustomLineItems()
                .stream()
                .filter(customLineItem -> MANDATORY_INSURANCE.equals(customLineItem.getSlug()))
                .findFirst();
        boolean cartHasInsurance = insuranceItem.isPresent();
        if (cartRequiresInsurance && !cartHasInsurance) {
            CartAddCustomLineItemAction action = CartAddCustomLineItemAction.builder()
                    .name(LocalizedString.builder()
                            .addValue("en", "Mandatory Insurance for items above " + priceThreshold)
                            .build())
                    .money(Money.builder()
                            .currencyCode(cart.getTotalPrice()
                                    .getCurrencyCode())
                            .centAmount(insuranceCentAmount)
                            .build())
                    .slug("mandatory-insurance")
                    .taxCategory(TaxCategoryResourceIdentifier.builder().id(taxCategoryId).build())
                    .build();
            response.setActions(Arrays.asList(action));
        } else if (!cartRequiresInsurance && cartHasInsurance) {
            CartRemoveCustomLineItemAction action = CartRemoveCustomLineItemAction.builder()
                    .customLineItemId(insuranceItem.get()
                            .getId())
                    .build();
            response.setActions(Arrays.asList(action));
        }
        return response;
    }
}
