package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-25 21:26
 **/
public class NIOFileChannel3 {
    public static void main(String[] args) throws Exception {
        File file = new File("./a.txt");


        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel1 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("b.txt");
        FileChannel fileChannel2 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {

//            不能忘记！！！
            byteBuffer.clear();

            int read = fileChannel1.read(byteBuffer);
            if (read == -1){
                break;
            }
            byteBuffer.flip();
            fileChannel2.write(byteBuffer);
        }

        fileInputStream.close();
        fileOutputStream.close();
    }

}
