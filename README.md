# msc-rpc-framework

## 📖项目简介

msc-rpc-framework是一款基于 Netty + Zookeeper + Kryo 实现的RPC框架

## ✨功能列表

- 自定义异常类，根据自己业务的异常情况细化异常。同时定义了异常解释枚举类供自定义异常类调用
- 客户端通过动态代理完成对RpcRequest消息格式的封装与对远程服务的调用
- 使用 Netty（基于 NIO）替代 BIO 实现网络传输
- 支持服务端暴露多个接口与接口实现类服务
- 自定义协议通信，使之支持多种消息类型、序列化方式
- 将序列化抽象成接口，利用多种方式实现序列化(Json、Hessian、Kryo)
- 使用Netty的半包解码器解决粘包问题
- 利用zookeeper完成服务器的注册与发现，实现注册中心功能
- 客户端实现负载均衡策略：用服务的时候，从很多服务地址中根据相应的负载均衡算法选取一个服务地址。
- 通过注解实现自动扫描注册服务替代手动注册
- 将RPC消息统一成RpcMessage，使之支持多种消息类型（RpcRequest、RpcResponse、心跳消息）
- 实现了 Netty 心跳机制，确保长连接有效
- 增加SPI机制，方便我们为程序提供扩展功能

## 📚配套教程

[自己动手实现RPC_MSC419的博客-CSDN博客](https://blog.csdn.net/qq_38939822/category_11722139.html)

项目中的doc目录也有部分笔记

## 📝待实现及优化

- [ ] 增加数据压缩方式
- [ ] 增加自定义协议相关配置
- [ ] 集群容错