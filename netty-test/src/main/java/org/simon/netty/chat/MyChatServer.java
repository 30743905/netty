package org.simon.netty.chat;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;

public class MyChatServer {

    public static void main(String[] args) throws Exception {
        /**
         * bossGroup表示监听端口，accept 新连接的线程组
         * workerGroup表示处理每一条连接的数据读写的线程组
         *
         * 用生活中的例子来讲就是，一个工厂要运作，必然要有一个老板负责从外面接活，然后有很多员工，负责具体干活，
         * 老板就是bossGroup，员工们就是workerGroup，bossGroup接收完连接，扔给workerGroup去处理
         *
         * NioEventLoopGroup主要管理EventLoop的生命周期。
         * EventLoop是什么？姑且把它看成是内部的一个处理线程，数量默认是处理器个数的两倍。
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            /**
             * 接下来 我们创建了一个引导类 ServerBootstrap，这个类将引导我们进行服务端的启动工作，直接new出来开搞。
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)//通过.group(bossGroup, workerGroup)给引导类配置两大线程组，这个引导类的线程模型也就定型了。
                    /**
                     * 实例化不同的Channel类型，即代表服务端的IO模型为NIO，我们通过channel(NioServerSocketChannel.class)来指定IO模型，
                     * 当然，这里也有其他的选择，如果你想指定IO模型为BIO，那么这里配置上OioServerSocketChannel.class类型即可,
                     * 当然通常我们也不会这么做，因为Netty的优势就在于NIO。
                     */
                    .channel(NioServerSocketChannel.class)
                            .childOption(ChannelOption.TCP_NODELAY, true)
                            .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
                            .childHandler(new MyChatServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
