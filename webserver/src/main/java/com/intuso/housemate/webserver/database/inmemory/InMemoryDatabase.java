package com.intuso.housemate.webserver.database.inmemory;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.intuso.housemate.webserver.database.Database;
import com.intuso.housemate.webserver.database.model.Page;
import com.intuso.housemate.webserver.database.model.User;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.webserver.oauth.model.Authorisation;
import com.intuso.utilities.webserver.oauth.model.Client;
import com.intuso.utilities.webserver.oauth.model.Token;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tomc on 21/01/17.
 */
public  class InMemoryDatabase implements Database {

    private final ManagedCollection<Listener> listeners;

    private final TreeMap<String, User> users = Maps.newTreeMap();
    private final Map<String, String> userPasswordHashes = Maps.newHashMap();
    private final TreeMap<String, Client> clients = Maps.newTreeMap();
    private final Map<String, Authorisation> authorisations = Maps.newHashMap();
    private final Map<String, Token> tokens = Maps.newHashMap();

    @Inject
    public InMemoryDatabase(ManagedCollectionFactory managedCollectionFactory) {
        listeners = managedCollectionFactory.create();
    }

    @Override
    public Stream<User> getUsers() {
        return users.values().stream();
    }

    @Override
    public Page<User> getUserPage(long offset, int limit) {
        return page(users.values().stream(), offset, limit, users.size());
    }

    @Override
    public User getUser(String id) {
        return users.get(id);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
        for(Listener listener : listeners)
            listener.userUpdated(user);
    }

    @Override
    public void deleteUser(String id) {
        users.remove(id);
        userPasswordHashes.remove(id);
    }

    @Override
    public User getUserByEmail(String email) {
        for(User user : users.values())
            if(user.getEmail().equals(email))
                return user;
        return null;
    }

    @Override
    public void setUserPassword(String id, String passwordHash) {
        userPasswordHashes.put(id, passwordHash);
    }

    @Override
    public boolean authenticateUser(String id, String passwordHash) {
        return userPasswordHashes.containsKey(id) && userPasswordHashes.get(id).equals(passwordHash);
    }

    @Override
    public Page<Client> getClientPage(long offset, int limit) {
        return page(clients.values().stream(), offset, limit, clients.size());
    }

    @Override
    public Client getClient(String id) {
        return clients.get(id);
    }

    @Override
    public void updateClient(Client client) {
        clients.put(client.getId(), client);
        for(Listener listener : listeners)
            listener.clientUpdated(client);
    }

    @Override
    public void deleteClient(String id) {
        clients.remove(id);
    }

    @Override
    public void updateAuthorisation(Authorisation authorisation) {
        authorisations.put(authorisation.getCode(), authorisation);
    }

    @Override
    public Authorisation getAuthorisation(String code) {
        return authorisations.get(code);
    }

    @Override
    public void deleteAuthorisation(String code) {
        authorisations.remove(code);
    }

    @Override
    public void updateToken(Token token) {
        tokens.put(token.getId(), token);
    }

    @Override
    public Token getToken(String id) {
        return tokens.get(id);
    }

    @Override
    public void deleteToken(String token) {
        tokens.remove(token);
    }

    @Override
    public Token getTokenForToken(String token) {
        for(Token t : tokens.values())
            if(t.getToken().equals(token))
                return t;
        return null;
    }

    @Override
    public Token getTokenForRefreshToken(String refreshToken) {
        for(Token token : tokens.values())
            if(token.getRefreshToken().equals(refreshToken))
                return token;
        return null;
    }

    @Override
    public Page<Token> getUserTokenPage(String id, long offset, int limit) {
        return page(tokens.values().stream().filter(token -> token.getUserId().equals(id)), offset, limit, 0);
    }

    @Override
    public Page<Token> getClientTokenPage(String id, long offset, int limit) {
        return page(tokens.values().stream().filter(token -> token.getClient().getId().equals(id)), offset, limit, 0);
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener) {
        return listeners.add(listener);
    }

    private <T> Page<T> page(Stream<T> stream, long offset, int limit, long total) {
        if(offset > 0)
            stream = stream.skip(offset);
        if(limit >= 0)
            stream = stream.limit(limit);
        return new Page<>(offset, total, stream.collect(Collectors.toList()));
    }
}
