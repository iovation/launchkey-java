package com.iovation.launchkey.sdk.integration.steps;

import com.google.inject.Inject;
import com.iovation.launchkey.sdk.integration.entities.DirectoryEntity;
import com.iovation.launchkey.sdk.integration.managers.DirectoryManager;
import com.iovation.launchkey.sdk.integration.managers.DirectoryServiceManager;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;

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
@ScenarioScoped
public class DirectoryServiceSteps {
    private final GenericSteps genericSteps;
    private final DirectoryManager directoryManager;
    private final DirectoryServiceManager directoryServiceManager;

    @Inject
    public DirectoryServiceSteps(DirectoryManager directoryManager, GenericSteps genericSteps,
                                 DirectoryServiceManager directoryServiceManager) {
        this.genericSteps = genericSteps;
        this.directoryManager = directoryManager;
        this.directoryServiceManager = directoryServiceManager;
    }


    @And("^I add(?:ed)? (\\d+) Services to the Directory$")
    public void iAddedServicesToTheDirectory(int numberOfServices) throws Throwable {
        for (int i = 0; i < numberOfServices; i++) {
            iCreatedADirectoryService();
        }
    }

    @And("^I retrieve the created Directory Service$")
    public void iRetrieveTheCreatedDirectoryService() throws Throwable {
        directoryServiceManager.retrieveCurrentService(getCurrentDirectory().getId());
    }

    @Then("^the Directory Service name is the same as was sent$")
    public void theDirectoryServiceNameIsTheSameAsWasSent() throws Throwable {
        assertThat(directoryServiceManager.getCurrentServiceEntity().getName(),
                is(equalTo(directoryServiceManager.getPreviousServiceEntity().getName())));
    }

    @Given("^I created? a Directory Service$")
    public void iCreatedADirectoryService() throws Throwable {
        UUID serviceId = directoryServiceManager.createService(getCurrentDirectory().getId());
        getCurrentDirectory().getServiceIds().add(serviceId);
    }

    @And("^I attempt to create a Directory Service with the same name$")
    public void iAttemptToCreateADirectoryServiceWithTheSameName() throws Throwable {
        try {
            directoryServiceManager.createService(getCurrentDirectory().getId(),
                    directoryServiceManager.getCurrentServiceEntity().getName());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    private DirectoryEntity getCurrentDirectory() {
        DirectoryEntity directoryEntity = directoryManager.getCurrentDirectoryEntity();
        assertThat("You must create a Directory first", directoryEntity, not(nullValue()));
        return directoryEntity;
    }

    @When("^I created? a Directory Service with the following:$")
    public void iCreateADirectoryServiceWithTheFollowing(Map<String, String> table) throws Throwable {
        String description = table.get("description");
        URI icon = table.containsKey("icon") ? URI.create(table.get("icon")) : null;
        URI callbackUrl = table.containsKey("callback_url") ? URI.create(table.get("callback_url")) : null;
        Boolean active =
                table.containsKey("active") ? String.valueOf(table.get("active")).equalsIgnoreCase("true") : null;
        directoryServiceManager.createService(getCurrentDirectory().getId(), description, icon, callbackUrl, active);
    }

    @When("^I updated? the Directory Service with the following:$")
    public void iUpdatedTheDirectoryServiceWithTheFollowing(Map<String, String> table) throws Throwable {
        String description = table.get("description");
        URI icon = table.containsKey("icon") ? URI.create(table.get("icon")) : null;
        URI callbackUrl = table.containsKey("callback_url") ? URI.create(table.get("callback_url")) : null;
        Boolean active =
                table.containsKey("active") ? String.valueOf(table.get("active")).equalsIgnoreCase("true") : null;
        directoryServiceManager
                .updateService(getCurrentDirectory().getId(), directoryServiceManager.getCurrentServiceEntity().getId(),
                        description, icon, callbackUrl, active);
    }

    @And("^the Directory Service description is \"([^\"]*)\"$")
    public void theDirectoryServiceDescriptionIs(String description) throws Throwable {
        assertThat(directoryServiceManager.getCurrentServiceEntity().getDescription(),
                is(equalTo(description)));

    }

    @And("^the Directory Service icon is \"([^\"]*)\"$")
    public void theDirectoryServiceIconIs(String icon) throws Throwable {
        assertThat(directoryServiceManager.getCurrentServiceEntity().getIcon(),
                is(equalTo(URI.create(icon))));
    }

    @And("^the Directory Service callback_url is \"([^\"]*)\"$")
    public void theDirectoryServiceCallback_urlIs(String callback_url) throws Throwable {
        assertThat(directoryServiceManager.getCurrentServiceEntity().getCallbackURL(),
                is(equalTo(URI.create(callback_url))));
    }

    @And("^the Directory Service is (active|not active)$")
    public void theDirectoryServiceIsActive(String active) throws Throwable {
        assertThat(directoryServiceManager.getCurrentServiceEntity().getActive(),
                is("active".equalsIgnoreCase(active)));
    }

    @When("^I attempt retrieve retrieve the Directory Service with the ID \"([^\"]*)\"$")
    public void iAttemptRetrieveRetrieveTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            directoryServiceManager.retrieveService(serviceId, getCurrentDirectory().getId());
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I retrieve the Directory Service with the ID \"([^\"]*)\"$")
    public void iRetrieveTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        directoryServiceManager.retrieveService(getCurrentDirectory().getId(), serviceId);
    }

