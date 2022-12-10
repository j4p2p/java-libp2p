package io.libp2p;

import io.libp2p.api.Node;
import io.libp2p.api.Peer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Host implements Node {
    private final Executor executor = Executors.newFixedThreadPool(2);
    private final int port;
    private Channel channel;
    private Channel clientChannel;
    private EventLoopGroup workerGroup;
    private EventLoopGroup bossGroup;

    private Node.Status status = Status.STOPPED;
    private final List<Peer> peers = new ArrayList<>();

    private Host(int port) {
        this.port = port;
    }

    public static Host of(int port) {
        return new Host(port);
    }

    @Override
    public void start() {
        bossGroup = new NioEventLoopGroup(1, executor);
        workerGroup = new NioEventLoopGroup(1, executor);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            peers.add(new Peer() {

                                @Override
                                public String host() {
                                    String hostname = ch.remoteAddress().getHostName();
                                    return hostname.equals("localhost") ? "127.0.0.1" : hostname;
                                }

                                @Override
                                public int port() {
                                    return ch.remoteAddress().getPort();
                                }
                            });
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    byte[] bytes = new byte[6];
                                    ((ByteBuf) msg).readBytes(bytes);
                                    System.out.println(new String(bytes));
                                    ((ByteBuf) msg).release();
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            });
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(host(), port).sync();
            channel = f.channel();
            status = Status.RUNNING;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

        status = Status.STOPPED;
    }

    @Override
    public String host() {
        return "127.0.0.1";
    }

    @Override
    public int port() {
        return port;
    }

    @Override
    public int outputPort() {
        return ((InetSocketAddress) clientChannel.localAddress()).getPort();
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public List<Peer> peers() {
        return peers;
    }

    @Override
    public void connect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            ByteBuf msg = ctx.alloc().buffer(6);
                            msg.writeBytes("qwerty".getBytes());
                            ctx.channel().writeAndFlush(msg);
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                            cause.printStackTrace();
                            ctx.close();
                        }
                    });
                }
            });
            clientChannel = b.connect(host, port).sync().channel();

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
