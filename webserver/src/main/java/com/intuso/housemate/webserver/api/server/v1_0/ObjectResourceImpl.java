package com.intuso.housemate.webserver.api.server.v1_0;

import com.intuso.housemate.client.v1_0.api.object.Object;
import com.intuso.housemate.client.v1_0.api.object.Tree;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.api.object.view.CommandView;
import com.intuso.housemate.client.v1_0.api.object.view.View;
import com.intuso.housemate.client.v1_0.proxy.ProxyRemoveable;
import com.intuso.housemate.client.v1_0.proxy.ProxyRenameable;
import com.intuso.housemate.client.v1_0.proxy.ProxyRunnable;
import com.intuso.housemate.client.v1_0.proxy.object.ProxyCommand;
import com.intuso.housemate.client.v1_0.rest.ObjectResource;
import com.intuso.housemate.webserver.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;

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
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path.split("/"), false);
        if(object == null)
            throw new NotFoundException();
        return object.getData();
    }

    @Override
    public void delete(String path) {
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path.split("/"));
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
    public Tree view(String path, View view) {
        if(path == null)
            throw new BadRequestException("No path specified");
        Object object = SessionUtils.getServer(request.getSession()).find(path.split("/"), false);
        if(object == null)
            throw new NotFoundException();
        return object.getTree(view, listeners.getListener(request.getSession(true).getId()), listeners.getListenerRegistrations(request.getSession().getId()));
    }

    @Override
    public void rename(String path, String newName) {
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path.split("/"));
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
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path.split("/"));
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
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path.split("/"));
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
        Object<?, ?, ?> object = SessionUtils.getServer(request.getSession()).find(path.split("/"));
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
}
