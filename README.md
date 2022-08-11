# 已做：
## 1.通过注解注册Servlet、Filter、Listener到Web容器：
    HelloServlet
    MyFilter
    MyListener
## 2.使用Web容器通用参数扩展入口：MyGeneralCustomizer
    网络监听端口：8080
    web上下文路径：/hello
## 3.使用Tomcat扩展入口：MyTomcatCustomizer
    engine容器中的pipeline增加一个valve,用于为所有请求增加traceId
## 4.配置jmx监控tomcat
    -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9002 -Djava.rmi.server.hostname=127.0.0.1 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false 