    @When("^I attempt to retrieve the Directory Service with the ID \"([^\"]*)\"$")
    public void iAttemptToRetrieveTheDirectoryServiceWithTheID(String uuid) throws Throwable {
        UUID serviceId = UUID.fromString(uuid);
        try {
            directoryServiceManager.retrieveService(getCurrentDirectory().getId(), serviceId);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @Then("^the current Directory Service list is a list with only the current Service$")
    public void theCurrentDirectoryServiceListIsAListWithOnlyTheCurrentService() throws Throwable {
        assertThat("Unexpected list size", directoryServiceManager.getCurrentServiceEntities().size(), is(equalTo(1)));
    }

    @When("^I retrieve a list of Directory Services with the created Service's ID$")
    public void iRetrieveAListOfDirectoryServicesWithTheCreatedServiceSID() throws Throwable {
        directoryServiceManager.retrieveServices(getCurrentDirectory().getId(),
                Collections.singletonList(directoryServiceManager.getCurrentServiceEntity().getId()));
    }

    @When("^I attempt retrieve a list of Directory Services with the Service ID \"([^\"]*)\"$")
    public void iAttemptRetrieveAListOfDirectoryServicesWithTheServiceID(String uuid) throws Throwable {
        List<UUID> serviceIds = Collections.singletonList(UUID.fromString(uuid));
        try {
            directoryServiceManager.retrieveServices(getCurrentDirectory().getId(), serviceIds);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }

    @When("^I retrieve a list of all Directory Services with the created Service's ID$")
    public void iRetrieveAListOfAllDirectoryServicesWithTheCreatedServiceSID() throws Throwable {
        directoryServiceManager.retrieveAllServices(getCurrentDirectory().getId());
    }

    @Then("^the current Directory Service list is an empty list$")
    public void theCurrentDirectoryServiceListIsAnEmptyList() throws Throwable {
        assertThat(directoryServiceManager.getCurrentServiceEntities(), is(empty()));
    }

    @When("^I retrieve a list of all Directory Services$")
    public void iRetrieveAListOfAllDirectoryServices() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        directoryServiceManager.retrieveAllServices(getCurrentDirectory().getId());
    }

    @Then("^the current Directory Service is in the Services list$")
    public void theCurrentDirectoryServiceIsInTheServicesList() throws Throwable {
        assertThat(directoryServiceManager.getCurrentServiceEntities(),
                hasItem(directoryServiceManager.getCurrentServiceEntity()));
    }

    @When("^I attempt to update the active status of the Directory Service with the ID \"([^\"]*)\"$")
    public void iAttemptToUpdateTheActiveStatusOfTheDirectoryServiceWithTheID(String uuid) throws Throwable {

        final UUID serviceId = UUID.fromString(uuid);
        try {
            directoryServiceManager
                    .updateService(getCurrentDirectory().getId(), serviceId, null, null, null, Boolean.TRUE);
        } catch (Exception e) {
            genericSteps.setCurrentException(e);
        }
    }
}