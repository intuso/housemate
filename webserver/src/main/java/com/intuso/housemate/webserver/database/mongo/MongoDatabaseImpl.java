package com.intuso.housemate.webserver.database.mongo;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.webserver.database.Database;
import com.intuso.housemate.webserver.database.model.Page;
import com.intuso.housemate.webserver.database.model.User;
import com.intuso.utilities.collection.ManagedCollection;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import com.intuso.utilities.webserver.oauth.model.Authorisation;
import com.intuso.utilities.webserver.oauth.model.Client;
import com.intuso.utilities.webserver.oauth.model.Token;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by tomc on 21/01/17.
 */
public class MongoDatabaseImpl implements Database {

    public final static String HOST = "mongo.host";
    public final static String PORT = "mongo.port";
    public final static String DATABASE = "mongo.database";

    public static void configureDefaults(WriteableMapPropertyRepository defaultProperties) {
        defaultProperties.set(HOST, "localhost");
        defaultProperties.set(PORT, "27017");
        defaultProperties.set(DATABASE, "housemate");
    }

    private final Logger logger = LoggerFactory.getLogger(MongoDatabaseImpl.class);

    private final ManagedCollection<Listener> listeners;

    private final MongoCollection<Document> userCollection;
    private final MongoCollection<Document> clientCollection;
    private final MongoCollection<Document> authorisationCollection;
    private final MongoCollection<Document> tokenCollection;

    private final LoadingCache<String, Optional<User>> userCache;
    private final LoadingCache<String, Optional<Client>> clientCache;

    private final Function<Document, User> toUser;
    private final Function<User, Document> fromUser;
    private final Function<Document, Client> toClient;
    private final Function<Client, Document> fromClient;
    private final Function<Document, Authorisation> toAuthorisation;
    private final Function<Authorisation, Document> fromAuthorisation;
    private final Function<Document, Token> toToken;
    private final Function<Token, Document> fromToken;

    @Inject
    public MongoDatabaseImpl(PropertyRepository properties, ManagedCollectionFactory managedCollectionFactory) {

        listeners = managedCollectionFactory.createSet();

        MongoClient mongoClient = new MongoClient(properties.get(HOST), Integer.parseInt(properties.get(PORT)));

        logger.info("Connecting to mongod at " + mongoClient.getConnectPoint());

        MongoDatabase database = mongoClient.getDatabase(properties.get(DATABASE));

        logger.info("Using database " + database.getName());

        userCollection = database.getCollection("user");
        userCollection.createIndex(new Document().append("email", 1));
        clientCollection = database.getCollection("client");
        clientCollection.createIndex(new Document().append("owner", 1));
        authorisationCollection = database.getCollection("authorisation");
        tokenCollection = database.getCollection("token");
        tokenCollection.createIndex(new Document().append("client", 1));
        tokenCollection.createIndex(new Document().append("user", 1));
        tokenCollection.createIndex(new Document().append("token", 1));
        tokenCollection.createIndex(new Document().append("refresh", 1));

        userCache = CacheBuilder.newBuilder()
                .expireAfterAccess(7, TimeUnit.DAYS)
                .build(new CacheLoader<String, Optional<User>>() {
                    @Override
                    public Optional<User> load(String s) throws Exception {
                        return loadAll(Lists.newArrayList(s)).get(s);
                    }

                    @Override
                    public Map<String, Optional<User>> loadAll(Iterable<? extends String> keys) throws Exception {
                        Map<String, Optional<User>> result = StreamSupport.stream(userCollection.find(in("_id", keys)).spliterator(), false)
                                .map(toUser)
                                .collect(Collectors.toMap(User::getId, Optional::of));
                        for(String key : keys)
                            if(!result.containsKey(key))
                                result.put(key, Optional.empty());
                        return result;
                    }
                });

        clientCache = CacheBuilder.newBuilder()
                .expireAfterAccess(7, TimeUnit.DAYS)
                .build(new CacheLoader<String, Optional<Client>>() {
                    @Override
                    public Optional<Client> load(String s) throws Exception {
                        return loadAll(Lists.newArrayList(s)).get(s);
                    }

                    @Override
                    public Map<String, Optional<Client>> loadAll(Iterable<? extends String> keys) throws Exception {
                        Map<String, Optional<Client>> result = StreamSupport.stream(clientCollection.find(in("_id", keys)).spliterator(), false)
                                .map(toClient)
                                .collect(Collectors.toMap(Client::getId, Optional::of));
                        for(String key : keys)
                            if(!result.containsKey(key))
                                result.put(key, Optional.empty());
                        return result;
                    }
                });

        toUser = document -> document == null ? null : new User(
                document.getString("_id"),
                document.getString("email"),
                document.getString("server"));
        fromUser = user -> user == null ? null : new Document()
                .append("_id", user.getId())
                .append("email", user.getEmail())
                .append("server", user.getServerAddress());
        toClient = document -> document == null ? null : new Client(
                document.getString("owner"),
                document.getString("_id"),
                document.getString("secret"),
                document.getString("name"));
        fromClient = client -> client == null ? null : new Document()
                .append("_id", client.getId())
                .append("secret", client.getSecret())
                .append("owner", client.getOwnerId())
                .append("name", client.getName());
        toAuthorisation = document -> document == null ? null : new Authorisation(
                clientCache.getUnchecked(document.getString("client")).orElse(null),
                document.getString("user"),
                document.getString("_id"));
        fromAuthorisation = authorisation -> authorisation == null ? null : new Document()
                .append("client", authorisation.getClient().getId())
                .append("user", authorisation.getUserId())
                .append("_id", authorisation.getCode());
        toToken = document -> document == null ? null : new Token(
                document.getString("_id"),
                clientCache.getUnchecked(document.getString("client")).orElse(null),
                document.getString("user"),
                document.getString("token"),
                document.getString("token"),
                document.getLong("expires-at"));
        fromToken = token -> token == null ? null : new Document()
                .append("_id", token.getId())
                .append("client", token.getClient().getId())
                .append("user", token.getUserId())
                .append("token", token.getToken())
                .append("refresh", token.getRefreshToken())
                .append("expires-at", token.getExpiresAt());
    }

