
import io.netty.util.NettyRuntime;

public class Test {
    public static void main(String[] args) {
        System.out.println("系统可用核数："+NettyRuntime.availableProcessors());
    }
}
