package com.atguigu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-20 22:46
 **/
public class BIOServer {
    public static void main(String[] args) throws IOException {

        //1. 创建线程池
//        2。如果有客户端连接，创建一个线程与之通信（单独写一个方法）

        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        final ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("server start");
//        Future<Integer> submit = newCachedThreadPool.submit(new Callable<Integer>() {
//            public Integer call() throws Exception {
//                return 1;
//            }
//        });
//        Integer integer = null;
//        try {
//            integer = submit.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        System.out.println(integer);
        while (true){
            //监听，等待客户端连接
            System.out.println("等待连接");
            final Socket socket = serverSocket.accept();


            System.out.println("连接一个客户端");

            //创建线程与之通信
            newCachedThreadPool.submit(new Runnable() {
                public void run() {//可以和客户端通信
                    handle(socket);
                }
            });
        }
    }

    //编写一个handle方法与客户端通信

    public static void handle(Socket socket){
        byte[] bytes = new byte[1024];
        // 通过socket 获取输入流
        try {
            System.out.println(Thread.currentThread().getId()+"name"+
                    Thread.currentThread().getName());
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while (true){
                System.out.println(Thread.currentThread().getId()+"name"+
                        Thread.currentThread().getName());

                System.out.println("read...");
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println(new String(bytes,0, read));
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
