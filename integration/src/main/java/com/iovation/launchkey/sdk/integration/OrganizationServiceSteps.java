/**
 * Copyright 2017 iovation, Inc.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iovation.launchkey.sdk.integration;

import com.iovation.launchkey.sdk.client.OrganizationClient;
import com.iovation.launchkey.sdk.integration.entities.ServiceEntity;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;

import javax.inject.Inject;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

@ScenarioScoped
public class OrganizationServiceSteps {
    private final OrganizationServiceManager organizationServiceManager;
    private final GenericSteps genericSteps;
    private ServiceEntity currentServiceEntity;
    private ServiceEntity previousServiceEntity;
    private OrganizationClient client;


    @Inject
    public OrganizationServiceSteps(OrganizationServiceManager organizationServiceManager,
                                    GenericSteps genericSteps) {
        this.organizationServiceManager = organizationServiceManager;
        this.genericSteps = genericSteps;
        this.currentServiceEntity = null;
        this.previousServiceEntity = null;
    }


    @When("^I created? an Organization Service")
    public void iCreateAnOrganizationService() throws Throwable {
        organizationServiceManager.createService();
    }

    @And("^I attempt to create a Organization Service with the same name$")
    public void iAttemptToCreateAOrganizationServiceWithTheSameName() throws Throwable {
        ServiceEntity service = organizationServiceManager.getCurrentServiceEntity();
        if (service == null) throw new Exception("Organization Service must be created before executing this step.");

        try {
            organizationServiceManager.createService(service.getName());
        } catch (Exception e) {
            this.genericSteps.setCurrentException(e);
        }
    }

    @And("^I retrieve the created Organization Service$")
    public void iRetrieveTheCreatedOrganizationService() throws Throwable {
        organizationServiceManager.retrieveCurrentService();
    }

    @Then("^the Organization Service name is the same as was sent$")
    public void theOrganizationServiceNameIsTheSameAsWasSent() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        assertThat(organizationServiceManager.getCurrentServiceEntity().getName(),
                is(equalTo(organizationServiceManager.getPreviousServiceEntity().getName())));
    }

    @And("^the Organization Service is (active|not active)")
    public void theOrganizationServiceIsActive(String active) throws Throwable {
        assertThat(organizationServiceManager.getCurrentServiceEntity().getActive(),
                is("active".equalsIgnoreCase(active)));
    }

    @When("^I created? a Organization Service with the following:$")
    public void iCreateAOrganizationServiceWithTheFollowing(Map<String, String> table) throws Throwable {
        String description = table.get("description");
        URI icon = table.containsKey("icon") ? URI.create(table.get("icon")) : null;
        URI callbackUrl = table.containsKey("callback_url") ? URI.create(table.get("callback_url")) : null;
        Boolean active =
                table.containsKey("active") ? String.valueOf(table.get("active")).equalsIgnoreCase("true") : null;
        organizationServiceManager.createService(description, icon, callbackUrl, active);
    }

    @When("^I updated? the Organization Service with the following:$")
    public void iUpdatedTheOrganizationServiceWithTheFollowing(Map<String, String> table) throws Throwable {
        String description = table.get("description");
        URI icon = table.containsKey("icon") ? URI.create(table.get("icon")) : null;
        URI callbackUrl = table.containsKey("callback_url") ? URI.create(table.get("callback_url")) : null;
        Boolean active =
                table.containsKey("active") ? String.valueOf(table.get("active")).equalsIgnoreCase("true") : null;
        organizationServiceManager.updateService(organizationServiceManager.getCurrentServiceEntity().getId(),
                description, icon, callbackUrl, active);
    }

    @And("^the Organization Service description is \"([^\"]*)\"$")
    public void theOrganizationServiceDescriptionIs(String description) throws Throwable {
        assertThat(organizationServiceManager.getCurrentServiceEntity().getDescription(),
                Is.is(CoreMatchers.equalTo(description)));

    }

    @And("^the Organization Service icon is \"([^\"]*)\"$")
    public void theOrganizationServiceIconIs(String icon) throws Throwable {
        assertThat(organizationServiceManager.getCurrentServiceEntity().getIcon(),
                Is.is(CoreMatchers.equalTo(URI.create(icon))));
    }

    @And("^the Organization Service callback_url is \"([^\"]*)\"$")
    public void theOrganizationServiceCallback_urlIs(String callback_url) throws Throwable {
        assertThat(organizationServiceManager.getCurrentServiceEntity().getCallbackURL(),
                Is.is(CoreMatchers.equalTo(URI.create(callback_url))));
    }

    @When("^I attempt retrieve retrieve the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptRetrieveRetrieveTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            organizationServiceManager.retrieveService(serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I retrieve the Organization Service with the ID \"([^\"]*)\"$")
    public void iRetrieveTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        organizationServiceManager.retrieveService(serviceId);
    }

    @When("^I attempt to retrieve the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptToRetrieveTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            organizationServiceManager.retrieveService(serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^the current Organization Service list is a list with only the current Service$")
    public void theCurrentOrganizationServiceListIsAListWithOnlyTheCurrentService() throws Throwable {
        assertThat("Unexpected list size", organizationServiceManager.getCurrentServiceEntities().size(), Is
                .is(CoreMatchers.equalTo(1)));
    }

    @When("^I retrieve a list of Organization Services with the created Service's ID$")
    public void iRetrieveAListOfOrganizationServicesWithTheCreatedServiceSID() throws Throwable {
        organizationServiceManager.retrieveServices(Collections.singletonList(organizationServiceManager.getCurrentServiceEntity().getId()));
    }

    @When("^I attempt retrieve a list of Organization Services with the Service ID \"([^\"]*)\"$")
    public void iAttemptRetrieveAListOfOrganizationServicesWithTheServiceID(String uuid) throws Throwable {
        List<UUID> serviceIds = Collections.singletonList(UUID.fromString(uuid));
        try {
            organizationServiceManager.retrieveServices(serviceIds);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I retrieve a list of all Organization Services with the created Service's ID$")
    public void iRetrieveAListOfAllOrganizationServicesWithTheCreatedServiceSID() throws Throwable {
        organizationServiceManager.retrieveAllServices();
    }

    @When("^I retrieve a list of all Organization Services$")
    public void iRetrieveAListOfAllOrganizationServices() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        organizationServiceManager.retrieveAllServices();
    }

    @Then("^the current Organization Service is in the Services list$")
    public void theCurrentOrganizationServiceIsInTheServicesList() throws Throwable {
        assertThat(organizationServiceManager.getCurrentServiceEntities(),
                hasItem(organizationServiceManager.getCurrentServiceEntity()));
    }

    @When("^I attempt to update the active status of the Organization Service with the ID \"([^\"]*)\"$")
    public void iAttemptToUpdateTheActiveStatusOfTheOrganizationServiceWithTheID(String uuid) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        UUID serviceId = UUID.fromString(uuid);
        try {
            organizationServiceManager.updateService(serviceId, null, null, null, null, Boolean.FALSE);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }
}
