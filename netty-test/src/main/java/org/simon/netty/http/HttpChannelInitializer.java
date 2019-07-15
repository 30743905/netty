package org.simon.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Administrator
 * @Copyright © 2019 tiger Inc. All rights reserved.
 * @create 2019-04-19 下午 23:42
 * @Description:TODO
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * PooledUnsafeDirectByteBuf -> DefaultHttpRequest+EmptyLastHttpContent
     * -> HttpObjectAggregator$AggregatedFullHttpRequest
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        /**
         * 服务端请求解码  HttpRequestDecoder：把流数据解析为httpRequest
         */
        pipeline.addLast(new HttpRequestDecoder());
        /**
         * HttpServerCodec extends CombinedChannelDuplexHandler<HttpRequestDecoder, HttpResponseEncoder>
         */
        //pipeline.addLast(new HttpServerCodec());
        /**
         * 经过HttpServerCodec解码之后，一个HTTP请求会导致：ParseRequestHandler的 channelRead()方法调用多次（测试时 "received message"输出了两次）
         * 可以用HttpObjectAggregator 将多个消息转换为单一的一个FullHttpRequest
         */
        pipeline.addLast(new HttpObjectAggregator(65536));

        /**
         * 服务端响应编码
         */
        pipeline.addLast(new HttpResponseEncoder());
        /**
         * 块写入处理器
         */
        pipeline.addLast(new ChunkedWriteHandler());


        pipeline.addLast(new MyHttpHandler());
    }
}
