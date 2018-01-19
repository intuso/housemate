package com.intuso.housemate.webserver.api.server.v1_0;

import com.google.common.collect.Lists;
import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.view.CommandView;
import com.intuso.housemate.client.v1_0.api.object.view.ServerView;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.api.type.ObjectReference;
import com.intuso.housemate.client.v1_0.proxy.ProxyRemoveable;
import com.intuso.housemate.client.v1_0.proxy.ProxyRenameable;
import com.intuso.housemate.client.v1_0.proxy.ProxyRunnable;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyCommand;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyServer;
import com.intuso.housemate.client.v1_0.rest.ObjectResource;
import com.intuso.housemate.webserver.SessionUtils;
import com.intuso.utilities.collection.ManagedCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import java.util.List;

/**
 * Created by tomc on 23/01/17.
 */
public class ObjectResourceImpl implements ObjectResource {

    private final static Logger logger = LoggerFactory.getLogger(ObjectResourceImpl.class);

    private final Listeners listeners;

    @Context private HttpServletRequest request;

    @Inject
    public ObjectResourceImpl(Listeners listeners) {
        this.listeners = listeners;
    }

    @Override
    public Object.Data get(String path) {
        if(path == null)
            throw new BadRequestException("No path specified");
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path, false);
        if(object == null)
            throw new NotFoundException();
        return object.getData();
    }

    @Override
    public void delete(String path) {
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path);
        if(object == null)
            throw new NotFoundException();
        else if(object instanceof ProxyRemoveable) {
            try {
                ProxyRemoveable<ProxyCommand<?, ?, ?>> removeable = (ProxyRemoveable<ProxyCommand<?, ?, ?>>) object;
                removeable.loadRemoveCommand(new CommandView(View.Mode.ANCESTORS));
                removeable.getRemoveCommand().performSync(10000L);
            } catch (InterruptedException e) {
                throw new BadRequestException("Failed to wait for remove command to complete", e);
            }
        } else
            throw new BadRequestException(path + " does not represent a removeable object");
    }

    @Override
    public Tree view(ServerView view) {
        ProxyServer.Simple server = SessionUtils.getServer(request.getSession());
        LoadingReferenceHandler referenceHandler = new LoadingReferenceHandler(server);
        Tree result = server.getTree(view, referenceHandler, listeners.getListener(request.getSession(true).getId()), listeners.getListenerRegistrations(request.getSession().getId()));
        referenceHandler.mergeInto(result);
        return result;
    }

    @Override
    public void rename(String path, String newName) {
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path);
        if(object == null)
            throw new NotFoundException();
        else if(object instanceof ProxyRenameable) {
            try {
                ProxyRenameable<ProxyCommand<?, ?, ?>> renameable = (ProxyRenameable<ProxyCommand<?, ?, ?>>) object;
                renameable.loadRenameCommand(new CommandView(View.Mode.ANCESTORS));
                Type.InstanceMap values = new Type.InstanceMap();
                values.getChildren().put("name", new Type.Instances(new Type.Instance(newName)));
                renameable.getRenameCommand().performSync(values, 10000L);
            } catch (InterruptedException e) {
                throw new BadRequestException("Failed to wait for rename command to complete", e);
            }
        } else
            throw new BadRequestException(path + " does not represent a renameable object");
    }

    @Override
    public void start(String path) {
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path);
        if(object == null)
            throw new NotFoundException();
        else if(object instanceof ProxyRunnable) {
            try {
                ProxyRunnable<ProxyCommand<?, ?, ?>, ?> runnable = (ProxyRunnable<ProxyCommand<?, ?, ?>, ?>) object;
                runnable.loadStartCommand(new CommandView(View.Mode.ANCESTORS));
                runnable.getStartCommand().performSync(10000L);
            } catch (InterruptedException e) {
                throw new BadRequestException("Failed to wait for start command to complete", e);
            }
        } else
            throw new BadRequestException(path + " does not represent a runnable object");
    }

    @Override
    public void stop(String path) {
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path);
        if(object == null)
            throw new NotFoundException();
        else if(object instanceof ProxyRunnable) {
            try {
                ProxyRunnable<ProxyCommand<?, ?, ?>, ?> runnable = (ProxyRunnable<ProxyCommand<?, ?, ?>, ?>) object;
                runnable.loadStopCommand(new CommandView(View.Mode.ANCESTORS));
                runnable.getStopCommand().performSync(10000L);
            } catch (InterruptedException e) {
                throw new BadRequestException("Failed to wait for start command to complete", e);
            }
        } else
            throw new BadRequestException(path + " does not represent a runnable object");
    }

    @Override
    public void perform(String path, Type.InstanceMap values) {
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path);
        if(object == null)
            throw new NotFoundException();
        else if(object instanceof ProxyCommand) {
            try {
                ProxyCommand<?, ?, ?> command = (ProxyCommand<?, ?, ?>) object;
                command.performSync(values, 10000L);
            } catch (InterruptedException e) {
                throw new BadRequestException("Failed to wait for start command to complete", e);
            }
        } else
            throw new BadRequestException(path + " does not represent a command");
    }

    private class LoadingReferenceHandler implements Tree.ReferenceHandler {

        private final ProxyServer.Simple server;
        private final List<LoadedReference> loadedReferences = Lists.newArrayList();

        private LoadingReferenceHandler(ProxyServer.Simple server) {
            this.server = server;
        }

        @Override
        public void handle(String path, View view, Tree.ReferenceHandler referenceHandler, Tree.Listener listener, List<ManagedCollection.Registration> listenerRegistrations) {
            ObjectReference reference = server.reference(path);
            if(reference.getObject() != null)
                loadedReferences.add(new LoadedReference(path, reference.getObject().getTree(view, referenceHandler, listener, listenerRegistrations)));
        }

        private void mergeInto(Tree tree) {
            for(LoadedReference loadedReference : loadedReferences)
                loadedReference.mergeInto(tree);
        }

        private class LoadedReference {

            private final String path;
            private final Tree tree;

            private LoadedReference(String path, Tree tree) {
                this.path = path;
                this.tree = tree;
            }

            private void mergeInto(Tree tree) {
                merge(find(tree, path.split("\\."), 0), this.tree);
            }

            private Tree find(Tree tree, String[] path, int pathIndex) {
                if(pathIndex == path.length)
                    return tree;
                if(!tree.getChildren().containsKey(path[pathIndex]))
                    tree.getChildren().put(path[pathIndex], new Tree());
                return find(tree.getChildren().get(path[pathIndex]), path, pathIndex + 1);
            }

            private void merge(Tree into, Tree from) {
                if(from.getData() != null)
                    into.setData(from.getData());
                for(String key : from.getChildren().keySet()) {
                    if(into.getChildren().containsKey(key))
                        merge(into.getChildren().get(key), from.getChildren().get(key));
                    else
                        into.getChildren().put(key, from.getChildren().get(key));
                }
            }
        }
    }
}
