package com.atguigu.netty.EX_89_protocolTcp;

import lombok.Getter;
import lombok.Setter;

public class MessageProtocol {
    @Getter
    @Setter
    private int len;
    @Getter
    @Setter
    private byte[] content;

}
