package com.app.pack0709;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;

import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED;

// 服务端只能是netty，springboot无法拦截
public class HttpClient {

    public static void main(String[] args) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpRequestEncoder())
                                    .addLast(new HttpResponseDecoder());
                        }
                    });
            ChannelFuture f = b.connect("127.0.0.1", 8081).sync();
            URI uri = new URI("http://127.0.0.1:8081");
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.POST,uri.toASCIIString(),Unpooled.wrappedBuffer("makoto".getBytes()));
            request.headers().set(HttpHeaderNames.CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
                    .set(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes())
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderNames.CONNECTION)
                    .set(HttpHeaderNames.HOST,"127.0.0.1");
            f.channel().writeAndFlush(request);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
