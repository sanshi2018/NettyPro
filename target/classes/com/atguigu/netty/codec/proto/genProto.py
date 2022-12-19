'''
Author: sanshi 191041656@qq.com
Date: 2022-12-06 16:07:47
LastEditors: sanshi 191041656@qq.com
LastEditTime: 2022-12-06 16:23:41
FilePath: /proto/genProto.py
Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
'''
# 将proto文件生成java文件

import os
import sys

# proto文件路径
protoPath = "/Users/sanshi/IdeaProjects/NettyPro/src/main/java/com/atguigu/netty/codec/proto"
# java文件输出路径
javaPath = "/Users/sanshi/IdeaProjects/NettyPro/src/main/java/com/atguigu/netty/codec/proto"

# 遍历proto文件夹下的所有文件
for root, dirs, files in os.walk(protoPath):
    for file in files:
        # 只处理proto文件
        if file.endswith(".proto"):
            # 生成java文件 using --proto_path
            os.system("protoc --proto_path={} --java_out={} {}".format(protoPath, javaPath, file))


