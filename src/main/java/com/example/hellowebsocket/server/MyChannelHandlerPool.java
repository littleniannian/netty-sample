package com.example.hellowebsocket.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author jarvis.yuan
 * @version 1.0.0
 * @ClassName MyChannelHandlerPool.java
 * @Description 通道组池，管理所有websocket连接
 * @createTime 2020年12月24日 10:20:00
 */
public class MyChannelHandlerPool {
    public MyChannelHandlerPool() {
    }

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
