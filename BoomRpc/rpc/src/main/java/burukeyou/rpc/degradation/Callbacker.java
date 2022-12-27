package burukeyou.rpc.degradation;

import burukeyou.common.protocol.RpcRequest;
import burukeyou.rpc.annoation.BoomRpc;

import java.lang.reflect.Method;

/**
 *      容错处理器
 */
public class Callbacker {

    private RpcRequest rpcRequest;

    private Class<?> callBackClass;
    private Class<?> callback;


    public static Callbacker Builder(RpcRequest rpcRequest){
        return new Callbacker(rpcRequest);
    }

    // 创建Callbacker的同时拿到 BoomRpc注解配置的callback类
    private  Callbacker(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
        try {
            System.out.println(rpcRequest);
            callBackClass =  Class.forName(rpcRequest.getClassName());
            callback = callBackClass.getAnnotation(BoomRpc.class).callback();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("1");
        }
    }

    // 如果不需要回调处理直接执行自定义的处理方法Process,一般都会直接传抛出异常
    public Callbacker IfNotCallback(Process process){
         if (!shouldCallback())
             process.doSomething();
        return this;
    }
    // 如果要进行回调处理, 直接把异常信息传入去处理即可
    public Object orElseSet(Throwable throwable) throws Exception {
        if (shouldCallback()){
            // 创建callback的实例对象
            Object obj = callback.newInstance();
            //从callback上获得此次请求的方法的方法对象
            Method method = callback.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
            // 由于callback类都需要继承Callback抽象类拿到异常信息,这里就可以动态注入异常信息
            Method setThrowable = callback.getMethod("setThrowable", Throwable.class);
            setThrowable.invoke(obj,throwable);
            // 之后执行此次请求的方法的方法对象即可
            return method.invoke(obj,rpcRequest.getParameters());
        }else
            return null;
    }
    // 判断callback是否为默认值void.class来进行回调处理
    private boolean shouldCallback(){
        return callback != void.class;
    }


    public interface Process {
        void doSomething();
    }
}
