package com.jeffery.holmes.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Netty server that read data from listening port.
 */
@Service
public class NettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    @Value("${socket.port}")
    private int socketPort;

    @PostConstruct
    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        LOGGER.info("Netty server is starting...");
        try {
            serverBootstrap
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(new DefaultMessageDecoder());
                            ch.pipeline().addLast(new DefaultMessageHandler());
                        }
                    })
                    .bind(socketPort);
        } catch (Exception e) {
            LOGGER.error("Failed to start netty", e);
        }
        LOGGER.info("Netty server started!!!");
    }

}
