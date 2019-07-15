package org.simon.netty.chat;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class MyChatClient {

    public static void main(String[] args) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).
                    handler(new MyChatClientInitializer());

            Channel channel = bootstrap.connect("localhost", 8899).sync().channel();
            /*ChannelFuture future = bootstrap.connect("localhost", 8899);
            future.addListener(new ChannelFutureListener() {//注册一个ChannelFutureListener，以便在操作完成时获得通知
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("hahah");
                        ByteBuf buffer = Unpooled.copiedBuffer("Hello", Charset.defaultCharset());
                        future.channel().writeAndFlush(buffer);
                    }else{
                        Throwable cause = future.cause();
                        cause.printStackTrace();
                    }
                }
            });

            TimeUnit.SECONDS.sleep(100);*/

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            for (; ;) {
                channel.writeAndFlush(br.readLine() + "\r\n");
            }

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
