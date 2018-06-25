### 匹配方法
- execution()
```java
//匹配service包下，以Service结尾的类的所有方法，任意参数（包含无参），任意返回值，并抛出了下面异常
@Pointcut("execution(public * com.fangminx.service.*Service.*(..) throws java.lang.IllegalAccessException)")
public void matchCondition(){}

1.若只匹配无参，括号里的..不写
2.不匹配service的子包，若要匹配子包service后面加两个点号
```

### 匹配注解
- @target()/@within()
```
匹配加了@Service注解的类中的所有方法：
@within(org.springframework.stereotype.Service)
或
@target(org.springframework.stereotype.Service)
 
@within()要求annotation的RetentionPolicy级别为RUNTIME
@target()要求annotation的RetentionPolicy级别为CLASS
```
- @args()
```
匹配方法加了@RequestBody注解的所有方法
@args(org.springframework.web.bind.annotation.RequestBody)
```

- @annotation()
```
匹配加了@RequestMapping注解的所有方法
@annotation(org.springframework.web.bind.annotation.RequestMapping)
```

### 匹配包/类型
- within()
```java
//匹配ProductService类里所有方法
@Pointcut("within(com.fangminx.service.ProductService)")
public void matchType(){}
  
//匹配com.fangminx包及子包下所有类的方法
@Pointcut("within(com.fangminx..*)")
public void matchPackage(){}
```

### 匹配对象
- this()
```java
//匹配实现DemoDao对象的方法
@Pointcut("this(com.fangminx.DemoDao)")
public void thisDemo(){}
```
- target()
```
和this()类似，匹配不到introduction动态生成的方法
```
- bean
```java
//匹配所有以Service结尾的bean里的方法
@Pointcut("bean(*Service)")
public void beanDemo(){}
```
### 匹配参数
- args()
```java
//匹配任何以find开头而且只有一个Long参数的方法
@Pointcut("execution(* *..find*(Long))")
public void argsDemo1(){}

//匹配任何只有一个Long参数的方法
@Pointcut("args(Long)")
public void argsDemo2(){}

//匹配任何以find开头而且第一个参数为Long型的方法
@Pointcut("execution(* *..find*(Long,..))")
public void argsDemo3(){}

//匹配第一个参数为Long型的方法
@Pointcut("args(Long,..)")
public void argsDemo4(){}

```
## wildcards(通配符)
```java
* 匹配任意数量的字符
+ 匹配指定类及其子类
.. 一般用于匹配任意数的子包或参数
```
## operators(运算符)
```java
&& 与操作符
|| 或操作符
! 非操作符
```

## 5种Advice注解
```java
1.@Before前置通知
2.@After后置通知,方法执行之后
3.@AfterReturning返回通知，成功执行之后
4.@AfterThrowing异常通知，抛出异常之后
5.@Around环绕通知
```

## JoinPoint的用法

JoinPoint 对象

- JoinPoint对象封装了SpringAop中切面方法的信息,在切面方法中添加JoinPoint参数,就可以获取到封装了该方法信息的JoinPoint对象.
常用api:

|方法名 | 	功能
|:-------|:------|  
|Signature getSignature(); |	获取封装了署名信息的对象,在该对象中可以获取到目标方法名,所属类的Class等信息
|Object[] getArgs(); |	获取传入目标方法的参数对象
|Object getTarget(); |	获取被代理的对象
|Object getThis(); |	获取代理对象


ProceedingJoinPoint对象

- ProceedingJoinPoint对象是JoinPoint的子接口,该对象只用在@Around的切面方法中,
添加了  

|方法名 | 	功能
|:-------|:------|  
|Object proceed() throws Throwable | //执行目标方法
|Object proceed(Object[] var1) throws Throwable | //传入的新的参数去执行目标方法
两个方法.

[参考1](https://github.com/fangminx/execution-demo/edit/master/README.md)  
[参考2](https://blog.csdn.net/it_zouxiang/article/details/52576917)