    @Override
    public Stream<User> getUsers() {
        return StreamSupport.stream(userCollection.find().spliterator(), false)
                .map(toUser);
    }

    @Override
    public Page<User> getUserPage(long offset, int limit) {
        return new Page<>(offset,
                userCollection.count(),
                StreamSupport.stream(userCollection.find().skip((int) offset).limit(limit).spliterator(), false)
                        .map(toUser)
                        .collect(Collectors.toList()));
    }

    @Override
    public void updateUser(User user) {
        userCache.invalidate(user.getId());
        userCollection.updateOne(eq("_id", user.getId()), new Document("$set", fromUser.apply(user)), new UpdateOptions().upsert(true));
        for(Listener listener : listeners)
            listener.userUpdated(user);
    }

    @Override
    public User getUser(String id) {
        return userCache.getUnchecked(id).orElse(null);
    }

    @Override
    public void deleteUser(String id) {
        userCache.invalidate(id);
        userCollection.deleteOne(eq("_id", id));
    }

    @Override
    public User getUserByEmail(String email) {
        return toUser.apply(userCollection.find(and(
                eq("email", email)
        )).limit(1).first());
    }

    @Override
    public void setUserPassword(String id, String passwordHash) {
        userCache.invalidate(id);
        userCollection.updateOne(eq("_id", id), new Document("$set", new Document("passwordHash", passwordHash)), new UpdateOptions().upsert(true));
    }

    @Override
    public boolean authenticateUser(String id, String passwordHash) {
        return userCollection.count(and(
                eq("_id", id),
                eq("passwordHash", passwordHash)
        )) > 0;
    }

    @Override
    public Page<Client> getClientPage(long offset, int limit) {
        return new Page<>(offset,
                clientCollection.count(),
                StreamSupport.stream(clientCollection.find().skip((int) offset).limit(limit).spliterator(), false)
                        .map(toClient)
                        .collect(Collectors.toList()));
    }

    @Override
    public void updateClient(Client client) {
        clientCache.invalidate(client.getId());
        clientCollection.updateOne(eq("_id", client.getId()), new Document("$set", fromClient.apply(client)), new UpdateOptions().upsert(true));
        for(Listener listener : listeners)
            listener.clientUpdated(client);
    }

    @Override
    public Client getClient(String id) {
        return clientCache.getUnchecked(id).orElse(null);
    }

    @Override
    public void deleteClient(String id) {
        clientCache.invalidate(id);
        clientCollection.deleteOne(eq("_id", id));
    }

    @Override
    public void updateAuthorisation(Authorisation authorisation) {
        authorisationCollection.updateOne(eq("_id", authorisation.getCode()), new Document("$set", fromAuthorisation.apply(authorisation)), new UpdateOptions().upsert(true));
    }

    @Override
    public Authorisation getAuthorisation(String code) {
        return toAuthorisation.apply(authorisationCollection.find(eq("_id", code)).first());
    }

    @Override
    public void deleteAuthorisation(String code) {
        authorisationCollection.deleteOne(eq("_id", code));
    }

    @Override
    public void updateToken(Token token) {
        tokenCollection.updateOne(eq("_id", token.getId()), new Document("$set", fromToken.apply(token)), new UpdateOptions().upsert(true));
    }

    @Override
    public Token getToken(String id) {
        return toToken.apply(tokenCollection.find(eq("_id", id)).first());
    }

    @Override
    public void deleteToken(String token) {
        tokenCollection.deleteOne(eq("_id", token));
    }

    @Override
    public Token getTokenForToken(String token) {
        return toToken.apply(tokenCollection.find(eq("token", token)).first());
    }

    @Override
    public Token getTokenForRefreshToken(String refreshToken) {
        return toToken.apply(tokenCollection.find(eq("refresh", refreshToken)).first());
    }

    @Override
    public Page<Token> getUserTokenPage(String id, long offset, int limit) {
        return new Page<>(offset,
                tokenCollection.count(eq("user", id)),
                StreamSupport.stream(tokenCollection.find(eq("user", id)).skip((int) offset).limit(limit).spliterator(), false)
                        .map(toToken)
                        .collect(Collectors.toList()));
    }

    @Override
    public Page<Token> getClientTokenPage(String id, long offset, int limit) {
        return new Page<>(offset,
                tokenCollection.count(eq("client", id)),
                StreamSupport.stream(tokenCollection.find(eq("client", id)).skip((int) offset).limit(limit).spliterator(), false)
                        .map(toToken)
                        .collect(Collectors.toList()));
    }

    @Override
    public ManagedCollection.Registration addListener(Listener listener) {
        return listeners.add(listener);
    }
}
