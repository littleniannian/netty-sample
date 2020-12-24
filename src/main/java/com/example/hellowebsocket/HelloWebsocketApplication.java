package com.example.hellowebsocket;

import com.example.hellowebsocket.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class HelloWebsocketApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWebsocketApplication.class, args);
       // DiscardServer discardServer = new DiscardServer(12345);
       // discardServer.run();
        NettyServer nettyServer = new NettyServer(12345);
        nettyServer.start();
    }

}
