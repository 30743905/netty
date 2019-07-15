package org.simon.netty.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author Administrator
 * @Copyright © 2019 tiger Inc. All rights reserved.
 * @create 2019-04-19 下午 23:42
 * @Description:TODO
 */
public class MyHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private Logger logger = LoggerFactory.getLogger(MyHttpHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        logger.info("*********receive msg:"+request+"<");
        logger.info("content:{}", request.content().toString(CharsetUtil.UTF_8));
        logger.info("decoderResult:{}", request.decoderResult());
        logger.info("headers:{}", request.headers());
        logger.info("method:{}", request.method());
        logger.info("protocolVersion:{}", request.protocolVersion());
        logger.info("uri:{}", request.uri());


        /**
         * fireChannelRead()：将处理后的结果发送到下一个Channel处理
         * writeAndFlush()：writeAndFlush/write是Outbound，它是把消息发送到上一个Handler，进而发送到remote peer
         */
        ctx.writeAndFlush("time:"+System.currentTimeMillis());

        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                Unpooled.wrappedBuffer(("time:"+System.currentTimeMillis()).getBytes()));

        response.headers().set("Content-Type", "application/json");
        response.headers().setInt("content-length",
                response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }
}
