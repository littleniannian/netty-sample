package com.example.hellowebsocket.telnet;

import com.example.hellowebsocket.server.MyChannelHandlerPool;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.Date;
@Slf4j
@ChannelHandler.Sharable
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive {}",ctx.channel().remoteAddress());
        MyChannelHandlerPool.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Send greeting for a new connection.
        ctx.write("欢迎，进入 " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("It is " + new Date() + " now.\r\n");
        ctx.flush();
        MyChannelHandlerPool.channelGroup.add(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        // Generate and write a response.
        String response;
        if (request.isEmpty()) {
            response = "Please type something.\r\n";
        } else {
            response = request + "\r\n";
        }
        log.info("message {}",response);
        // We do not need to write a ChannelBuffer here.
        // We know the encoder inserted at TelnetPipelineFactory will do the conversion.

        MyChannelHandlerPool.channelGroup.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}