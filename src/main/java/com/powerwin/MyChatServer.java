package com.powerwin;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyChatServer {



    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    //我要指定使用NioServerSocketChannel这种类型的通道
                    .channel(NioServerSocketChannel.class)
                    //一定要使用 childHandler 去绑定具体的 事件处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            sc.pipeline().addLast(new MyChatServerHandler());
                        }
                    });

//            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
//            channelFuture.channel().closeFuture().sync();
            //绑定指定的端口 进行监听

            ChannelFuture f = serverBootstrap.bind(8765).sync();
            ChannelFuture f2 = serverBootstrap.bind(8764).sync();

            //Thread.sleep(1000000);
            f.channel().closeFuture().sync();   //等待关闭
            f2.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
