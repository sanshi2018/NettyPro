package burukeyou.client.rpc;

import burukeyou.rpc.annoation.BoomRpc;

import java.util.List;

/*
    同时启动后扫描我们要订阅哪些服务即哪些服务是需要进行远程调用的,这个通过@BoomRpc这个注解去标记, 它的name属性就是配置它需要调用哪个远程服务的服务名,
    所以只需要在启动的时候扫描被标记了@BoomRpc注解的接口获取到name属性值再做一个去重就可以只要当前要订阅哪些服务, 之后就可以用curator的api去订阅这些服务.
 */
@BoomRpc(name = "ServerA",callback = ProductServiceCallback.class)//
public interface ProductService {

    List<String> getAllProductByUserId(String id);

    void buyOne(Integer productId);
}
