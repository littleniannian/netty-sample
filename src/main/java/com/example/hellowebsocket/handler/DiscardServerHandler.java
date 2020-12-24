package com.example.hellowebsocket.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author jarvis.yuan
 * @version 1.0.0
 * @ClassName DiscardServerHandler.java
 * @createTime 2020年12月24日 13:59:00
 * @description 模拟discard protocol  https://tools.ietf.org/html/rfc863
 * @Link https://netty.io/wiki/user-guide-for-4.x.html
 */
@Slf4j
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 处理从客户端接收到的数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        // try {
        log.info("Discard Server 接收到的数据 {}",in.toString(Charset.forName("UTF-8")));
        // ChannelHandlerContext 提供了各种操作使你能够触发各种I/O事件和操作
        ctx.write(msg);
        ctx.flush();
        /*} finally {
            // 悄悄丢弃收到的数据
            ReferenceCountUtil.release(msg);
        }*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 有异常发生 关闭数据
        // 此方法是对发生I/O异常后的处理
        cause.printStackTrace();;
        ctx.close();
    }
}
