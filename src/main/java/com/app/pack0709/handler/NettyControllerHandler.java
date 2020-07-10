package com.app.pack0709.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.apache.http.entity.ContentType;

import java.util.logging.Logger;

// HttpHeaderNames.CONTENT_LENGTH,需设置此属性，否则客户端无法得知报文接收完毕
public class NettyControllerHandler extends ChannelInboundHandlerAdapter {

    Logger logger = Logger.getLogger(NettyControllerHandler.class.getName());

    // msg只能获取到请求，如果数据放body里面无法获取
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("receive msg" + msg);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.wrappedBuffer("I got it".getBytes("UTF-8")));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/json; charset=UTF-8")
                            .set(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
        // ctx.fireChannelRead(msg);
        ctx.writeAndFlush(response);
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // ctx.flush();
        logger.info("read over");
    }

}
