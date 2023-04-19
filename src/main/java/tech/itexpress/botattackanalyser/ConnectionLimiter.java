package tech.itexpress.botattackanalyser;

// ConnectionLimiter.java
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionLimiter extends ChannelDuplexHandler {

    private final AtomicInteger connectionCount;
    private final int maxConnectionsPerSecond;

    public ConnectionLimiter(int maxConnectionsPerSecond) {
        this.connectionCount = new AtomicInteger(0);
        this.maxConnectionsPerSecond = maxConnectionsPerSecond;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        int currentConnectionCount = connectionCount.incrementAndGet();
        if (currentConnectionCount > maxConnectionsPerSecond) {
            // Überschreitung der Verbindungslimit, schließen Sie die Verbindung
            ctx.close();
            displayBossBar(currentConnectionCount);
        } else {
            ctx.fireChannelActive();
        }
    }
    public boolean isAllowed() {
        int currentConnectionCount = connectionCount.incrementAndGet();
        if (currentConnectionCount > maxConnectionsPerSecond) {
            connectionCount.decrementAndGet();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        connectionCount.decrementAndGet();
        ctx.fireChannelInactive();
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
        ctx.close(promise);
    }

    private void displayBossBar(int currentConnectionCount) {
        int bossBarLength = 50;
        double connectionPercentage = (double) currentConnectionCount / maxConnectionsPerSecond;
        int filledLength = (int) (bossBarLength * connectionPercentage);

        StringBuilder bossBarBuilder = new StringBuilder();
        bossBarBuilder.append("[");

        for (int i = 0; i < filledLength; i++) {
            bossBarBuilder.append("#");
        }
        for (int i = filledLength; i < bossBarLength; i++) {
            bossBarBuilder.append(" ");
        }

        bossBarBuilder.append("] ");
        bossBarBuilder.append(String.format("%.2f", connectionPercentage * 100)).append("%");

        System.out.println("Verbindungen pro Sekunde: " + currentConnectionCount);
        System.out.println("Bossbar: " + bossBarBuilder.toString());
    }

}
