package com.ejada.automation.api;

import com.ejada.automation.utils.ConfigReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class SimpleBooksApiTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private String accessToken;
    private int availableBookId;
    private String createdOrderId;
    private final String initialCustomerName = "Mayar Elhakim";
    private final String updatedCustomerName = "Mayar Mohammed";

    @BeforeClass
    public void setUpApi() {
        RestAssured.baseURI = ConfigReader.getProperty("apiBaseUrl");
    }

    @Test(priority = 1, description = "GET /status - Verify Simple Books API status is OK")
    public void testGetApiStatus() {
        Response response = given()
                .when()
                .get("/status")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String status = response.jsonPath().getString("status");
        Assert.assertEquals(status, "OK", "Expected API status to be 'OK'");
    }

    @Test(priority = 2, description = "GET /books - Retrieve list of available books")
    public void testGetBooksList() {
        Response response = given()
                .queryParam("type", "fiction")
                .when()
                .get("/books")
                .then()
                .statusCode(200)
                .extract()
                .response();

        int bookCount = response.jsonPath().getList("$").size();
        Assert.assertTrue(bookCount > 0, "Books list should not be empty");

        // Select the first available book ID for our order test
        availableBookId = response.jsonPath().getInt("[0].id");
        Assert.assertTrue(availableBookId > 0, "Valid book ID should be returned");
    }

    @Test(priority = 3, description = "POST /api-clients - Register client and obtain Bearer token")
    public void testRegisterApiClient() {
        String uniqueEmail = "mayar." + System.currentTimeMillis() + "@example.com";

        ObjectNode payload = mapper.createObjectNode();
        payload.put("clientName", "EjadaAutomationClient");
        payload.put("clientEmail", uniqueEmail);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload.toString())
                .when()
                .post("/api-clients/")
                .then()
                .statusCode(201)
                .extract()
                .response();

        accessToken = response.jsonPath().getString("accessToken");
        Assert.assertNotNull(accessToken, "Access token must not be null");
        Assert.assertFalse(accessToken.isBlank(), "Access token must not be empty");
    }

    @Test(priority = 4, dependsOnMethods = {"testGetBooksList", "testRegisterApiClient"},
            description = "POST /orders - Submit a new book order")
    public void testSubmitOrder() {
        ObjectNode orderPayload = mapper.createObjectNode();
        orderPayload.put("bookId", availableBookId);
        orderPayload.put("customerName", initialCustomerName);

        Response response = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .body(orderPayload.toString())
                .when()
                .post("/orders")
                .then()
                .statusCode(201)
                .extract()
                .response();

        boolean created = response.jsonPath().getBoolean("created");
        createdOrderId = response.jsonPath().getString("orderId");

        Assert.assertTrue(created, "Order 'created' flag should be true");
        Assert.assertNotNull(createdOrderId, "Generated orderId must not be null");
    }

    @Test(priority = 5, dependsOnMethods = "testSubmitOrder",
            description = "GET /orders/{orderId} - Verify created order details")
    public void testGetOrderById() {
        Response response = given()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("orderId", createdOrderId)
                .when()
                .get("/orders/{orderId}")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("id"), createdOrderId);
        Assert.assertEquals(response.jsonPath().getInt("bookId"), availableBookId);
        Assert.assertEquals(response.jsonPath().getString("customerName"), initialCustomerName);
    }

    @Test(priority = 6, dependsOnMethods = "testGetOrderById",
            description = "PATCH /orders/{orderId} - Update customerName on existing order")
    public void testUpdateOrderCustomerName() {
        ObjectNode patchPayload = mapper.createObjectNode();
        patchPayload.put("customerName", updatedCustomerName);

        // 1. Send PATCH request to update customerName
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .pathParam("orderId", createdOrderId)
                .body(patchPayload.toString())
                .when()
                .patch("/orders/{orderId}")
                .then()
                .statusCode(204);

        // 2. Verify via GET that customerName was actually updated on the server
        Response getResponse = given()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("orderId", createdOrderId)
                .when()
                .get("/orders/{orderId}")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(getResponse.jsonPath().getString("customerName"), updatedCustomerName,
                "Customer name should be updated after PATCH request");
    }

    @Test(priority = 7, dependsOnMethods = "testUpdateOrderCustomerName",
            description = "DELETE /orders/{orderId} - Delete the order and confirm removal")
    public void testDeleteOrder() {
        // 1. Send DELETE request
        given()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("orderId", createdOrderId)
                .when()
                .delete("/orders/{orderId}")
                .then()
                .statusCode(204);

        // 2. Verify via GET that the deleted order now returns 404 Not Found
        given()
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("orderId", createdOrderId)
                .when()
                .get("/orders/{orderId}")
                .then()
                .statusCode(404);
    }
}