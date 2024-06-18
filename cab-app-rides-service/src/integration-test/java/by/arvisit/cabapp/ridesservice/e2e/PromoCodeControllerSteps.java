package by.arvisit.cabapp.ridesservice.e2e;

import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.BRILLIANT10_ID;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_PROMO_CODES;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.URL_PROMO_CODES_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getAddedPromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getBRILLIANT10ActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getNewPromoCodeRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getPAIN49ActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideIntegrationTestData.getRICE23NotActivePromoCodeResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PromoCodeControllerSteps {

    private static final String PAGE_REQUEST_PARAM = "page";

    private static final String ID_FIELD = "id";

    private PromoCodeRequestDto promoCodeRequest;
    private Response response;
    private Integer promoCodeId;
    private List<PromoCodeResponseDto> promoCodesBeforeDelete;

    @Given("User wants to save a new promo code with keyword {string} and discount percent {int}")
    public void prepareNewPromoCodeToSave(String keyword, int discountPercent) {
        promoCodeRequest = getNewPromoCodeRequestDto()
                .withKeyword(keyword)
                .withDiscountPercent(discountPercent)
                .build();
    }

    @When("he performs saving of a new promo code via request")
    public void sendSaveNewPromoCodeRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(promoCodeRequest)
                .when().post(URL_PROMO_CODES);
    }

    @Then("response should have 201 status, json content type, contain promo code with expected parameters and id")
    public void checkSaveNewPromoCodeResponse() {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto actual = response.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getAddedPromoCodeResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields(ID_FIELD)
                .isEqualTo(expected);
        assertThat(actual.id())
                .isNotNull();
    }

    @Given("User wants to update an existing promo code with keyword {string} and discount percent {int}")
    public void prepareUpdateRequest(String keyword, int discountPercent) {
        promoCodeRequest = getNewPromoCodeRequestDto()
                .withKeyword(keyword)
                .withDiscountPercent(discountPercent)
                .build();
    }

    @When("he performs update of existing promo code with id {int} via request")
    public void sendUpdatePromoCodeRequest(int id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(promoCodeRequest)
                .when().put(URL_PROMO_CODES_ID_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain promo code with keyword {string} and discount percent {int}")
    public void checkUpdatePromoCodeResponse(String keyword, int discountPercent) {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto actual = response.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getBRILLIANT10ActivePromoCodeResponseDto()
                .withKeyword(keyword)
                .withDiscountPercent(discountPercent)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to deactivate an existing promo code")
    public void prepareDeactivateRequest() {
    }

    @When("he performs deactivation of existing promo code with id {int} via request")
    public void sendDeactivatePromoCodeRequest(int id) {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain deactivated promo code")
    public void checkDeactivatePromoCodeResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto actual = response.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getPAIN49ActivePromoCodeResponseDto()
                .withIsActive(false)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to delete an existing promo code with id {int}")
    public void prepareInfoForPromoCodeDelete(int id) {
        promoCodeId = id;
        promoCodesBeforeDelete = extractAllItems();
    }

    @When("he performs delete of existing promo code via request")
    public void sendDeletePromoCodeRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_PROMO_CODES_ID_TEMPLATE, promoCodeId);
    }

    @Then("response should have 204 status, minus one promo code in database")
    public void checkDeletePromoCodeResponse() {
        response.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<PromoCodeResponseDto> promoCodesAfterDelete = extractAllItems();

        assertThat(promoCodesAfterDelete).size()
                .isEqualTo(promoCodesBeforeDelete.size() - 1);

        Predicate<? super PromoCodeResponseDto> matchById = p -> p.id()
                .equals(BRILLIANT10_ID);
        assertThat(promoCodesBeforeDelete)
                .anyMatch(matchById);
        assertThat(promoCodesAfterDelete)
                .noneMatch(matchById);
    }

    @Given("User wants to get details about an existing promo code with id {int}")
    public void prepareInfoForRetrievingPromoCodeById(int id) {
        promoCodeId = id;
    }

    @When("he performs search promo code by id via request")
    public void sendGetPromoCodeByIdRequest() {
        response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PROMO_CODES_ID_TEMPLATE, promoCodeId);
    }

    @Then("response should have 200 status, json content type, contain promo code with requested id")
    public void checkGetPromoCodeByIdResponse() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto actual = response.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getRICE23NotActivePromoCodeResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private List<PromoCodeResponseDto> extractAllItems() {
        List<PromoCodeResponseDto> items = new ArrayList<>();

        int nextPage = 0;
        int lastPage = 0;

        do {
            Response tmpResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .queryParam(PAGE_REQUEST_PARAM, nextPage)
                    .when().get(URL_PROMO_CODES);
            ListContainerResponseDto<PromoCodeResponseDto> itemsContainer = tmpResponse
                    .as(new TypeRef<ListContainerResponseDto<PromoCodeResponseDto>>() {
                    });
            items.addAll(itemsContainer.values());
            lastPage = itemsContainer.lastPage();
            nextPage++;
        } while (nextPage <= lastPage);

        return items;
    }
}
