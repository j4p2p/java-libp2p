package io.libp2p.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SillyClient {
    private final String host;
    private final int port;
    private final ExecutorService executor;

    private SillyClient(String host, int port, ExecutorService executor) {
        this.host = host;
        this.port = port;
        this.executor = executor;
    }

    public static SillyClient of(String host, int port, ExecutorService executor) {
        return new SillyClient(host, port, executor);
    }

    public Future<String> readLine() {
        return executor.submit(() -> {
            try(Socket socket = new Socket(host, port);
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                return br.readLine();
            }
        });
    }
}
