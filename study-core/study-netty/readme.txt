discard 丢弃 服务端接受消息 直接丢弃处理 (了解如何异步发送无限数据流而不会使写入缓冲区泛滥)
echo  客户端 服务端 互相发送消息 (非常基本的客户端和服务器)
qotm  客户端 向服务端进行广播 (广播UDP / IP数据包)
telnet 进行telnet请求 (经典的基于行的网络应用程序)
uptime 连接重连 (实现自动重新连接机制)
SecureChat 群聊(基于TLS的聊天服务器，源自Telnet示例 )
ObjectEcho 相互发送可序列化对象( 交换可序列化的Java对象)
WorldClock 获得不同时区的时间 (快速协议与Google协议缓冲集成)



ChannelHandlerContext,ChannelHandler,ChannelPipeline,Channel之间的关系


ChannelHandlerContext在 ChannelHandler 被添加到 ChannelPipeline时候 实例化



我们常用的inbound事件有：

ChannelHandlerContext fireChannelRegistered() //channel注册事件
ChannelHandlerContext fireChannelActive() //channel激活事件
ChannelHandlerContext fireExceptionCaught(Throwable var1) //channel异常处理事件
ChannelHandlerContext fireUserEventTriggered(Object var1) //用户自定义事件
ChannelHandlerContext fireChannelRead(Object var1) //读事件

常用的outbound事件有：

ChannelFuture bind(SocketAddress var1, ChannelPromise var2) //绑定地址
ChannelFuture connect(SocketAddress var1, ChannelPromise var2) //连接服务器
ChannelFuture write(Object var1) //发送事件
ChannelHandlerContext flush() //刷新事件