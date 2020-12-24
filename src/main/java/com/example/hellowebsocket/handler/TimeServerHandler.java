package com.example.hellowebsocket.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author jarvis.yuan
 * @version 1.0.0
 * @ClassName TimeServerHandler.java
 * @Description The protocol to implement in this section is the TIME protocol
 * @createTime 2020年12月24日 15:45:00
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        // As explained, the channelActive() method will be invoked when a connection is established and ready to generate traffic.
        // Let's write a 32-bit integer that represents the current time in this method.
        final ByteBuf time = ctx.alloc().buffer(4);
        // 时间戳 1900年1月1日
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        // Another point to note is that the ChannelHandlerContext.write() (and writeAndFlush()) method returns a ChannelFuture.
        // A ChannelFuture represents an I/O operation which has not yet occurred.
        final ChannelFuture f = ctx.writeAndFlush(time); // 这个东西是异步的
         /*Therefore, you need to call the close() method after the ChannelFuture is complete, which was returned by the write() method,
         and it notifies its listeners when the write operation has been done.
         Please note that, close() also might not close the connection immediately, and it returns a ChannelFuture.
        f.addListener((ChannelFutureListener) channelFuture -> {
            assert f == channelFuture;
            ctx.close();
        });
         Alternatively, you could simplify the code using a pre-defined listener*/
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
