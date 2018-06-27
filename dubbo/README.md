## dubbo学习笔记

### 初步认识dubbo
dubbo是阿里巴巴公司开源的一个高性能优秀的服务框架，使得应用可通过高性能的RPC实现服务的输出和输入功能，可以和Spring框架无缝集成。  
随着互联网的发展，网站应用的规模不断扩大，常规的垂直应用架构已无法应对，分布式服务架构以及流动计算架构势在必行，亟需一个治理系统确保架构有条不紊的演进。  
dubbo就是在这种背景中产生的。  
下面有几大文档能极大帮助开发者熟悉dubbo这个框架的,建议大家有事没事去看看:  
[Dubbo用户手册(中文)](http://dubbo.apache.org/books/dubbo-user-book/)  
[Dubbo开发手册(中文)](http://dubbo.apache.org/books/dubbo-dev-book/)  
[Dubbo管理手册(中文)](http://dubbo.apache.org/books/dubbo-admin-book/)

### dubbo简单使用
下面将以一个简单的例子来说明dubbo的用法。  
首先dubbo是面向接口开发的，所以第一步得先定义一个接口。  
- api
```kotlin
package com.github.wenj91.kotlin.api

interface TestService {
    fun sayHello(name: String): String
}
```
接口的实现
- service.impl
```kotlin
package com.github.wenj91.kotlin.provider.service.impl

import com.github.wenj91.kotlin.api.TestService

class TestServiceImpl : TestService{
    override fun sayHello(name: String): String {
        return "hello, $name!"
    }
}
```
接口的export
- provider
```kotlin
package com.github.wenj91.kotlin.provider

import com.alibaba.dubbo.config.ApplicationConfig
import com.alibaba.dubbo.config.ProtocolConfig
import com.alibaba.dubbo.config.RegistryConfig
import com.alibaba.dubbo.config.ServiceConfig
import com.github.wenj91.kotlin.api.TestService
import com.github.wenj91.kotlin.provider.service.impl.TestServiceImpl

fun main(args: Array<String>){
    val applicationConfig = ApplicationConfig()
    applicationConfig.name = "dubbo-provider" //配置服务名

    val registryConfig = RegistryConfig()
    registryConfig.address = "N/A" //配置注册中心，这里不使用任何注册中心

    val protocolConfig = ProtocolConfig()
    protocolConfig.name = "dubbo" //配置协议及端口
    protocolConfig.port = 9090

    val serviceConfig = ServiceConfig<TestService>()
    serviceConfig.application = applicationConfig
    serviceConfig.registry = registryConfig
    serviceConfig.protocol = protocolConfig
    serviceConfig.setInterface(TestService::class.java)
    serviceConfig.ref = TestServiceImpl()
    serviceConfig.export() //export接口，暴露接口

    System.`in`.read()
}
```
服务已经暴露出来，下来就是接口的调用
- consumer
```kotlin
package com.github.wenj91.kotlin.consumer

import com.alibaba.dubbo.config.ApplicationConfig
import com.alibaba.dubbo.config.ReferenceConfig
import com.alibaba.dubbo.config.RegistryConfig
import com.github.wenj91.kotlin.api.TestService

fun main(args: Array<String>){
    val applicationConfig = ApplicationConfig()
    applicationConfig.name = "dubbo-consumer" //配置服务名

    val registryConfig = RegistryConfig()
    registryConfig.address = "N/A" //配置注册中心，这里不使用任何注册中心

    val referenceConfig = ReferenceConfig<TestService>()
    referenceConfig.application = applicationConfig
    referenceConfig.registry = registryConfig
    referenceConfig.url = "dubbo://127.0.0.1:9090" //配置直连链接，这样配置就把dubbo当作普通的RPC框架来使用，最简单最直接最本质
    referenceConfig.setInterface(TestService::class.java) //需要获取接口类型

    var testService = referenceConfig.get() //获取接口代理对象
    println(testService.sayHello("wenj91")) //远程调用接口并打印接口返回值

}
```
至此，dubbo已经能正常的工作了，当然这是最简单的配置使用，没有用到dubbo的一些高级特性，甚至没有用注册中心，这其实就是dubbo最本质的东西吧，抛开其它东西，它的本质就是一个rpc框架。

### dubbo配置说明
dubbo功能丰富多样,这就说明dubbo的配置也是很丰富的,这样才能灵活的使用dubbo来实现我们的需求.当然这里只是简单的说明一下dubbo的配置使用,更多更全的说明请参考[dubbo的schema 配置参考手册](http://dubbo.apache.org/books/dubbo-user-book/references/xml/introduction.html),里面有对dubbo的配置做详细的说明.

#### api配置
一开始我们使用的样例就是通过api简单配置来使用的,api配置跟其它配置是一对一的关系,完全通过api来进行配置使用,不依赖任何框架.
比如：`applicationConfig.name = "dubbo-provider"` 对应 `<dubbo:application name="dubbo-provider" />`

provider的api配置:
```kotlin
    //当前应用的配置
    val applicationConfig = ApplicationConfig()
    applicationConfig.name = "dubbo-provider" //配置服务名

    //注册中心的配置
    val registryConfig = RegistryConfig()
    registryConfig.address = "N/A" //配置注册中心，这里不使用任何注册中心

    //协议的配置
    val protocolConfig = ProtocolConfig()
    protocolConfig.name = "dubbo" //配置协议及端口
    protocolConfig.port = 9090

    //暴露服务配置
    val serviceConfig = ServiceConfig<TestService>()
    serviceConfig.application = applicationConfig
    serviceConfig.registry = registryConfig
    serviceConfig.protocol = protocolConfig
    serviceConfig.setInterface(TestService::class.java) //配置接口服务
    serviceConfig.ref = TestServiceImpl() //接口实现
    serviceConfig.export() //export接口，暴露接口
```
consumer的api配置:
```kotlin
    //当前应用的配置
    val applicationConfig = ApplicationConfig()
    applicationConfig.name = "dubbo-consumer" //配置服务名

    //注册中心的配置
    val registryConfig = RegistryConfig()
    registryConfig.address = "N/A" //配置注册中心，这里不使用任何注册中心

    // 引用远程服务
    val referenceConfig = ReferenceConfig<TestService>()
    referenceConfig.application = applicationConfig
    referenceConfig.registry = registryConfig
    referenceConfig.url = "dubbo://127.0.0.1:9090" //配置直连链接，这样配置就把dubbo当作普通的RPC框架来使用，最简单最直接最本质
    referenceConfig.setInterface(TestService::class.java) //需要获取接口类型

    var testService = referenceConfig.get() //获取接口代理对象
    println(testService.sayHello("wenj91")) //远程调用接口并打印接口返回值
```
#### xml配置
有关XML的详细配置项，请参见：[dubbo的schema 配置参考手册](http://dubbo.apache.org/books/dubbo-user-book/references/xml/introduction.html)

基于spring配置的provider.xml示例:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 这里的配置与api配置是一对一关系 -->
    <bean id="testService" class="com.github.wenj91.kotlin.provider.service.impl.TestServiceImpl" />
    <dubbo:application name="dubbo-provider" />
    <dubbo:registry address="N/A" />
    <dubbo:protocol name="dubbo" port="9090" />
    <dubbo:service interface="com.github.wenj91.kotlin.api.TestService" ref="testService" />
</beans>
```
provider加载配置并暴露服务:
```kotlin
package com.github.wenj91.kotlin.provider

import org.springframework.context.support.ClassPathXmlApplicationContext

fun main(args: Array<String>){
    val applicationContext = ClassPathXmlApplicationContext("classpath:provider.xml")
    applicationContext.start()
    System.`in`.read()
}
```
基于spring配置consumer.xml示例:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans-4.3.xsd
       http://code.alibabatech.com/schema/dubbo
       http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!-- 消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样 -->
    <dubbo:application name="dubbo-consumer"  />

    <!-- 不使用注册中心 -->
    <dubbo:registry address="N/A" />

    <!-- 生成远程服务代理，可以和本地bean一样使用testService -->
    <dubbo:reference id="demoService" url="dubbo://127.0.0.1:9090" interface="com.github.wenj91.kotlin.api.TestService" />
</beans>
```
consumer加载配置并引用服务:
```kotlin
package com.github.wenj91.kotlin.consumer

import com.github.wenj91.kotlin.api.TestService
import org.springframework.context.support.ClassPathXmlApplicationContext

fun main(args: Array<String>){
    val applicationContext = ClassPathXmlApplicationContext("classpath:consumer.xml")
    applicationContext.start()

    val testService = applicationContext.getBean(TestService::class.java)
    println(testService.sayHello("wenj91"))
}
```

#### 注解配置并与spring boot集成
注解配置将与Spring Boot集成使用,在这里,采用redis(redis://127.0.0.1:6379)做注册中心,服务配置版本号(version="v1.0.0")并分组(group="wenj91")

provider注解配置示例:
```kotlin
//dubbo provider config
package com.github.wenj91.kotlin.provider.config

import com.alibaba.dubbo.config.ApplicationConfig
import com.alibaba.dubbo.config.ProtocolConfig
import com.alibaba.dubbo.config.RegistryConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DubboProviderConfig{
    @Bean
    fun applicationConfig(): ApplicationConfig {
        var applicationConfig = ApplicationConfig()
        applicationConfig.name = "dubbo-provider" //应用配置

        return applicationConfig
    }

    @Bean
    fun registerConfig(): RegistryConfig {
        var registryConfig = RegistryConfig()
        registryConfig.address = "redis://127.0.0.1:6379" //注册中心配置,这里采用redis做注册中心

        return registryConfig
    }

    @Bean
    fun protocolConfig(): ProtocolConfig {
        var protocolConfig = ProtocolConfig()
        protocolConfig.port = 8888 //protocol配置

        return protocolConfig
    }
}

//service impl
package com.github.wenj91.kotlin.provider.service.impl

import com.alibaba.dubbo.config.annotation.Service
import com.github.wenj91.kotlin.api.TestService

//service config, 这里配置了版本号,以及给服务分组
@Service(version = "v1.0.0", group = "wenj91")
class TestServiceImpl: TestService {
    override fun sayHello(name: String): String {
        return "hello, $name!"
    }
}

//provider app
package com.github.wenj91.kotlin.provider

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@DubboComponentScan
class ProviderApp

//启动应用并暴露服务
fun main(args: Array<String>){
    SpringApplication.run(ProviderApp::class.java, *args)
}
```

consumer注解配置示例:
```kotlin
//dubbo consumer config
package com.github.wenj91.kotlin.consumer.config

import com.alibaba.dubbo.config.ApplicationConfig
import com.alibaba.dubbo.config.ProtocolConfig
import com.alibaba.dubbo.config.RegistryConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DubboConsumerConfig{
    @Bean
    fun applicationConfig(): ApplicationConfig {
        var applicationConfig = ApplicationConfig()
        applicationConfig.name = "dubbo-consumer" //应用配置

        return applicationConfig
    }

    @Bean
    fun registerConfig(): RegistryConfig {
        var registryConfig = RegistryConfig()
        registryConfig.address = "redis://127.0.0.1:6379" //redis注册中心配置

        return registryConfig
    }

    @Bean
    fun protocolConfig(): ProtocolConfig {
        var protocolConfig = ProtocolConfig()
        protocolConfig.port = 8888 //protocol配置

        return protocolConfig
    }
}

//dubbo reference config
package com.github.wenj91.kotlin.consumer.config

import com.alibaba.dubbo.config.annotation.Reference
import com.github.wenj91.kotlin.api.TestService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DubboReferenceConfig{
    //引用服务配置, 这里需要与provider同样配置version与group
    @Reference(version = "v1.0.0", group = "wenj91")
    lateinit var testService: TestService

    @Bean
    fun testService(): TestService {
        return testService
    }
}

//consumer app
package com.github.wenj91.kotlin.consumer

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan
import com.github.wenj91.kotlin.api.TestService
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@DubboComponentScan
class ConsumerApp

//启动应用并调用
fun main(args: Array<String>){
    var applicationContext = SpringApplication.run(ConsumerApp::class.java, *args)
    var testService = applicationContext.getBean(TestService::class.java)
    println(testService.sayHello("wenj91"))
}
```

### dubbo注册中心
dubbo目前支持zookeeper,redis,multicast,simple作为注册中心,但是官方推荐使[Zookeeper 注册中心](http://dubbo.apache.org/books/dubbo-user-book/references/registry/zookeeper.html)

以下是这些注册中心成熟度说明,可以根据这个来选择合适的注册中心:
|Feature | 	Maturity |	Strength |	Problem |	Advise |	User |
|:------:|:------:|:------:|:------:|:------:|:------:|
|Zookeeper注册中心 |	Stable 	|支持基于网络的集群方式，有广泛周边开源产品，建议使用dubbo-2.3.3以上版本|（推荐使用） 	依赖于Zookeeper的稳定性| 	可用于生产环境 	
|Redis注册中心 	| Stable 	| 支持基于客户端双写的集群方式，性能高 	| 要求服务器时间同步，用于检查心跳过期脏数据 |	可用于生产环境 	
|Multicast注册中心 |	Tested |	去中心化，不需要安装注册中心 |	依赖于网络拓普和路由，跨机房有风险 |	小规模应用或开发测试环境 	
|Simple注册中心 |	Tested 	| Dogfooding，注册中心本身也是一个标准的RPC服务 |	没有集群支持，可能单点故障 |	试用 	
	

### dubbo高级用法简介
#### JDK的SPI
SPI是英文`Service Provider Interface`的缩写。中文意思是服务提供商接口。满足某种服务标准的供应商提供的符合该标准的应用程序接口，SPI应该和该服务的API标准是兼容的，应用程序一般应该是基于API编写，除非是SPI中包含API中没有提供的功能而又必须使用。
SPI机制有点类似IOC的思想，就是将装配的控制权移到程序之外.

约定:
当服务的提供者，提供了服务接口的一种实现之后，在jar包的`META-INF/services/`目录里同时创建一个以服务接口命名的文件。该文件里就是实现该服务接口的具体实现类。而当外部程序装配这个模块的时候，就能通过该jar包`META-INF/services/`里的配置文件找到具体的实现类名，并装载实例化，完成模块的注入。 基于这样一个约定就能很好的找到服务接口的实现类，而不需要再代码里制定。jdk提供服务实现查找的一个工具类：`java.util.ServiceLoader`

SPI具体实现示例:
```kotlin
//service
package com.github.wenj91.kotlin.spi.service

interface SpiService {
    fun spiMethod()
}

//service 1 impl
package com.github.wenj91.kotlin.spi.service.impl

import com.github.wenj91.kotlin.spi.service.SpiService

class Spi1ServiceImpl: SpiService{
    override fun spiMethod() {
        println("spi 1 method!")
    }
}

//service 2 impl
package com.github.wenj91.kotlin.spi.service.impl

import com.github.wenj91.kotlin.spi.service.SpiService

class Spi2ServiceImpl: SpiService{
    override fun spiMethod() {
        println("spi 2 method!")
    }
}
```
约定配置`META-INF/services/com.github.wenj91.kotlin.spi.service.SpiService`:
```properties
com.github.wenj91.kotlin.spi.service.impl.Spi1ServiceImpl
com.github.wenj91.kotlin.spi.service.impl.Spi2ServiceImpl
```
service加载调用:
```kotlin
//service loading
package com.github.wenj91.kotlin.spi

import com.github.wenj91.kotlin.spi.service.SpiService
import java.util.*

fun main(args: Array<String>){
    //加载服务
    var loader = ServiceLoader.load(SpiService::class.java)
    var it = loader.iterator()
    while (it.hasNext()){
        //获取服务并执行服务方法
        var s = it.next()
        println(s.spiMethod())
    }
}
```

#### dubbo扩展点加载与配置
Dubbo的扩展点加载从JDK标准的`SPI (Service Provider Interface)`扩展点发现机制加强而来。
Dubbo改进了JDK标准的SPI的以下问题：

>JDK 标准的 SPI 会一次性实例化扩展点所有实现，如果有扩展实现初始化很耗时，但如果没用上也加载，会很浪费资源。
如果扩展点加载失败，连扩展点的名称都拿不到了。比如：JDK 标准的 ScriptEngine，通过 getName() 获取脚本类型的名称，但如果 RubyScriptEngine 因为所依赖的 jruby.jar 不存在，导致 RubyScriptEngine 类加载失败，这个失败原因被吃掉了，和 ruby 对应不起来，当用户执行 ruby 脚本时，会报不支持 ruby，而不是真正失败的原因。
增加了对扩展点 IoC 和 AOP 的支持，一个扩展点可以直接 setter 注入其它扩展点。  

约定：
在扩展类的`jar`包内，放置扩展点配置文件`META-INF/dubbo/接口全限定名`，内容为：`配置名=扩展实现类全限定名`，多个实现类用换行符分隔。

更多细节可以查阅dubbo官方手册[Dubbo开发手册(中文)](http://dubbo.apache.org/books/dubbo-dev-book/)

### dubbo之自定义filter
服务提供方和服务消费方调用过程拦截，Dubbo本身的大多功能均基于此扩展点实现，每次远程方法执行，该拦截都会被执行，请注意对性能的影响。  

约定：  
>用户自定义 filter 默认在内置 filter 之后。
特殊值 default，表示缺省扩展点插入的位置。比如：filter="xxx,default,yyy"，表示 xxx 在缺省 filter 之前，yyy 在缺省 filter 之后。
特殊符号 -，表示剔除。比如：filter="-foo1"，剔除添加缺省扩展点 foo1。比如：filter="-default"，剔除添加所有缺省扩展点。
provider 和 service 同时配置的 filter 时，累加所有 filter，而不是覆盖。比如：<dubbo:provider filter="xxx,yyy"/> 和 <dubbo:service filter="aaa,bbb" />，则 xxx,yyy,aaa,bbb 均会生效。如果要覆盖，需配置：<dubbo:service filter="-xxx,-yyy,aaa,bbb" />

扩展接口:  
com.alibaba.dubbo.rpc.Filter  

例子:
下面一个例子来说明filter的实现,例子实现的功能是在每次服务调用前打印`hello world!`
```kotlin 
//TestFilter
package com.github.wenj91.kotlin.filter

import com.alibaba.dubbo.rpc.Filter
import com.alibaba.dubbo.rpc.Invocation
import com.alibaba.dubbo.rpc.Invoker
import com.alibaba.dubbo.rpc.Result

class TestFilter: Filter{
    override fun invoke(invoker: Invoker<*>?, invocation: Invocation?): Result {
        println("hello world!")
        return invoker!!.invoke(invocation)
    }
}
```
约定配置`META-INF/dubbo/com.alibaba.dubbo.rpc.Filter`:
```properties
testFilter=com.github.wenj91.kotlin.filter.TestFilter
```
consumer配置filter:
```xml
<!-- 新增filter属性 -->
<dubbo:reference id="demoService" filter="testFilter" url="dubbo://127.0.0.1:9090" interface="com.github.wenj91.kotlin.api.TestService" />
```

上述例子filter是通过手工配置指定激活的,如果想要自动激活filter,可以添加`@Activate`注解
>对于可以被框架中自动激活加载扩展，此Annotation用于配置扩展被自动激活加载条件。
比如，过滤扩展，有多个实现，使用Activate Annotation的扩展可以根据条件被自动加载。{@link Activate#group()}生效的Group。具体的有哪些Group值由框架SPI给出。 {@link Activate#value()}在{@link com.alibaba.dubbo.common.URL}中Key集合中有，则生效。
底层框架SPI提供者通过{@link com.alibaba.dubbo.common.extension.ExtensionLoader}的{@link ExtensionLoader#getActivateExtension}方法获得条件的扩展。  

如果需要自动激活可以将filter改造成下例子:
```kotlin
//TestFilter
package com.github.wenj91.kotlin.filter

import com.alibaba.dubbo.common.Constants
import com.alibaba.dubbo.common.extension.Activate
import com.alibaba.dubbo.rpc.Filter
import com.alibaba.dubbo.rpc.Invocation
import com.alibaba.dubbo.rpc.Invoker
import com.alibaba.dubbo.rpc.Result

//group配置默认消费者激活
@Activate(group = arrayOf(Constants.CONSUMER))
class TestFilter: Filter{
    override fun invoke(invoker: Invoker<*>?, invocation: Invocation?): Result {
        println("hello world!")
        return invoker!!.invoke(invocation)
    }
}
```

