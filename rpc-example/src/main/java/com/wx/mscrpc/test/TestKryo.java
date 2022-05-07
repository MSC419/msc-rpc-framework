package com.wx.mscrpc.test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/4/5 10:59
 * @Version 1.0
 */
public class TestKryo {
    static public void main (String[] args) throws Exception {
        Kryo kryo = new Kryo();
        kryo.register(SomeClass.class);

        SomeClass object = new SomeClass();
        object.value = "Hello Kryo!";

        Output output = new Output(new FileOutputStream("D:\\file.txt"));
        kryo.writeObject(output, object);
        output.close();

        Input input = new Input(new FileInputStream("D:\\file.txt"));
        SomeClass object2 = kryo.readObject(input, SomeClass.class);
        input.close();

        System.out.println(object2.value);
    }
    static public class SomeClass {
        String value;
    }

}
