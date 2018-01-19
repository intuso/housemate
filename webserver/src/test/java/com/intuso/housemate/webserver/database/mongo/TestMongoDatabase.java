package com.intuso.housemate.webserver.database.mongo;

import com.google.common.collect.Maps;
import com.intuso.housemate.webserver.database.model.User;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import com.intuso.utilities.webserver.oauth.model.Authorisation;
import com.intuso.utilities.webserver.oauth.model.Client;
import com.intuso.utilities.webserver.oauth.model.Token;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Created by tomc on 21/01/17.
 */
@Ignore // more of an integration test. Needs mongo running
public class TestMongoDatabase {

    private final ManagedCollectionFactory managedCollectionFactory = new ManagedCollectionFactory() {

        @Override
        public <LISTENER> ManagedCollection<LISTENER> createSet() {
            return new ManagedCollection<>(Collections.synchronizedSet(new HashSet<>()));
        }

        @Override
        public <LISTENER> ManagedCollection<LISTENER> createList() {
            return new ManagedCollection<>(Collections.synchronizedList(new LinkedList<>()));
        }
    };
    private final MongoDatabaseImpl mongoDatabase = new MongoDatabaseImpl(new WriteableMapPropertyRepository(managedCollectionFactory, Maps.newHashMap()), managedCollectionFactory);

    @After
    public void cleanup() {
        mongoDatabase.deleteUser("aUser");
        mongoDatabase.deleteClient("aClient");
        mongoDatabase.deleteAuthorisation("anAuthorisation");
        mongoDatabase.deleteToken("aToken");
    }

    @Test
    public void testCRDUser() {
        User user = new User("aUser", "name@domain.com", "some.server.com:1234");
        mongoDatabase.updateUser(user);
        user = mongoDatabase.getUser("aUser");
        assertNotNull(user);
        assertEquals("aUser", user.getId());
        assertEquals("name@domain.com", user.getEmail());
        assertEquals("some.server.com:1234", user.getServerAddress());
        mongoDatabase.deleteUser("aUser");
        user = mongoDatabase.getUser("aUser");
        assertNull(user);
    }

    @Test
    public void testCRDClient() {
        User user = new User("aUser", "name@domain.com", "some.server.com:1234");
        mongoDatabase.updateUser(user);
        Client client = new Client(user.getId(), "aClient", "someSecret", "A Test Client");
        mongoDatabase.updateClient(client);
        client = mongoDatabase.getClient("aClient");
        assertNotNull(client);
        assertNotNull(client.getOwnerId());
        assertEquals("aUser", client.getOwnerId());
        assertEquals("aClient", client.getId());
        assertEquals("someSecret", client.getSecret());
        assertEquals("A Test Client", client.getName());
        mongoDatabase.deleteClient("aClient");
        client = mongoDatabase.getClient("aClient");
        assertNull(client);
    }

    @Test
    public void testCRDAuthorisation() {
        User user = new User("aUser", "name@domain.com", "some.server.com:1234");
        mongoDatabase.updateUser(user);
        Client client = new Client(user.getId(), "aClient", "someSecret", "A Test Client");
        mongoDatabase.updateClient(client);
        Authorisation authorisation = new Authorisation(client, user.getId(), "anAuthorisation");
        mongoDatabase.updateAuthorisation(authorisation);
        authorisation = mongoDatabase.getAuthorisation("anAuthorisation");
        assertNotNull(authorisation);
        assertNotNull(authorisation.getUserId());
        assertEquals("aUser", authorisation.getUserId());
        assertNotNull(authorisation.getClient());
        assertEquals("aClient", authorisation.getClient().getId());
        assertEquals("anAuthorisation", authorisation.getCode());
        mongoDatabase.deleteAuthorisation("anAuthorisation");
        authorisation = mongoDatabase.getAuthorisation("anAuthorisation");
        assertNull(authorisation);
    }

    @Test
    public void testCRDToken() {
        User user = new User("aUser", "name@domain.com", "some.server.com:1234");
        mongoDatabase.updateUser(user);
        Client client = new Client(user.getId(), "aClient", "someSecret", "A Test Client");
        mongoDatabase.updateClient(client);
        Token token = new Token("aToken", client, user.getId(), "token", "refresh", 123456789);
        mongoDatabase.updateToken(token);
        token = mongoDatabase.getToken("aToken");
        assertNotNull(token);
        assertEquals("aToken", token.getId());
        assertNotNull(token.getUserId());
        assertEquals("aUser", token.getUserId());
        assertNotNull(token.getClient());
        assertEquals("aClient", token.getClient().getId());
        assertEquals("token", token.getToken());
        assertEquals("refresh", token.getRefreshToken());
        assertEquals(123456789L, token.getExpiresAt());
        mongoDatabase.deleteToken("aToken");
        token = mongoDatabase.getToken("aToken");
        assertNull(token);
    }
}
