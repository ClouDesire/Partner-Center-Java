// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license. See the LICENSE file in the project root for full license information.

package com.microsoft.store.partnercenter.subscriptions;

import java.text.MessageFormat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microsoft.store.partnercenter.BasePartnerComponent;
import com.microsoft.store.partnercenter.IPartner;
import com.microsoft.store.partnercenter.PartnerService;
import com.microsoft.store.partnercenter.models.ResourceCollection;
import com.microsoft.store.partnercenter.models.subscriptions.AzureEntitlement;
import com.microsoft.store.partnercenter.models.subscriptions.Subscription;
import com.microsoft.store.partnercenter.models.subscriptions.SubscriptionActivationResult;
import com.microsoft.store.partnercenter.models.utils.Tuple;
import com.microsoft.store.partnercenter.usage.ISubscriptionUsageRecordCollection;
import com.microsoft.store.partnercenter.usage.ISubscriptionUsageSummary;
import com.microsoft.store.partnercenter.usage.SubscriptionUsageRecordCollectionOperations;
import com.microsoft.store.partnercenter.usage.SubscriptionUsageSummaryOperations;
import com.microsoft.store.partnercenter.utilization.IUtilizationCollection;
import com.microsoft.store.partnercenter.utilization.UtilizationCollectionOperations;
import com.microsoft.store.partnercenter.utils.StringHelper;

/**
 * This class implements the operations for a customer's subscription.
 */
