package com.geekbrains;

import com.geekbrains.db.DataBaseHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);


    public static void main(String[] args) {
        UserNameService nameService = new UserNameService();
        HandlerProvider provider = new HandlerProvider(nameService, new ContextStoreService());
        EventLoopGroup auth = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            DataBaseHandler.dbConnection();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(auth, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(
                                    provider.getSerializePipeline()
                            );
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
