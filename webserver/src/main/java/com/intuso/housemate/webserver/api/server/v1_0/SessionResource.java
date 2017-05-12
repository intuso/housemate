package com.intuso.housemate.webserver.api.server.v1_0;

import com.intuso.housemate.client.v1_0.api.HousemateException;
import com.intuso.housemate.webserver.database.Database;
import com.intuso.housemate.webserver.SessionUtils;
import com.intuso.housemate.webserver.api.server.v1_0.model.LoginResponse;
import com.intuso.housemate.webserver.api.server.v1_0.model.RegisterResponse;
import com.intuso.housemate.webserver.api.server.v1_0.model.UpdateUserResponse;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by tomc on 21/01/17.
 */
@Path("/")
public class SessionResource {

    private final Database database;

    @Inject
    public SessionResource(Database database) {
        this.database = database;
    }

    @POST
    @Path("/register")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public RegisterResponse register(@FormParam("email") String email,
                                     @FormParam("password") String password,
                                     @Context HttpServletRequest request) throws IOException {

        RegisterResponse response = new RegisterResponse(false, true, false, false);

        // check the email and password are valid format
        if(email != null && email.length() > 0) // todo check format too?
            response.setValidEmail(true);
        if(password != null && password.length() > 0)
            response.setValidPassword(true);

        // if so ...
        if(response.isValidEmail() && response.isValidPassword()) {

            // check if there's already a user with that email address
            com.intuso.housemate.webserver.database.model.User user = database.getUserByEmail(email);
            response.setAlreadyRegistered(user != null);

            // if there's not, create that user, and create them a session so they're logged in
            if(!response.isAlreadyRegistered()) {
                user = new com.intuso.housemate.webserver.database.model.User(UUID.randomUUID().toString(), email, null);
                database.updateUser(user);
                database.setUserPassword(user.getId(), password);
                HttpSession session = request.getSession(true);
                SessionUtils.setUser(session, user);
                com.intuso.utilities.webserver.oauth.SessionUtils.setUserId(session, user.getId());
                session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 1 week
                response.setSuccess(true);
            }
        }

        return response;
    }

    @POST
    @Path("/login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    public LoginResponse login(@FormParam("email") String email,
                               @FormParam("password") String password,
                               @Context HttpServletRequest request) throws IOException {

        LoginResponse response = new LoginResponse(false, false, false, false);

        // check the email and password are valid format
        if(email != null && email.length() > 0) // todo check format too?
            response.setValidEmail(true);
        if(password != null && password.length() > 0)
            response.setValidPassword(true);

        // if so ....
        if(response.isValidEmail() && response.isValidPassword()) {

            // check a user with that email exists
            com.intuso.housemate.webserver.database.model.User user = database.getUserByEmail(email);
            response.setKnownEmail(user != null);

            // if there is, check their password
            if (response.isKnownEmail()) {
                response.setCorrectPassword(database.authenticateUser(user.getId(), password));

                // if it's all good, then create them a session
                if(response.isCorrectPassword()) {
                    HttpSession session = request.getSession(true);
                    SessionUtils.setUser(session, user);
                    com.intuso.utilities.webserver.oauth.SessionUtils.setUserId(session, user.getId());
                    session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 1 week
                }
            }
        }

        return response;
    }

    @POST
    @Path("/logout")
    public void logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session != null)
            session.invalidate();
    }

    @GET
    @Path("/currentuser")
    @Consumes("application/json")
    @Produces("application/json")
    public com.intuso.housemate.webserver.api.server.v1_0.model.User getCurrentUser(@Context HttpServletRequest request) {
        return com.intuso.housemate.webserver.api.server.v1_0.model.User.from(SessionUtils.getUser(request.getSession()));
    }

    @POST
    @Path("/currentuser")
    @Consumes("application/json")
    public UpdateUserResponse saveCurrentUser(com.intuso.housemate.webserver.api.server.v1_0.model.User user, @Context HttpServletRequest request) {

        // check it's the correct user!
        com.intuso.housemate.webserver.database.model.User sessionUser = SessionUtils.getUser(request.getSession());
        if (user.getId() != null && !user.getId().equals(sessionUser.getId()))
            throw new HousemateException("The user id doesn't match the logged in user's id");

        UpdateUserResponse response = new UpdateUserResponse(false, true, false);

        // check the email and password are valid format
        if(user.getEmail() != null && user.getEmail().length() > 0) // todo check format too?
            response.setValidEmail(true);
        if(user.getServerAddress() != null && user.getServerAddress().length() > 0)
            response.setValidServerAddress(true);

        // if so ....
        if(response.isValidEmail() && response.isValidServerAddress()) {

            // if the email to update is changed, check it's not already registered
            if(!user.getEmail().equals(sessionUser.getEmail())) {
                com.intuso.housemate.webserver.database.model.User emailUser = database.getUserByEmail(user.getEmail());
                response.setAlreadyRegistered(emailUser != null);
            } else
                response.setAlreadyRegistered(false);

            // if we're all good, update the session user and save it
            if(!response.isAlreadyRegistered()) {
                if (user.getEmail() != null)
                    sessionUser.setEmail(user.getEmail());
                if (user.getServerAddress() != null)
                    sessionUser.setServerAddress(user.getServerAddress());
                database.updateUser(sessionUser);
            }
        }

        return response;
    }
}
