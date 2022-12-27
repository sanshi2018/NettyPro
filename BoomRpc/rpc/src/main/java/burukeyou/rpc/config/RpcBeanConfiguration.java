package burukeyou.rpc.config;

import burukeyou.common.config.BoomRpcProperties;
import burukeyou.rpc.loadBalance.LoadBalanceContext;
import burukeyou.rpc.loadBalance.RandomStrategy;
import burukeyou.rpc.loadBalance.RoundRobinStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 一般都可以在配置文件中配置使用哪种负载策略,然后用Spring读取动态生成对应的负载均衡实现类然后设置到LoadBalanceContext上下文种,
 * 最后再把LoadBalanceContext注入到Spring容器中即可. 之后通过LoadBalanceContext执行负载策略.代码实现
 */
@Configuration
public class RpcBeanConfiguration {

    private final BoomRpcProperties properties;

    public RpcBeanConfiguration(BoomRpcProperties properties) {
        this.properties = properties;
    }

    // Spring启动时执行这段代码,把LoadBalanceContext注入到容器中
    @Bean
    public LoadBalanceContext loadBalanceContext(){
        LoadBalanceContext loadBalanceContext = new LoadBalanceContext();
        if ("roundRobin".equalsIgnoreCase(properties.getLoadblacne().trim())){
            loadBalanceContext.setLoadBalanceStrategy(new RoundRobinStrategy());
        }else if("random".equalsIgnoreCase(properties.getLoadblacne().trim())){
            loadBalanceContext.setLoadBalanceStrategy(new RandomStrategy());
        }else if("hash".equalsIgnoreCase(properties.getLoadblacne().trim())){
            // todo
        }else {
            loadBalanceContext.setLoadBalanceStrategy(new RandomStrategy());
        }
        return loadBalanceContext;
    }
}
