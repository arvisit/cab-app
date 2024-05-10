package by.arvisit.cabapp.ridesservice.component;

import static by.arvisit.cabapp.ridesservice.util.RideITData.BRILLIANT10_ID;
import static by.arvisit.cabapp.ridesservice.util.RideITData.DEFAULT_PAGEABLE_SIZE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.NEW_PROMO_CODE_DISCOUNT_PERCENT;
import static by.arvisit.cabapp.ridesservice.util.RideITData.NEW_PROMO_CODE_KEYWORD;
import static by.arvisit.cabapp.ridesservice.util.RideITData.UNSORTED;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_PROMO_CODES;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_PROMO_CODES_ACTIVE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.URL_PROMO_CODES_ID_TEMPLATE;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getAddedPromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getBRILLIANT10ActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getListContainerForResponse;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getNewPromoCodeRequestDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getPAIN49ActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getPAIN49NotActivePromoCodeResponseDto;
import static by.arvisit.cabapp.ridesservice.util.RideITData.getRICE23NotActivePromoCodeResponseDto;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import by.arvisit.cabapp.common.dto.ListContainerResponseDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeRequestDto;
import by.arvisit.cabapp.ridesservice.dto.PromoCodeResponseDto;
import by.arvisit.cabapp.ridesservice.persistence.model.PromoCode;
import by.arvisit.cabapp.ridesservice.persistence.repository.PromoCodeRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PromoCodeControllerSteps {

    private static final String ID_FIELD = "id";

    @Autowired
    private PromoCodeRepository promoCodeRepository;

    private PromoCodeRequestDto saveNewPromoCodeRequest;
    private Response saveNewPromoCodeResponse;
    private PromoCodeRequestDto updatePromoCodeRequest;
    private Response updatePromoCodeResponse;
    private Response deactivatePromoCodeResponse;
    private Integer deletePromoCodeId;
    private List<PromoCode> promoCodesBeforeDelete;
    private Response deletePromoCodeResponse;
    private Integer idToGetPromoCodeBy;
    private Response getPromoCodeByIdResponse;
    private Response getPromoCodesWithNoRequestParamsResponse;
    private Response getActivePromoCodesWithNoRequestParamsResponse;

    @Given("User wants to save a new promo code with keyword {string} and discount percent {int}")
    public void prepareNewPromoCodeToSave(String keyword, int discountPercent) {
        saveNewPromoCodeRequest = getNewPromoCodeRequestDto()
                .withKeyword(keyword)
                .withDiscountPercent(discountPercent)
                .build();
    }

    @When("he performs saving of a new promo code via request")
    public void sendSaveNewPromoCodeRequest() {
        saveNewPromoCodeResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(saveNewPromoCodeRequest)
                .when().post(URL_PROMO_CODES);
    }

    @Then("response should have 201 status, json content type, contain promo code with expected parameters and id")
    public void checkSaveNewPromoCodeResponse() {
        saveNewPromoCodeResponse.then()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto actual = saveNewPromoCodeResponse.as(PromoCodeResponseDto.class);
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
        updatePromoCodeRequest = getNewPromoCodeRequestDto()
                .withKeyword(keyword)
                .withDiscountPercent(discountPercent)
                .build();
    }

    @When("he performs update of existing promo code with id {int} via request")
    public void sendUpdatePromoCodeRequest(int id) {
        updatePromoCodeResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatePromoCodeRequest)
                .when().put(URL_PROMO_CODES_ID_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain promo code with updated parameters")
    public void checkUpdatePromoCodeResponse() {
        updatePromoCodeResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto actual = updatePromoCodeResponse.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getBRILLIANT10ActivePromoCodeResponseDto()
                .withKeyword(NEW_PROMO_CODE_KEYWORD)
                .withDiscountPercent(NEW_PROMO_CODE_DISCOUNT_PERCENT)
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
        deactivatePromoCodeResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().patch(URL_PROMO_CODES_ID_DEACTIVATE_TEMPLATE, id);
    }

    @Then("response should have 200 status, json content type, contain deactivated promo code")
    public void checkDeactivatePromoCodeResponse() {
        deactivatePromoCodeResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto actual = deactivatePromoCodeResponse.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getBRILLIANT10ActivePromoCodeResponseDto()
                .withIsActive(false)
                .build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to delete an existing promo code with id {int}")
    public void prepareInfoForPromoCodeDelete(int id) {
        deletePromoCodeId = id;
    }

    @When("he performs delete of existing promo code via request")
    public void sendDeletePromoCodeRequest() {
        promoCodesBeforeDelete = promoCodeRepository.findAll();

        deletePromoCodeResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().delete(URL_PROMO_CODES_ID_TEMPLATE, deletePromoCodeId);
    }

    @Then("response should have 204 status, minus one promo code in database")
    public void checkDeletePromoCodeResponse() {
        deletePromoCodeResponse.then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        List<PromoCode> promoCodesAfterDelete = promoCodeRepository.findAll();

        assertThat(promoCodesAfterDelete).size()
                .isEqualTo(promoCodesBeforeDelete.size() - 1);

        Predicate<? super PromoCode> matchById = p -> p.getId()
                .equals(BRILLIANT10_ID);
        assertThat(promoCodesBeforeDelete)
                .anyMatch(matchById);
        assertThat(promoCodesAfterDelete)
                .noneMatch(matchById);
    }

    @Given("User wants to get details about an existing promo code with id {int}")
    public void prepareInfoForRetrievingPromoCodeById(int id) {
        idToGetPromoCodeBy = id;
    }

    @When("he performs search promo code by id via request")
    public void sendGetPromoCodeByIdRequest() {
        getPromoCodeByIdResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PROMO_CODES_ID_TEMPLATE, idToGetPromoCodeBy);
    }

    @Then("response should have 200 status, json content type, contain promo code with requested id")
    public void checkGetPromoCodeByIdResponse() {
        getPromoCodeByIdResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        PromoCodeResponseDto actual = getPromoCodeByIdResponse.as(PromoCodeResponseDto.class);
        PromoCodeResponseDto expected = getBRILLIANT10ActivePromoCodeResponseDto().build();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Given("User wants to get details about existing promo codes")
    public void prepareInfoForRetrievingPromoCodes() {
    }

    @When("he performs request with no request parameters")
    public void sendGetPromoCodesWithNoRequestParamsRequest() {
        getPromoCodesWithNoRequestParamsResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PROMO_CODES);
    }

    @Then("response should have 200 status, json content type, contain info about {int} promo codes")
    public void checkGetPromoCodesWithNoRequestParams(int promoCodesCount) {
        getPromoCodesWithNoRequestParamsResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PromoCodeResponseDto> actual = getPromoCodesWithNoRequestParamsResponse
                .as(new TypeRef<ListContainerResponseDto<PromoCodeResponseDto>>() {
                });

        ListContainerResponseDto<PromoCodeResponseDto> expected = getListContainerForResponse(
                PromoCodeResponseDto.class)
                .withValues(List.of(getBRILLIANT10ActivePromoCodeResponseDto().build(),
                        getPAIN49ActivePromoCodeResponseDto().build(),
                        getPAIN49NotActivePromoCodeResponseDto().build(),
                        getRICE23NotActivePromoCodeResponseDto().build()))
                .build();

        assertThat(actual.currentPage())
                .isZero();
        assertThat(actual.lastPage())
                .isZero();
        assertThat(actual.size())
                .isEqualTo(DEFAULT_PAGEABLE_SIZE);
        assertThat(actual.sort())
                .isEqualTo(UNSORTED);

        assertThat(actual.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
        assertThat(actual.values())
                .hasSize(promoCodesCount);
    }

    @Given("User wants to get details about active promo codes")
    public void prepareInfoForRetrievingAvailablePromoCodes() {
    }

    @When("he performs request with no request parameters to active promo codes url")
    public void sendGetAvailablePromoCodesWithNoRequestParamsRequest() {
        getActivePromoCodesWithNoRequestParamsResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().get(URL_PROMO_CODES_ACTIVE);
    }

    @Then("response should have 200 status, json content type, contain info about {int} active promo codes")
    public void checkGetAvailablePromoCodesWithNoRequestParams(int promoCodesCount) {
        getActivePromoCodesWithNoRequestParamsResponse.then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

        ListContainerResponseDto<PromoCodeResponseDto> actual = getActivePromoCodesWithNoRequestParamsResponse
                .as(new TypeRef<ListContainerResponseDto<PromoCodeResponseDto>>() {
                });

        ListContainerResponseDto<PromoCodeResponseDto> expected = getListContainerForResponse(
                PromoCodeResponseDto.class)
                .withValues(List.of(getBRILLIANT10ActivePromoCodeResponseDto().build(),
                        getPAIN49ActivePromoCodeResponseDto().build()))
                .build();

        assertThat(actual.currentPage())
                .isZero();
        assertThat(actual.lastPage())
                .isZero();
        assertThat(actual.size())
                .isEqualTo(DEFAULT_PAGEABLE_SIZE);
        assertThat(actual.sort())
                .isEqualTo(UNSORTED);

        assertThat(actual.values())
                .containsExactlyInAnyOrderElementsOf(expected.values());
        assertThat(actual.values())
                .hasSize(promoCodesCount);
    }
}
