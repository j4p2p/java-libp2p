package io.libp2p.api.impl;

import io.libp2p.api.Node;
import io.libp2p.api.Peer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NodeImpl implements Node {
    private final String host = "127.0.0.1";
    private final int port;
    private Status nodeStatus = Status.STOPPED;
    private final List<Peer> peers = new ArrayList<>();

    private final Executor executor = Executors.newFixedThreadPool(2);
    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;

    private Channel channel;
    private Channel clientChannel;

    public NodeImpl(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup(1, executor);
        workerGroup = new NioEventLoopGroup(1, executor);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch) {
                            String hostname = ch.remoteAddress().getHostName();

                            peers.add(new PeerImpl(
                                            hostname.equals("localhost") ? "127.0.0.1" : hostname,
                                            ch.remoteAddress().getPort()
                                    )
                            );

                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            channel = bootstrap.bind(host, port).sync().channel();

            nodeStatus = Status.RUNNING;

        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

        this.nodeStatus = Status.STOPPED;
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public Status status() {
        return nodeStatus;
    }

    @Override
    public List<Peer> peers() {
        return peers;
    }

    @Override
    public void connect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                        }
                    });

            clientChannel = bootstrap.connect(host, port).sync().channel();

            peers.add(new PeerImpl(host, port));

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
