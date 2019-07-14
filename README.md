# holmes-java-agent #

<a href="http://lucene.apache.org/">
    <img src="https://img.shields.io/badge/lucene-7.4.0-brightgreen.svg" alt="lucene">
</a>
<a href="https://github.com/vuejs/vue">
    <img src="https://img.shields.io/badge/vue-2.6.10-brightgreen.svg" alt="vue">
</a>
<a href="https://github.com/ElemeFE/element">
    <img src="https://img.shields.io/badge/element--ui-2.8.2-brightgreen.svg" alt="element-ui">
</a>
<a href="https://github.com/Jeffery1993/holmes-java-agent/blob/master/LICENSE">
    <img src="https://img.shields.io/github/license/mashape/apistatus.svg" alt="license">
</a>
<a href="https://github.com/Jeffery1993/holmes-java-agent/releases">
    <img src="https://img.shields.io/github/release/Jeffery1993/holmes-java-agent.svg" alt="GitHub release">
</a>
  
Holmes-java-agent是一个基于字节码增强技术实现的Java应用性能监控工具。

## 截图 ##
### 调用链 ###
![Image text](screenshots/trace.png)

### 监控 ###
![Image text](screenshots/monitor.png)

## 特点 ##
- [x] 无侵入式分布式调用链和应用性能监控
- [x] 存储和搜索采用lucene，除JRE环境外无需其他中间件
- [x] 配套前端页面实现调用链搜索和监控数据可视化

## 试用 ##
```
// 解压已经打好的Release包
unzip holmes-java-agent.zip
// 进入解压后的目录
cd holmes-java-agent
// 启动服务端，浏览器访问"http://localhost:8080"
java -jar holmes-server.jar
// 启动demo程序，浏览器访问"http://localhost:1234/swagger-ui.html"
java -javaagent:holmes-agent.jar -DclusterId=demo -jar holmes-demo.jar
```

## 编译 ##
```
// 拷贝代码到本地
git clone https://github.com/Jeffery1993/holmes-java-agent.git
// 首先进入web目录下
cd holmes-java-agent/server/src/main/web
// 编译前端依赖
npm install
// 前端出包
npm run build
// 复制dist下文件到静态资源文件下
mv dist/* ../resources/common/static

// 返回根目录
cd holmes-java-agent
// 整个工程出包，在packaging模块的target目录下
mvn clean install
```
