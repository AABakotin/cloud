package com.geekbrains;

import com.geekbrains.db.DataBaseHandler;
import com.geekbrains.message.AbstractMessage;
import com.geekbrains.message.impl.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
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
                commandFileRequest(ctx, (FileRequest) message);
                break;
            case FILE:
                commandFile(ctx, (FileMessage) message);
                break;
            case AUTHENTICATION:
                commandAuthentication(ctx, (Authentication) message);
                break;
            case REGISTRATION:
                commandRegistration(ctx, (Registration) message);
                break;
            case FOLDER_REQUEST:
                commandFolderRequests((RequestFolder) message);
                break;
            case DELETE_FILE:
                commandDeleteFile(ctx, (DeleteFile) message);
                break;
            case CREATE_FOLDER:
                commandCreateFolder(ctx, (CreateFolder) message);
                break;
            case CHANGE_PASSWORD:
                commandChangePassword(ctx, (ChangePassword) message);
                break;
            case DELETE_ACCOUNT:
                commandDeleteAccount(ctx, (DeleteAccount) message);
        }
    }

    private void commandDeleteAccount(ChannelHandlerContext ctx, DeleteAccount message) {
        if (DataBaseHandler.deleteUser(message.getLogin(), message.getPassword()) > 0) {
            File file = new File(currentPath + "/" + message.getLogin());
            Cleaner.delete(file);
            log.info("Account has been delete");
            ctx.writeAndFlush(new DeleteAccountOK());
        } else {
            ctx.writeAndFlush(new DeleteAccountError());
            log.error("Account not deleted");
        }
    }

    private static void commandChangePassword(ChannelHandlerContext ctx, ChangePassword message) {
        if (DataBaseHandler.ChangePass(message.getNewPassword(), message.getOldPassword(), message.getLogin()) > 0) {
            ctx.writeAndFlush(new ChangePasswordOK());
            log.info("Password change");
        } else {
            ctx.writeAndFlush(new ChangePasswordError());
            log.warn("Password not change");
        }
    }

    private void commandCreateFolder(ChannelHandlerContext ctx, CreateFolder message) throws IOException {
        new File(locationPath.toString() + "/" + message.getName()).mkdirs();
        ctx.writeAndFlush(new FilesList(locationPath));
    }

    private void commandDeleteFile(ChannelHandlerContext ctx, DeleteFile message) throws IOException {
        Cleaner.delete(new File(String.valueOf(locationPath.resolve(message.getName()))));
        ctx.writeAndFlush(new FilesList(locationPath));
    }

    private void commandFolderRequests(RequestFolder message) {
        locationPath = Paths.get(message.getFolder());
    }

    private void commandRegistration(ChannelHandlerContext ctx, Registration message) throws IOException {
        if (DataBaseHandler.getLogin(message.getLogin()) == null) {
            locationPath = Paths.get(currentPath + "/" + message.getLogin());
            DataBaseHandler.addNewUser(message.getLogin(), message.getPassword(), locationPath.toString());
            new File(currentPath + "/" + message.getLogin()).mkdirs();
            ctx.writeAndFlush(new AuthenticationOK(locationPath.toString()));
            ctx.writeAndFlush(new FilesList(Paths.get(Objects.requireNonNull(DataBaseHandler.getLocation(message.getLogin(), message.getPassword())))));
        } else {
            ctx.writeAndFlush(new RegistrationError("Login is already"));
            log.info("Login is already");
        }
    }

    private void commandAuthentication(ChannelHandlerContext ctx, Authentication message) throws IOException {
        if (DataBaseHandler.getLocation(message.getLogin(), message.getPassword()) == null) {
            log.info("Not found users " + message.getLogin());
            ctx.writeAndFlush(new AuthenticationError());
        } else {
            locationPath = Paths.get(currentPath + "/" + message.getLogin());
            ctx.writeAndFlush(new AuthenticationOK(locationPath.toString()));
            ctx.writeAndFlush(new FilesList(locationPath));
            log.info("User" + message.getLogin() + " found");
        }
    }

    private void commandFile(ChannelHandlerContext ctx, FileMessage message) throws IOException {
        Files.write(
                locationPath.resolve(message.getFileName()),
                message.getBytes()
        );
        ctx.writeAndFlush(new FilesList(locationPath));
    }

    private void commandFileRequest(ChannelHandlerContext ctx, FileRequest message) throws IOException {
        ctx.writeAndFlush(new FileMessage(locationPath.resolve(message.getFileName())));
    }
}