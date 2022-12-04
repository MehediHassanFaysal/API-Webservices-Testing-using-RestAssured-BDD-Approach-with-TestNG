package Azureweb_authors;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class AuthorApiTest {

    public static HashMap map = new HashMap();

    // return list of authors
    @Test(priority = 1)
    void getAllAuthorsInfo()
    {
        given()
           .when()
                .get("https://fakerestapi.azurewebsites.net/api/v1/Authors")
           .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .body("id[1]", equalTo(2))
                .log().all();
    }

    // return author details by idBook

    @Test(priority = 2)
    void getSpecificAuth()
    {
        given()
          .when()
                .get("https://fakerestapi.azurewebsites.net/api/v1/Authors/authors/books/25")
           .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .assertThat()
                .body("idBook[1]", equalTo(25))
                .log().all();
    }


    // store author info. to web
    @Test(priority = 3)
    void storeAuthorDetails()
    {
        map.put("id",AuthRestUtilities.authID());
        map.put("idBook", AuthRestUtilities.authIdBook());
        map.put("firstName", AuthRestUtilities.authFirstName());
        map.put("lastName", AuthRestUtilities.authLastName());

        given()
                .contentType("application/json")
                .body(map)
             .when()
                .post("https://fakerestapi.azurewebsites.net/api/v1/Authors")
             .then()
                .statusCode(200)
                .header("Content-Type", "application/json; charset=utf-8; v=1.0")
                .assertThat()
                .body("idBook", equalTo(100))
                .log().all();

    }

    @BeforeClass
    void dataUpdate()
    {
        map.put("id",AuthRestUtilities.upAuthID());
        map.put("idBook", AuthRestUtilities.upIdBook());
        map.put("firstName", AuthRestUtilities.upAuthFirstName());
        map.put("lastName", AuthRestUtilities.upAuthLastName());

        RestAssured.baseURI = "https://fakerestapi.azurewebsites.net";
        RestAssured.basePath = "/api/v1/Authors/100";
    }

    @Test(priority = 4)
    void updateAuthorInfo()
    {
        given()
                .contentType("application/json")
                .body(map)
           .when()
                .put()
           .then()
                .statusCode(200)
                .header("Content-Type", "application/json; charset=utf-8; v=1.0")
                .assertThat()
                .body("idBook", equalTo(100))
                .log().all();
    }

    @Test(priority = 5)
    void deleteAuthor()
    {
        given()
             .when()
                .delete("https://fakerestapi.azurewebsites.net/api/v1/Authors/101")
             .then()
                .statusCode(200)
                .header("Content-Length", "0")
                .log().all();
    }


}
