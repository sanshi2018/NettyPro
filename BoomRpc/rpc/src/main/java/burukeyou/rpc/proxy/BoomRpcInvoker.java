package burukeyou.rpc.proxy;

import burukeyou.common.protocol.RpcRequest;
import burukeyou.common.protocol.RpcResponse;
import burukeyou.rpc.client.RpcClient;
import burukeyou.common.util.RpcCacheHolder;
import burukeyou.rpc.degradation.Callbacker;
import burukeyou.rpc.loadBalance.ConsistentHashStrategy;
import burukeyou.rpc.loadBalance.LoadBalanceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class BoomRpcInvoker implements InvocationHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String serverName;

    public BoomRpcInvoker(String serverName) {
        this.serverName = serverName;
    }


    /**
     *  消费者远程调用的核心方法
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1 - 封装请求对象,用于发送到另一端
        //就是要告诉另一端你要调用哪个类的哪个方法
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParameterTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);

        // 2- 获得服务serverName的服务提供者列表
        List<String> providerList = RpcCacheHolder.SERVER_PROVIDERS.get(serverName);
        // 容错处理
        if (providerList == null || providerList.size() < 1 ){
            // 另一段压根没提供服务的机器存在,肯定无法远程调用直接降级处理
            return Callbacker.Builder(rpcRequest)
                    .IfNotCallback(() -> {throw new RuntimeException(serverName+"服务不存在，调用失败");})
                    .orElseSet(new RuntimeException(serverName+"服务不存在，调用失败"));
        }

        // 4. 负载均衡,具体要把请求发送到哪个机器处理
        LoadBalanceContext loadBalanceContext = RpcCacheHolder.APPLICATION_CONTEXT.getBean(LoadBalanceContext.class);
        String serverIp = loadBalanceContext.executeLoadBalance(providerList);
        System.out.println("负载均衡: 调用服务" + serverName +"的" +  serverIp + " 服务器节点");

        //
        String[] host = serverIp.split(":");
        RpcClient rpcClient = new RpcClient(host[0].trim(), Integer.parseInt(host[1].trim()),serverName);

        RpcResponse rpcResponse = null;
        try {
            // 5 - 最后把请求消息通过Netty发送到另一端即可, 等待接受另一端的响应
            rpcResponse = rpcClient.sendRequest(rpcRequest);
        } catch (Exception e) {
            // 如果因为某些原因发送失败,直接降级处理
            return Callbacker.Builder(rpcRequest).IfNotCallback(e::printStackTrace).orElseSet(e);
        }

        // 最后把实际远程调用的那个方法的返回值返回即可
        return rpcResponse != null ? rpcResponse.getResult() : null;
    }
}
