package com.example.hellowebsocket;

import com.example.hellowebsocket.telnet.TelnetServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@Slf4j
@SpringBootApplication
public class HelloWebsocketApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWebsocketApplication.class, args);
        TelnetServer telnetServer = new TelnetServer();
        telnetServer.run();
    }

}
