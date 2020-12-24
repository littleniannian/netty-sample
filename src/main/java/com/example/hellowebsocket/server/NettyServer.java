package com.example.hellowebsocket.server;

import com.example.hellowebsocket.handler.MyWebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jarvis.yuan
 * @version 1.0.0
 * @ClassName NettyServer.java
 * @Description Netty服务器配置
 * @createTime 2020年12月24日 10:03:00
 */
@Slf4j
public class NettyServer {
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup group = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.option(ChannelOption.SO_BACKLOG,1024);
        serverBootstrap.group(group,bossGroup) // 绑定线程池
                .channel(NioServerSocketChannel.class) //指定使用的channel
                .localAddress(this.port)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        log.info("收到新连接");
                        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                        socketChannel.pipeline().addLast(new HttpServerCodec());
                        //以块的方式来写的处理器
                        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
                        socketChannel.pipeline().addLast(new HttpObjectAggregator(8192));
                        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws",null,true,65526*10));
                        socketChannel.pipeline().addLast(new MyWebSocketHandler());
                    }
                });
        ChannelFuture cf = serverBootstrap.bind().sync(); //服务器异步创建绑定
        log.info("NettyServer 启动正在监听 {}",cf.channel().localAddress());
        cf.channel().closeFuture().sync(); //关闭服务器通道
    }
}
