package com.geekbrains;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StringMessageHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(StringMessageHandler.class);
    private final UserNameService nameService;
    private final ContextStoreService contextStoreService;
    private String name;

    public StringMessageHandler(UserNameService nameService,
                                ContextStoreService contextStoreService) {
        this.nameService = nameService;
        this.contextStoreService = contextStoreService;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client connected");
        nameService.userConnect();
        contextStoreService.registerContext(ctx);
        name = nameService.getUserName();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        logger.info("received: {}", s);
        for (ChannelHandlerContext context : contextStoreService.getContexts()) {
            context.writeAndFlush(name + ": " + s);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client disconnected");
        nameService.userDisconnect();
        contextStoreService.removeContext(ctx);
    }
}
