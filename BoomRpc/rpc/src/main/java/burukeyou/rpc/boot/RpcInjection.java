package burukeyou.rpc.boot;

import burukeyou.common.util.RpcCacheHolder;
import burukeyou.rpc.annoation.BoomRpc;
import burukeyou.rpc.annoation.EnableBoomRpc;
import burukeyou.rpc.proxy.BoomRpcInvoker;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Set;

public class RpcInjection implements ImportBeanDefinitionRegistrar {

    /**
     * 注入服务方法
     *
     * AnnotationMetadata:当前类的注解信息；
     *          就是使用了@Import({RpcBoot.class})并且RpcBoot是ImportBeanDefinitionRegistrar的实现类才会被调用了
     *
     * BeanDefinitionRegistry：注册类，其registerBeanDefinition()可以注册bean
     **/
    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableBoomRpc.class.getName());
        String[] provider = (String[])attributes.get("provider");

        /**
         * 扫描@BoomRpc注解代码实现如下:
         * Reflections reflections = new Reflections("@BoomRpc注解所在的包");
         * Set<Class<?>> boomRpcClasses = reflections.getTypesAnnotatedWith(BoomRpc.class);
         *
         * 但是，这里需要注意的是，如果是springboot项目，springboot项目启动时会扫描项目中所有的类，所以这里不需要再次扫描
         * 如果是spring项目，那么就需要扫描了
         *
         * 扫描@BoomRpc注解的实现代码如下：
         * Reflections reflections = new Reflections(provider);
         * Set<Class<?>> boomRpcClasses = reflections.getTypesAnnotatedWith(BoomRpc.class);
         */
        Reflections reflections = new Reflections(provider); // @BoomRpc注解所在的包
        Set<Class<?>> rpcClazz = reflections.getTypesAnnotatedWith(BoomRpc.class, true);// honorInherited-不包含子类
        //把要订阅的服务serverName添加到Set集合即可
        for (Class<?> e : rpcClazz) {
            BoomRpc annotation = e.getAnnotation(BoomRpc.class);
            // 获取服务名
            String serverName = "".equals(annotation.name()) ? annotation.value() : annotation.name();
            // 通过反射创建代理对象
            BoomRpcInvoker boomRpcInvoker = new BoomRpcInvoker(serverName);
            // 代理对象
            Object proxyInstance = Proxy.newProxyInstance(e.getClassLoader(), new Class[]{e},boomRpcInvoker);
            // 将代理对象放入spring容器中
            SingletonBeanRegistry singletonBeanRegistry = (SingletonBeanRegistry)registry;
            singletonBeanRegistry.registerSingleton(e.getName(),proxyInstance);
            // 将服务名和代理对象放入缓存中
            RpcCacheHolder.SUBSCRIBE_SERVICE.add(serverName);
        }
    }
}
