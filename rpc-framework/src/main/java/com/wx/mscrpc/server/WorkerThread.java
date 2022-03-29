package com.wx.mscrpc.server;

import com.wx.mscrpc.common.ServiceDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 21:52
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@Slf4j
public class WorkerThread implements Runnable{
    private Socket socket;
    private Object service;

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            ServiceDescription sdp = (ServiceDescription) objectInputStream.readObject();
            Method method = service.getClass().getMethod(sdp.getMethodName(), sdp.getParamTypes());
            Object result = method.invoke(service, sdp.getParameters());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("occur exception:", e);
        }
    }
}
