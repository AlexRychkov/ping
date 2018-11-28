package com.network.ping;

import com.network.ping.net.Catcher;
import com.network.ping.net.Pitcher;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

import java.net.InetAddress;
import java.util.concurrent.Callable;

public class Application implements Callable<Void> {
    @Spec
    private CommandSpec spec;
    @Option(names = "-c", paramLabel = "CATCHER MODE", description = "Run program in catcher mode")
    private boolean catcherMode;
    @Option(names = "-p", paramLabel = "PITCHER MODE", description = "Run program in pitcher mode")
    private boolean pitcherMode;
    @Option(names = "-port", required = true, paramLabel = "PORT", description = "Listening/connecting port")
    private int port;
    @Option(names = "-bind", paramLabel = "ADDRESS", description = "Remote address in catcher mode")
    private InetAddress bindAddress;
    @Option(names = "-mps", paramLabel = "SPEED", description = "The speed of message sending (only pitcher mode)")
    private Integer speed = 1;
    @Option(names = "-size", paramLabel = "SIZE", description = "Message length; Minimum: 50, Maximum: 3000, Default: 300 (only pitcher mode")
    private int size = 300;
    @Parameters(index = "0", arity = "0..1")
    private InetAddress hostName;

    public static void main(String[] args) {
        CommandLine.call(new Application(), args);
    }

    @Override
    public Void call() {
        if (catcherMode == pitcherMode) {
            throw new ParameterException(spec.commandLine(), "Required to choose one program mode: -c catcher, -p pitcher");
        }
        if (catcherMode && bindAddress == null) {
            throw new ParameterException(spec.commandLine(), "Required to specify local address in catcher mode");
        }
        if (pitcherMode && hostName == null) {
            throw new ParameterException(spec.commandLine(), "Required to specify catcher hostname in pitcher mode");
        }
        if (size < 50 || size > 3000) {
            throw new ParameterException(spec.commandLine(), "Size must be in range 50..3000");
        }
        if (catcherMode) {
            Catcher catcher = new Catcher(port, bindAddress);
            catcher.run();
        } else {
            Pitcher pitcher = new Pitcher(hostName, 21843, port, speed, size);
            pitcher.run();
        }
        return null;
    }
}