package burukeyou.discover.server.handler;

import burukeyou.common.protocol.RpcRequest;
import burukeyou.common.protocol.RpcResponse;
import burukeyou.discover.boot.ServerRegisterBoot;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ServerRequesthandler extends SimpleChannelInboundHandler<RpcRequest> {

    protected ServerRequesthandler(){}

    public static final ServerRequesthandler INSTACNCE = new ServerRequesthandler();

    private static final ThreadPoolExecutor requestThreadPool = new ThreadPoolExecutor(16,32,500L,
            TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(50000),new ThreadPoolExecutor.DiscardOldestPolicy());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        requestThreadPool.submit(() -> {
            System.out.println(msg);

           /* try {
                Thread.sleep(30000); // 模拟请求超时
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setRequestId(msg.getRequestId());

            try {
                Object res = handleRequest(msg);
                rpcResponse.setResult(res);
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setException(e);
            }
            System.out.println(rpcResponse);
            ctx.pipeline().writeAndFlush(rpcResponse);
        });
    }

    /*
        远程调用的核心方法
     */
    private Object handleRequest(RpcRequest request) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //获得要调用哪个类
        String className = request.getClassName();
        className = className.substring(className.lastIndexOf(".")+1);
        // 获得要调用的这个类的哪个方法
        String methodName = request.getMethodName();
        // 获得要调用的这个类的这个方法的参数类型
        Class<?>[] parameterTypes = request.getParameterTypes();
        // 获得要调用的这个类的这个方法的参数类型的具体值
        Object[] parameters = request.getParameters();
        // 获得这个类的具体实现类的class对象
        Object obj = ServerRegisterBoot.impClassMap.get(className);
        Class<?> clazz = obj.getClass();
        // 根据这个类的具体实现类的class对象获取到方法对象
        Method method = clazz.getMethod(methodName, parameterTypes);
        // 执行方法对象的到结果
        return  method.invoke(obj, parameters);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channel_Active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("channel_Inactive");
    }
}
