package com.geekbrains;

import com.geekbrains.db.DataBaseHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);


    public static void main(String[] args) {
        EventLoopGroup auth = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            DataBaseHandler.dbConnection();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(auth, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new ObjectDecoder(50 * 1024 * 1024,
                                            ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new AbstractMessageHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(8189).sync();
            logger.info("Server started...");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.warn("USER Disconnect");
        } finally {
            auth.shutdownGracefully();
            worker.shutdownGracefully();
            DataBaseHandler.closeConnection();
        }
    }
}
