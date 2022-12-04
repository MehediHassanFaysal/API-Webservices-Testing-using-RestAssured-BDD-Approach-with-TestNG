package RestAssured_In_BDD;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Azurewebsites {

    public static HashMap map = new HashMap();

    @Test(priority = 1)
    void Get_all_activities()
    {
        given()
            .when()
                .get("https://fakerestapi.azurewebsites.net/api/v1/Activities")
            .then()
                .statusCode(200)
                .header("Content-Type", "application/json; charset=utf-8; v=1.0")
                .statusLine("HTTP/1.1 200 OK")
                .assertThat()
                .body("title[1]", equalTo("Activity 2"))
                .and()
                .body("title[0,1,2]", hasItems("Activity 2", "Activity 1", "Activity 3"))
                .log().all();
    }


    @Test(priority = 2)
    void getSpecificActInfo()
    {
        given()
           .when()
                .get("https://fakerestapi.azurewebsites.net/api/v1/Activities/10")
           .then()
                .statusCode(200)
                .header("Content-Type", "application/json; charset=utf-8; v=1.0")
                .statusLine("HTTP/1.1 200 OK")
                .assertThat()
                .body("title", equalTo("Activity 10"))
                .and()
                .body("completed", equalTo(true))
                .log().body();
    }



    @BeforeClass
    void postData()
    {
        map.put("title", RestUtilities.getTitle());
        map.put("completed", RestUtilities.getCompleted());

        RestAssured.baseURI = "https://fakerestapi.azurewebsites.net";
        RestAssured.basePath = "/api/v1/Activities";
    }
    @Test(priority = 3)
    void postActInfo()
    {
        given()
                .contentType("application/json")
                .body(map)
            .when()
                .post()
            .then()
                .statusCode(200)
                //.statusCode(405)
                .statusLine("HTTP/1.1 200 OK")
                .assertThat()
                .header("Content-Type", "application/json; charset=utf-8; v=1.0")
                .and()
                .header("Server", "Kestrel")
                .and()
                .body("title", equalTo("RestAPI"))
                .log().headers();

    }


//    @BeforeClass
//    void updateData()
//    {
//        map.put("title", RestUtilities.updateTitle());
//        map.put("completed", RestUtilities.updateCompleted());
//
//        RestAssured.baseURI = "https://fakerestapi.azurewebsites.net";
//        RestAssured.basePath = "/api/v1/Activities/10";
//    }

    @Test(priority = 4)
    void updateActInfo()
    {
        map.put("title", RestUtilities.updateTitle());
        map.put("completed", RestUtilities.updateCompleted());


        given()
                .contentType("application/json")
                .body(map)
           .when()
                .put("https://fakerestapi.azurewebsites.net/api/v1/Activities/10")
           .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .assertThat()
                .header("Content-Type", "application/json; charset=utf-8; v=1.0")
                .and()
                .header("Server", "Kestrel")
                .and()
                .body("title", equalTo("RestASSURED"))
                .and()
                .body("completed", equalTo(false))
                .log().all();
    }


//    @Test(priority = 5)
//    void deleteSpecificAct()
//    {
//        given()
//            .when()
//                .delete("https://fakerestapi.azurewebsites.net/api/v1/Activities/9")
//            .then()
//                .statusCode(200)
//                .log().all();
//    }
}
