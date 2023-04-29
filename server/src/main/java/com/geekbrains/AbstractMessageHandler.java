package com.geekbrains;

import com.geekbrains.db.DataBaseHandler;
import com.geekbrains.message.*;
import com.geekbrains.message.impl.*;
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
        currentPath = Paths.get("storage");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(new FilesList(currentPath));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                AbstractMessage message) throws Exception {
        switch (message.getMessageType()) {
            case FILE_REQUEST:
                FileRequest req = (FileRequest) message;
                ctx.writeAndFlush(new FileMessage(locationPath.resolve(req.getFileName())));
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
                    log.info("Not found users " + ua.getLogin());
                    ctx.writeAndFlush(new AuthenticationError());
                } else {
                    locationPath = Paths.get(currentPath + "/" + ua.getLogin());
                    ctx.writeAndFlush(new AuthenticationOK(locationPath.toString()));
                    ctx.writeAndFlush(new FilesList(locationPath));
                    log.info("User" + ua.getLogin() + " found");
                }
                break;
            case REGISTRATION:
                Registration add = (Registration) message;
                if (DataBaseHandler.getLogin(add.getLogin()) == null) {
                    locationPath = Paths.get(currentPath + "/" + add.getLogin());
                    DataBaseHandler.addNewUser(add.getLogin(), add.getPassword(), locationPath.toString());
                    new File(currentPath + "/" + add.getLogin()).mkdirs();
                    ctx.writeAndFlush(new AuthenticationOK(locationPath.toString()));
                    ctx.writeAndFlush(new FilesList(Paths.get(Objects.requireNonNull(DataBaseHandler.getLocation(add.getLogin(), add.getPassword())))));
                } else {
                    ctx.writeAndFlush(new RegistrationError("Login is already"));
                    log.info("Login is already");
                }
                break;
            case FOLDER_REQUEST:
                RequestFolder rf = (RequestFolder) message;
                locationPath = Paths.get(rf.getFolder());
                break;
            case DELETE_FILE:
                DeleteFile del = (DeleteFile) message;
                Cleaner.delete(new File(String.valueOf(locationPath.resolve(del.getName()))));
                ctx.writeAndFlush(new FilesList(locationPath));
                break;
            case CREATE_FOLDER:
                CreateFolder cr = (CreateFolder) message;
                new File(locationPath.toString() + "/" + cr.getName()).mkdirs();
                ctx.writeAndFlush(new FilesList(locationPath));
                break;
            case CHANGE_PASSWORD:
                ChangePassword ch = (ChangePassword) message;
                if (DataBaseHandler.ChangePass(ch.getNewPassword(), ch.getOldPassword(), ch.getLogin()) > 0) {
                    ctx.writeAndFlush(new ChangePasswordOK());
                    log.info("Password change");
                } else {
                    ctx.writeAndFlush(new ChangePasswordError());
                    log.warn("Password not change");
                }
                break;
            case DELETE_ACCOUNT:
                DeleteAccount delAcc = (DeleteAccount) message;
                if (DataBaseHandler.deleteUser(delAcc.getLogin(), delAcc.getPassword()) > 0) {
                    File file = new File(currentPath + "/" + delAcc.getLogin());
                    Cleaner.delete(file);
                    log.info("Account has been delete");
                    ctx.writeAndFlush(new DeleteAccountOK());
                } else {
                    ctx.writeAndFlush(new DeleteAccountError());
                    log.error("Account not deleted");
                }
        }
    }
}