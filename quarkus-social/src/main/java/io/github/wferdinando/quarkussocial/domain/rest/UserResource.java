package io.github.wferdinando.quarkussocial.domain.rest;

import io.github.wferdinando.quarkussocial.domain.model.User;
import io.github.wferdinando.quarkussocial.domain.repository.UserRepository;
import io.github.wferdinando.quarkussocial.domain.rest.dto.CreateUserRequest;
import io.github.wferdinando.quarkussocial.domain.rest.dto.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {


    private final UserRepository repository;
    private final Validator validator;

    @Inject
    public UserResource(UserRepository repository, Validator validator){
        this.repository = repository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserRequest userRequest) {

        var violations = validator.validate(userRequest);
        if(!violations.isEmpty()){
            return ResponseError.createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setAge(userRequest.getAge());

        repository.persist(user);

        return Response.status(Response.Status.CREATED.getStatusCode())
                .entity(user)
                .build();
    }

    @GET
    public Response listAllUsers() {
        PanacheQuery<User> query = repository.findAll() ;
        return Response.ok(query.list()).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, CreateUserRequest userData) {
        User user = repository.findById(id);

        if (user != null) {
            user.setName(userData.getName());
            user.setAge(userData.getAge());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        User user = repository.findById(id);
        if (user != null) {
            repository.delete(user);
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
