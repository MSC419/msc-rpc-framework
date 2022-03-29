package com.wx.mscrpc.client;

import com.wx.mscrpc.common.ServiceDescription;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Description 客户端类：通过Socket发送请求
 * @Author MSC419
 * @Date 2022/3/29 21:08
 * @Version 1.0
 */
@Slf4j
public class RpcClient {
    /**
     * @Description 发送请求服务的请求
     * @param sdp  请求描述
     * @param host  对方IP地址
     * @param port  端口号
     * @Return 这个返回值暂时不知道有什么用
     * @Author MSC419
     * @Date 2022/3/29 21:39
     */

    public Object sendRpcRequest(ServiceDescription sdp, String host, int port){

        try(Socket socket = new Socket(host, port)){
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(sdp);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
