package burukeyou.common.protocol;


import burukeyou.common.entity.enums.RequestTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

public class RpcRequest extends RpcProtocol  {

    /**
     *      请求id
     *      请求id如果是基于长连接传输设计,
     *      即多个请求共用一个连接通道就会无法保证请求的有序性, 这样就不知哪个请求是谁发的那接收到一堆响应后又不知道要教给谁处理了,
     *      如果每次给请求生成全局唯一ID, 就可以保证每一个请求的唯一性有序性
     *
     *      不过这里实现是基于一个请求一个连接后就断开的方法处理暂时不会有这个问题
     */
    @Getter
    @Setter
    private String requestId;

    /**
     *  调用类名
     */
    @Getter
    @Setter
    private String className;

    /**
     *  调用方法名
     */
    @Getter
    @Setter
    private String methodName;

    /**
     *  方法参数类型
     */
    @Getter
    @Setter
    private Class<?>[] parameterTypes;

    /**
     *  方法参数
     */
    @Getter
    @Setter
    private Object[] parameters;


    @Override
    public Byte getRequestType() {
        return RequestTypeEnum.REQUEST.getType();
    }


    @Override
    public String toString() {
        return "RpcRequest{" +
                "requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}
