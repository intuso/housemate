package com.intuso.housemate.webserver.database;

import com.intuso.housemate.webserver.database.model.Page;
import com.intuso.housemate.webserver.database.model.User;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.webserver.oauth.OAuthStore;
import com.intuso.utilities.webserver.oauth.model.Client;
import com.intuso.utilities.webserver.oauth.model.Token;

import java.util.stream.Stream;

/**
 * Created by tomc on 21/01/17.
 */
public interface Database extends OAuthStore {

    // user methods
    Stream<User> getUsers();
    Page<User> getUserPage(long offset, int limit);
    void updateUser(User user);
    User getUser(String id);
    void deleteUser(String id);
    User getUserByEmail(String email);
    void setUserPassword(String id, String passwordHash);
    boolean authenticateUser(String id, String passwordHash);

    // extra client methods
    Page<Client> getClientPage(long offset, int limit);
    void updateClient(Client client);
    void deleteClient(String id);

    // extra token methods
    void deleteToken(String token);
    Page<Token> getUserTokenPage(String id, long offset, int limit);
    Page<Token> getClientTokenPage(String id, long offset, int limit);

    // add a listener for when objects are updated
    ManagedCollection.Registration addListener(Listener listener);

    interface Listener {
        void userUpdated(User user);
        void clientUpdated(Client client);
    }
}
