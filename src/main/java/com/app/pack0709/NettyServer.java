package com.app.pack0709;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;

import java.util.logging.Logger;

public class NettyServer {
    // private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);
    public static void  main(String[] args){
        final Logger logger = Logger.getLogger(NettyServer.class.getName());
        final EventLoopGroup  bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    });
            ChannelFuture f = b.bind(18080).sync();
            f.channel().closeFuture().addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture future) {
                    logger.warning(future.channel().toString()+"close");
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
