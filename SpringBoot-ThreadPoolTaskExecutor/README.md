# Getting Started

### 来源

* [ThreadPoolTaskExecutor corePoolSize vs. maxPoolSize](https://www.baeldung.com/java-threadpooltaskexecutor-core-vs-max-poolsize)
* [CountDownLatch介绍](https://www.cnblogs.com/Andya/p/12925634.html)
* [Spring线程池ThreadPoolTaskExecutor用法](https://juejin.cn/post/7033369137543905311)
* [java线程池ThreadPoolExecutor原理源码分析](https://juejin.cn/post/7079605753094340644)

### ThreadPoolTaskExecutor

* corePoolSize, 最小值, 默认值为1
* maxPoolSize, 最大值, 默认值为Integer.MAX_VALUE
* queueCapacity, 阻塞队列, 默认值为Integer.MAX_VALUE
* keepAliveSeconds, 超出corePoolSize阈值线程的保活时间, 默认值为60, 

### 参数机制

提交一个task到ThreadPoolTaskExecutor时, 什么情况下会创建新thread?

1. 池中running threads < corePoolSize, 即使池中有idle thread;
2. 阻塞队列queueCapacity已满, 且running threads < maxPoolSize;

### 测试样例

1. 设置一个CountDownLatch(N), 
2. 接着向线程池中添加N个任务, 每个任务线程会睡眠0.1~1s, 然后执行CountDownLatch-1(全部执行完毕恰好为0)
3. 在CountDownLatch>0期间, 检查poolSize的值

### 实际结果

* NULL/NULL/NULL, 提交10个task, poolSize始终为1;
  * queueCapacity是无限的, 永远不会超过corePoolSize默认值1
* 5/NULL/NULL, 提交10个task, poolSize始终为5; 
  * queueCapacity是无限的, 永远不会超过corePoolSize默认值5
* 5/10/NULL, 提交10个task, poolSize始终为5;
  * corePoolSize是无限的, 永远不会超过corePoolSize默认值5
* 5/10/0, 提交10个task, poolSize始终为10;
  * corePoolSize是0, 会一直创建线程到最大值10
* 5/10/10, 提交20个task, poolSize始终为10
  * corePoolSize是10, queueCapacity到10以后就会创建新线程, 所以poolSize为10

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.0.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.0.2/maven-plugin/reference/html/#build-image)

