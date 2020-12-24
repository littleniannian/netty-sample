package com.example.hellowebsocket.handler;

import com.example.hellowebsocket.server.MyChannelHandlerPool;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jarvis.yuan
 * @version 1.0.0
 * @ClassName MyWebSocketHandler.java
 * @Description
 * 处理ws一下几种情况：
 * channelActive与客户端建立连接
 * channelInactive与客户端断开连接
 * channelRead0客户端发送消息处理
 * @createTime 2020年12月24日 10:24:00
 */
@Slf4j
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端建立连接，通道开启！");
        // 添加到channelGroup通道组
        MyChannelHandlerPool.channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端断开连接，通道关闭！");
        //添加到channelGroup 通道组
        MyChannelHandlerPool.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //首次连接是FullHttpRequest，处理参数
            if(null!=msg && msg instanceof FullHttpRequest){
                FullHttpRequest request = (FullHttpRequest) msg;
                log.info("first fullHttpRequest {}",request);
                String uri = request.uri();
                Map paramMap = getUrlParams(uri);
                log.info("接收到的参数是 {} ",paramMap);
                // 如果url包含参数，需要处理
                if(uri.contains("?")){
                    String newUri=uri.substring(0,uri.indexOf("?"));
                    log.info("newUri {}",newUri);
                    request.setUri(newUri);
                }
            }else if(msg instanceof TextWebSocketFrame){
                // TEXT消息类型
                TextWebSocketFrame frame = (TextWebSocketFrame)msg;
                log.info("客户端收到服务器数据 {} ",frame.text());
                sendAllMessage(frame.text());
            }
        } catch (Exception e) {
            log.error("error occur when channel read message {}",e.getMessage());
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }

    private void sendAllMessage(String message){
        //收到信息后，群发给所有channel
        MyChannelHandlerPool.channelGroup.writeAndFlush(new TextWebSocketFrame(message));
    }

    private Map getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
            return  map;

        }else{
            return map;
        }
    }
}