public class SubscriptionOperations
    extends BasePartnerComponent<Tuple<String, String>>
    implements ISubscription
{
    /**
     * A reference to the current subscription's activation link operations.
     */
    private ISubscriptionActivationLinks subscriptionActivationLinks;

    /**
     * A reference to the current subscription's add-ons operations.
     */
    private ISubscriptionAddOnCollection subscriptionAddOnsOperations;

    /**
     * A reference to the current subscription's upgrade operations.
     */
    private ISubscriptionUpgradeCollection subscriptionUpgradeOperations;
    
    /**
     * A reference to the current subscription's resource usage records operations.
     */
    private ISubscriptionUsageRecordCollection usageRecordsOperations;

    /**
     * A reference to the current subscription's usage summary operations.
     */
    private ISubscriptionUsageSummary subscriptionUsageSummaryOperations;
    
    /**
     * A reference to the current subscription's utilities operations.
     */
    private IUtilizationCollection subscriptionUtilizationOperations;

    /**
     * A reference to the current subscription's provisioning status operations.
     */
    private ISubscriptionProvisioningStatus subscriptionProvisioningStatusOperations;

    /**
     * A reference to the current subscription's support contact operations.
     */
    private ISubscriptionSupportContact subscriptionSupportContactOperations;

    /**
     * A reference to the current subscription's registration status operations.
     */
    private ISubscriptionRegistration subscriptionRegistrationOperations;

    /**
     * A reference to the current subscription's registration status operations.
     */
    private ISubscriptionRegistrationStatus subscriptionRegistrationStatusOperations; 

    /**
     * A lazy reference to the current subscription's conversion operations.
     */
    private ISubscriptionConversionCollection subscriptionConversionOperations; 

    /**
     * Initializes a new instance of the SubscriptionOperations class.
     * 
     * @param rootPartnerOperations The root partner operations instance.
     * @param customerId The customer identifier.
     * @param subscriptionId The subscription identifier
     */
    public SubscriptionOperations(IPartner rootPartnerOperations, String customerId, String subscriptionId)
    {
        super(rootPartnerOperations, new Tuple<String, String>(customerId, subscriptionId));

        if (StringHelper.isNullOrWhiteSpace(customerId))
        {
            throw new IllegalArgumentException("customerId must be set.");
        }

        if (StringHelper.isNullOrWhiteSpace(subscriptionId))
        {
            throw new IllegalArgumentException("subscriptionId must be set.");
        }
    }

    /**
     * Activates a third-party subscription.
     * 
     * @return The result from the subscription activation.
     */
    public SubscriptionActivationResult activate()
    {
        return this.getPartner().getServiceClient().post(
            this.getPartner(),
            new TypeReference<SubscriptionActivationResult>(){}, 
            MessageFormat.format(
                PartnerService.getInstance().getConfiguration().getApis().get("Activate3ppSubscription").getPath(),
                this.getContext().getItem1(), 
                this.getContext().getItem2()), 
            null);
    }

    /**
     * Gets the current subscription's activation links.
     * 
     * @return The current subscription's activation links.
     */
    public ISubscriptionActivationLinks getActivationLinks()
    {
        if(subscriptionActivationLinks == null)
        {
            subscriptionActivationLinks = new SubscriptionActivationLinksOperations(
                this.getPartner(), 
                this.getContext().getItem1(), 
                this.getContext().getItem2());
        }

        return subscriptionActivationLinks;
    }

    /**
     * Gets the current subscription's add-ons operations.
     */
    @Override
    public ISubscriptionAddOnCollection getAddOns()
    {
        if (this.subscriptionAddOnsOperations == null)
        {
            this.subscriptionAddOnsOperations =
                new SubscriptionAddOnCollectionOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }

        return this.subscriptionAddOnsOperations;
    }

    /**
     * Gets an Azure Plan's subscription entitlements.
     * 
     * @return A resource collection of Azure entitlements.
     */
    public ResourceCollection<AzureEntitlement> getAzurePlanSubscriptionEntitlements()
    {
        return this.getPartner().getServiceClient().get(
            this.getPartner(),
            new TypeReference<ResourceCollection<AzureEntitlement>>(){}, 
            MessageFormat.format(
                PartnerService.getInstance().getConfiguration().getApis().get("GetAzureEntitlements").getPath(),
                this.getContext().getItem1(), 
                this.getContext().getItem2()));
    }

    /**
     * Gets the current subscription's upgrade operations.
     */
    @Override
    public ISubscriptionUpgradeCollection getUpgrades()
    {
        if (this.subscriptionUpgradeOperations == null)
        {
            this.subscriptionUpgradeOperations =
                new SubscriptionUpgradeCollectionOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }

        return this.subscriptionUpgradeOperations;
    }

    /**
     * Gets the current subscription's usage record operations.
     */
    @Override
    public ISubscriptionUsageRecordCollection getUsageRecords()
    {
        if (this.usageRecordsOperations == null)
        {
            this.usageRecordsOperations =
                new SubscriptionUsageRecordCollectionOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }

        return this.usageRecordsOperations;
    }

    /**
     * Gets the current subscription's usage summary operations.
     */
    @Override
    public ISubscriptionUsageSummary getUsageSummary()
    {
        if (this.subscriptionUsageSummaryOperations == null)
        {
            this.subscriptionUsageSummaryOperations =
                new SubscriptionUsageSummaryOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }

        return this.subscriptionUsageSummaryOperations;
    }

    /**
     * Gets the current subscription's utilization operations.
     */
    @Override
    public IUtilizationCollection getUtilization()
    {
        if (this.subscriptionUtilizationOperations == null)
        {
            this.subscriptionUtilizationOperations =
                new UtilizationCollectionOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }

        return this.subscriptionUtilizationOperations;
    }

    /**
     * Gets the current subscription's provisioning status operations.
     */
    public ISubscriptionProvisioningStatus getProvisioningStatus()
    {
        if(subscriptionProvisioningStatusOperations == null)
        {
            subscriptionProvisioningStatusOperations = new SubscriptionProvisioningStatusOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }

        return subscriptionProvisioningStatusOperations;
    }

    /**
     * Gets the current subscription's support contact operations.
     */
    public ISubscriptionSupportContact getSupportContact()
    {
        if(subscriptionSupportContactOperations == null)
        {
            subscriptionSupportContactOperations = new SubscriptionSupportContactOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }

        return subscriptionSupportContactOperations; 
    }

    /**
     * Gets the current subscription's registration operations.
     */
    public ISubscriptionRegistration getRegistration()
    {
        if(subscriptionRegistrationOperations == null)
        {
            subscriptionRegistrationOperations = new SubscriptionRegistrationOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }

        return subscriptionRegistrationOperations;
    }

    /**
     * Gets the current subscription's registration status operations.
     */
    public ISubscriptionRegistrationStatus getRegistrationStatus()
    {
        if (subscriptionRegistrationStatusOperations == null)
        {
            subscriptionRegistrationStatusOperations = new SubscriptionRegistrationStatusOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }

        return subscriptionRegistrationStatusOperations;
    }

    public ISubscriptionConversionCollection getConversions()
    {
        if (this.subscriptionConversionOperations == null)
        {
            subscriptionConversionOperations = new SubscriptionConversionCollectionOperations(this.getPartner(), this.getContext().getItem1(), this.getContext().getItem2());
        }
        
        return subscriptionConversionOperations; 
    }

    /**
     * Gets the subscription.
     * 
     * @return The subscription.
     */
    @Override
    public Subscription get()
    {
        return this.getPartner().getServiceClient().get(
            this.getPartner(),
            new TypeReference<Subscription>(){}, 
            MessageFormat.format(
                PartnerService.getInstance().getConfiguration().getApis().get("GetSubscription").getPath(),
                this.getContext().getItem1(), 
                this.getContext().getItem2()));
    }

    /**
     * Patches a subscription.
     * 
     * @param subscription The subscription information.
     * @return The updated subscription information.
     */
    @Override
    public Subscription patch(Subscription subscription)
    {
        if (subscription == null)
        {
            throw new IllegalArgumentException("subscription is required.");
        }

        return this.getPartner().getServiceClient().patch(
            this.getPartner(),
            new TypeReference<Subscription>(){}, 
            MessageFormat.format(
                PartnerService.getInstance().getConfiguration().getApis().get("UpdateSubscription").getPath(),
                this.getContext().getItem1(), 
                this.getContext().getItem2()),
            subscription);
    }
}
