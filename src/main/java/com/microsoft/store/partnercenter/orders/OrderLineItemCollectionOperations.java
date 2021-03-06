// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license. See the LICENSE file in the project root for full license information.

package com.microsoft.store.partnercenter.orders;

import com.microsoft.store.partnercenter.BasePartnerComponent;
import com.microsoft.store.partnercenter.IPartner;
import com.microsoft.store.partnercenter.models.utils.Tuple;
import com.microsoft.store.partnercenter.utils.StringHelper;

/**
 * Implements the order collection operations.
 */
public class OrderLineItemCollectionOperations 
    extends BasePartnerComponent<Tuple<String, String>> 
    implements IOrderLineItemCollection 
{
    /**
     * Initializes a new instance of the OrderLineItemCollectionOperations class.
     * 
     * @param rootPartnerOperations The root partner operations instance.
     * @param customerId The customer identifier.
     * @param orderId The order identifier.
     */
    public OrderLineItemCollectionOperations(IPartner rootPartnerOperations, String customerId, String orderId) {
        super(rootPartnerOperations, new Tuple<String, String>(customerId, orderId));

        if (StringHelper.isNullOrWhiteSpace(customerId)) 
        {
            throw new IllegalArgumentException("customerId must be set.");
        }

        if (StringHelper.isNullOrWhiteSpace(orderId)) 
        {
            throw new IllegalArgumentException("orderId must be set.");
        }
    }

    /**
     * Gets the available order line item operations.
     * 
     * @return The available order line item operations.
     */
    @Override
    public IOrderLineItem byId(Integer id) 
    {
        return new OrderLineItemOperations(
            this.getPartner(), 
            this.getContext().getItem1(),
            this.getContext().getItem2(), 
            id);
    }
}