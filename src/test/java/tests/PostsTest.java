package tests;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import rest.config.ApiConfig;
import rest.model.Post;

import java.util.HashSet;
import java.util.Set;

public class PostsTest {

    private static final String GET_POSTS_ENDPOINT = "/posts";


    @DataProvider
    public static Object[][] userPostsData() {
        return new Object[][]{
                {5, 10},
                {7, 10},
                {9, 10}
        };
    }

    @Test(dataProvider = "userPostsData")
    public void Task3(int userId, int expectedPostCount) {

        //Make the request and get the response
        Response response = ApiConfig.getRequestSpec()
                .when()
                .get(GET_POSTS_ENDPOINT)
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 200, "Expected response 200. Actual " + response.getStatusCode());

        //Parse the response to Post objects
        Post[] posts = response.getBody().as(Post[].class);

        int actualPostCount = 0;

        //Count the number of posts for specific user
        for(Post post : posts){

            if(post.getUserId() == userId){
                actualPostCount++;
            }
        }

        Assert.assertEquals(actualPostCount, expectedPostCount, "\r\nExpected Posts " + expectedPostCount + "\r\nActual Posts " + actualPostCount);
    }

    @Test
    public void Task4(){

        Response response = ApiConfig.getRequestSpec()
                .when()
                .get(GET_POSTS_ENDPOINT)
                .then()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 200, "Expected response 200. Actual " + response.getStatusCode());

        //Parse the response to Post objects
        Post[] posts = response.getBody().as(Post[].class);

        Set<Integer> ids = new HashSet<>();

        //Validate each post has different ID
        for(Post post : posts){
            //If add returns false it means it already has the id in the collection
            if(!ids.add(post.getId())){
                throw new AssertionError("Duplicate Post ID " + post.getId());
            }
        }
    }


}
