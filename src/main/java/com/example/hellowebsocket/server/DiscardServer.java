package com.example.hellowebsocket.server;

import com.example.hellowebsocket.handler.DiscardServerHandler;
import com.example.hellowebsocket.handler.TimeServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jarvis.yuan
 * @version 1.0.0
 * @ClassName DiscardServer.java
 * @Description 丢弃任何传来的数据
 * @createTime 2020年12月24日 14:14:00
 */
@Slf4j
public class DiscardServer {
    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception{
        log.info("Discard Server is Running ...............");
        // NioEventLoopGroup是一个处理I/O的多线程事件循环。
        // bossGroup用于accept连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // workGroup将会处理bossGroup接收和注册的连接的流量，bossGroup会将接收到的连接给workerGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 是一个用来建立一个server的类。可以使用Channel类来建立，但是很繁琐。
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class) // 指定NioServerSocketChannel类来创建新的Channel实例并接收连接
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 是一个帮助用户设置新Channel的特殊处理handler
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeServerHandler());
                            socketChannel.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    // 设置指定Channel的参数实现。这事一个基于TCP/IP的服务器，配置socket例如tcpNoDelay/keepAlive
                    // option 是为NioServerSocketChannel接收连接的
                    // childOption 是为被ServerChannel接收的Channels，在这个例子中就是NioServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            // 绑定并且开始接收进来的连接,可以多次调用bind()绑定不同的地址
            ChannelFuture cf = b.bind(port).sync();
            // 等待知道 server socket关闭
            cf.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
