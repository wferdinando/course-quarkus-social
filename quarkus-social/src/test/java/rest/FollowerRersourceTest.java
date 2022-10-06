package rest;

import io.github.wferdinando.quarkussocial.domain.model.Follower;
import io.github.wferdinando.quarkussocial.domain.model.User;
import io.github.wferdinando.quarkussocial.domain.repository.FollowerRepository;
import io.github.wferdinando.quarkussocial.domain.repository.UserRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import rest.dto.FollowerRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerRersource.class)
class FollowerRersourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    void setUP() {
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");
        userRepository.persist(user);
        userId = user.getId();

        var follower = new User();
        follower.setAge(30);
        follower.setName("Cicracno");
        userRepository.persist(follower);
        followerId = follower.getId();

        var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);
        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("Should return 409 when followerId is equal to UserId")
    public void sameUserAsFollowerTest() {

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("You can't follower yourself!"));
    }


    @Test
    @DisplayName("Should return 404 on follow a user when UserId doesn't exist")
    public void userNotFoundWhenTryingFollowTest() {

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", inexistentUserId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should follow a user")
    public void followUserTest() {

        var body = new FollowerRequest();
        body.setFollowerId(followerId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 on list user followers and UserId doesn't exist")
    public void userNotFoundWhenListingFollowersTest() {
        var inexistentUserId = 999;
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should list a user's followers")
    public void listingFollowersTest() {

        var response = given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .extract().response();

        var followersCount = response.jsonPath().get("followersCount");
        var followersContent = response.jsonPath().getList("content");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followersContent.size());
    }

    @Test
    @DisplayName("Should return 404 on unfollow user and UserId doesn't exist")
    public void userNotFoundWhenUnfollowingAUserTest() {
        var inexistentUserId = 999;
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
                .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should unfollow an user")
    public void unfollowUserTest() {
        given()
                .pathParam("userId", userId)
                .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}