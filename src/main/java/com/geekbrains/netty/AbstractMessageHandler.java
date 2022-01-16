package com.geekbrains.netty;

import com.geekbrains.model.*;
import com.geekbrains.netty.db.DataBaseHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public class AbstractMessageHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    private Path currentPath;
    private Path locationPath;

    public AbstractMessageHandler() {
        currentPath = Paths.get("Server");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new FilesList(currentPath));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                AbstractMessage message) throws Exception {
        log.debug("received: {}", message);
        switch (message.getMessageType()) {
            case FILE_REQUEST:
                FileRequest req = (FileRequest) message;
                ctx.writeAndFlush(
                        new FileMessage(locationPath.resolve(req.getFileName()))
                );
                break;
            case FILE:
                FileMessage fileMessage = (FileMessage) message;
                Files.write(
                        locationPath.resolve(fileMessage.getFileName()),
                        fileMessage.getBytes()
                );
                ctx.writeAndFlush(new FilesList(locationPath));
                break;
            case AUTHENTICATION:
                Authentication ua = (Authentication) message;
                if (DataBaseHandler.getLocation(ua.getLogin(), ua.getPassword()) == null) {
                    log.debug("Not found users");
                    ctx.writeAndFlush(new AuthenticationError("Not found user"));
                } else {
                    locationPath = Paths.get(currentPath + "/" + ua.getLogin());
                    ctx.writeAndFlush(new AuthenticationOK(locationPath.toString()));
                    ctx.writeAndFlush(new FilesList(locationPath));
                    log.debug("User" + ua.getLogin() + " found");
                }
                break;
            case REGISTRATION:
                Registration add = (Registration) message;
                if (DataBaseHandler.getLogin(add.getLogin()) == null) {
                    locationPath = Paths.get(currentPath + "/" + add.getLogin());
                    DataBaseHandler.addNewUser(add.getLogin(), add.getPassword(), locationPath.toString());
                    new File(locationPath.toString()).mkdirs();
                    ctx.writeAndFlush(new AuthenticationOK(locationPath.toString()));
                    ctx.writeAndFlush(new FilesList(Paths.get(Objects.requireNonNull(DataBaseHandler.getLocation(add.getLogin(), add.getPassword())))));
                } else {
                    ctx.writeAndFlush(new RegistrationError("Login is already"));
                    log.debug("Login is already");
                }
            case FOLDER_REQUEST:
                RequestFolder rf = (RequestFolder) message;
                locationPath = Paths.get(rf.getFolder());

//
//                        USER_PASSWORD_CHANGE
        }
    }
}
