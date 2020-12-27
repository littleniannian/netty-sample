package com.example.hellowebsocket.ssl.oneway;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

public class SslContextFactory {
    private static final String PROTOCOL = "TLS";
    private static SSLContext SERVER_CONTEXT; //服务安全套接字协议
    private static SSLContext CLIENT_CONTEXT;//客户端套接字协议

    public static SSLContext getServerContext(String pkPath){
        if(null!=SERVER_CONTEXT)
            return SERVER_CONTEXT;
        InputStream in = null;
        try {
            KeyManagerFactory kmf = null;
            if(pkPath!=null){
                // 密钥库KeyStore
                KeyStore ks = KeyStore.getInstance("JKS");
                // 加载服务端证书
                in = new FileInputStream(pkPath);
                // 加载服务端端KeyStore;nettyDemo是生成仓库时设置端密码，用于检查密钥库端完整性密码
                ks.load(in,"nettyDemo".toCharArray());
                kmf = KeyManagerFactory.getInstance("SunX509");
                // 初始化密钥管理器
                kmf.init(ks,"nettyDemo".toCharArray());
                // 获取安全套接字协议对象
                SERVER_CONTEXT = SSLContext.getInstance(PROTOCOL);
                // 初始化此上上下文
                // 1、认证端密钥 2、对等信任认证 3、伪随机数生成器 。由于单项认证，服务端不能验证客户端，所以第二个参数为null
                SERVER_CONTEXT.init(kmf.getKeyManagers(),null,null);
            }
        }catch (Exception e){
            throw new Error("Failed to initialize the server-side SSLContext", e);
        }finally {
            if(in !=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return SERVER_CONTEXT;
    }

    public static SSLContext getClientContext(String caPath){
        if(CLIENT_CONTEXT!=null) return CLIENT_CONTEXT;

        InputStream tIN = null;
        try{
            //信任库
            TrustManagerFactory tf = null;
            if (caPath != null) {
                //密钥库KeyStore
                KeyStore tks = KeyStore.getInstance("JKS");
                //加载客户端证书
                tIN = new FileInputStream(caPath);
                tks.load(tIN, "nettyDemo".toCharArray());
                tf = TrustManagerFactory.getInstance("SunX509");
                // 初始化信任库
                tf.init(tks);
            }

            CLIENT_CONTEXT = SSLContext.getInstance(PROTOCOL);
            //设置信任证书
            CLIENT_CONTEXT.init(null,tf == null ? null : tf.getTrustManagers(), null);

        }catch(Exception e){
            throw new Error("Failed to initialize the client-side SSLContext");
        }finally{
            if(tIN !=null){
                try {
                    tIN.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return CLIENT_CONTEXT;
    }

}
