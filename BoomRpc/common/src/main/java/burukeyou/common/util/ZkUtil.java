package burukeyou.common.util;

import burukeyou.common.config.BoomRpcProperties;
import burukeyou.common.entity.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *      服务注册和服务发现
 */
@Component
public class ZkUtil  {

    private Logger logger =  LoggerFactory.getLogger(ZkUtil.class);

    private final BoomRpcProperties boomRpcProperties;

    public ZkUtil(BoomRpcProperties boomRpcProperties) {
        this.boomRpcProperties = boomRpcProperties;
    }

    public static  CuratorFramework zkClient = null;

    private static ReentrantLock updateProviderLock = new ReentrantLock();

    @PostConstruct
    public void init(){
        connect(boomRpcProperties.getRegisterAddress());
    }

    private  boolean connect(String zkServerPath){
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkServerPath)
                .retryPolicy(new RetryNTimes(3, 5000)) // test
                .namespace(Constant.ROOT_NODE)
                .build();
        zkClient.start();
        return zkClient != null;
    }

    /**
     *      服务注册
     * @param nodePath
     * @param nodeData
     * @return
     *
     * 首先需要感知服务器节点的状态是在服务调用端的, 因为它需要知道哪些服务是可用的, 以及这个服务下有哪些服务提供者, 然后才可以进行远程服务调用.
     * 每个应用启动时把 “ /应用名/当前服务器IP : 端口” 这样的节点在Zookeeper创建即可, 比如“/ServerA/192.168.1.7:8090”, 然后一定要把节点设置为临时节点.
     * 这个当该节点挂掉的时候注册中心Zookeeper能够感知到然后自动把该节点删除. 服务注册代码如下:
     */
    public String register(String nodePath, String nodeData) {
        isConnenct();

        String path = null;
        try {
            path =  zkClient.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(nodePath, nodeData.getBytes());
        }catch (KeeperException.NodeExistsException e){
            logger.error("NodeExistsException ----服务注册失败，该服务器节点 {} 已经注册,请修改",e.getPath());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 服务发现
     * 其实就是给该服务添加一个监听器,当有服务器上线或者下线都能感知到,同时更新本地服务提供者缓存
     * @param serverName
     * @return
     */
    public List<String> discover(String serverName){
        isConnenct();

        List<String> serverList = null;
        try {
             PathChildrenCache childrenCache = new PathChildrenCache(zkClient, "/" + serverName,true);
            childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            addListener(childrenCache,serverName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverList;
    }

    private void addListener(PathChildrenCache childrenCache,String serverName){
        childrenCache.getListenable().addListener((curatorFramework, event) -> {
            // 创建子节点
            if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
                String path = event.getData().getPath();
                String host = path.substring(path.lastIndexOf("/") + 1, path.length());
                System.out.println("服务器上线:" + path);

                try {
                    // 更新本地服务提供者缓存
                    //SERVER_PROVIDERS就是一个 Map<String, List<String>>集合
                    updateProviderLock.lock();
                    List<String> list = RpcCacheHolder.SERVER_PROVIDERS.getOrDefault(serverName,new ArrayList<>());
                    list.add(host);
                    RpcCacheHolder.SERVER_PROVIDERS.put(serverName,list);
                } finally {
                    updateProviderLock.unlock();
                }
            }
            // 删除子节点
            else if(event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)){
                String path = event.getData().getPath();
                String host = path.substring(path.lastIndexOf("/") + 1, path.length());
                System.out.println("服务器下线:" + event.getData().getPath());

                try {
                    updateProviderLock.lock();
                    List<String> list = RpcCacheHolder.SERVER_PROVIDERS.get(serverName);
                    list.remove(host);
                    RpcCacheHolder.SERVER_PROVIDERS.put(serverName,list);
                } finally {
                    updateProviderLock.unlock();
                }
            }
        });
    }


    private void isConnenct(){
        if (zkClient == null){
            throw new RuntimeException("have not connect Zookeeper Server");
        }
    }






}
