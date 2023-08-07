package com.example.websocketclient.test;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketClientTest {

    private static final AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) {
        URI uri = URI.create("ws://127.0.0.1:20089/websocket/123");  //注意协议号为ws
        MyWebSocketClient client = new MyWebSocketClient(uri);
        client.addHeader("token", "123");              //这里为header添加了token，实现简单的校验
        try {
            client.connectBlocking();   //在连接成功之前会一直阻塞

            Timer timer = new Timer();
            MyTimerTask timerTask = new MyTimerTask(client);
            timer.schedule(timerTask, 1000, 2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyTimerTask extends TimerTask{

        private final MyWebSocketClient client;

        public MyTimerTask(MyWebSocketClient client){
            this.client = client;
        }

        @Override
        public void run() {
            client.send("Test message from client, the number is " + count.getAndIncrement());
        }
    }
}
