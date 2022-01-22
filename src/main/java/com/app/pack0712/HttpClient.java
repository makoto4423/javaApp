package com.app.pack0712;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.DefaultPromise;
import org.apache.http.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class HttpClient {

    private Channel channel;

    private void connect() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(Short.MAX_VALUE));
                    }
                });
        ChannelFuture f = b.connect("127.0.0.1", 8888).sync();
        channel = f.channel();
    }

    private HttpResponse blockSend(FullHttpRequest request) throws ExecutionException, InterruptedException {
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        DefaultPromise<HttpResponse> promise = new DefaultPromise<>(channel.eventLoop());
        channel.writeAndFlush(request);
        HttpResponse res = promise.get();
        if(res != null){
            System.out.println("the response is "+ res.getEntity());
        }
        return res;
    }

    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException, ExecutionException {
        HttpClient client = new HttpClient();
        client.connect();
        ByteBuf body = Unpooled.wrappedBuffer("HttpMessage".getBytes(StandardCharsets.UTF_8));
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                "http://127.0.0.1/ttt/a/vv",body);
        HttpResponse response = client.blockSend(request);
    }
}
