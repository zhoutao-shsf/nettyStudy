package com.atguigu.nio;

import java.nio.IntBuffer;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-22 08:42
 **/
public class BasicBuffer {

    public static void main(String[] args) {

        //创建一个buffer 容量为5
        IntBuffer intBuffer = IntBuffer.allocate(5);


//         向buffer存数据
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2);
        }


//        取数据

//        将buffer转换，读写切换（！！！！）
        intBuffer.flip();


        intBuffer.position(1);
        intBuffer.limit(4);
        while (intBuffer.hasRemaining()){

//            get维护一个索引
            System.out.println(intBuffer.get());
        }
    }
}
