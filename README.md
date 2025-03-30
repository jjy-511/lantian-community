## **技术架构**：  
框架：Spring Boot、Spring、Spring MVC、MyBatis  
中间件：Redis、Kafka、Elasticsearch  
安全与监控：Spring Security、Spring Actuator
<br><br>
## **开发环境**：  
构建工具：Apache Maven  
集成开发工具：IntelliJ IDEA  
数据库：MySQL、Redis  
应用服务器：Apache Tomcat  
版本控制工具：Git  
<br>
# **开发流程记录**：  
## 一、开发网站首页  
### 1.搭建数据库<br>
• 使用MySQL，建立user表和discuss_post表导入数据  
• 配置MyBatis，编写相应实体类及Mapper和Service<br>
### 2.首页开发   
• 实现后端逻辑 - 1次请求的执行过程（MVC）- 编写HomeController   
• 编写页面index.html并实现header和footer复用（Bootstrap+Themeleaf） <br>
• 开发社区首页，显示前10个帖子 - 开发分页组件（封装在Page类中），分页显示所有的帖子  
<br>
## 二、开发注册登录模块<br>
### 1. 开发注册功能<br>
#### • 编写register.html页面以及LoginController实现以下逻辑
• 访问注册页面- 点击顶部区域内的链接，打开注册页面<br>
• 提交注册数据- 规定表单输入规模- 通过表单提交数据- 服务端验证账号是否已存在、邮箱是否已注册- 服务端发送激活邮件<br>
• 激活注册账号- 点击邮件中的链接，访问服务端的激活服务<br>
• 注册完毕的账号数据存入数据库中，同时密码采用MD5加密和加盐的方式保证密码安全性<br>
• 编写CommunityUtil类封装表单空判定和MD5加密加盐功能<br>
### 2.实现注册时发送邮件激活功能<br>
• 邮箱设置- 启用客户端SMTP服务  
• Spring Email- 导入 jar 包  - 邮箱参数配置  - 使用 JavaMailSender 发送邮件  
• 编写MailClient类封装为邮件发送组件<br>
• 模板引擎- 使用 Thymeleaf 发送 HTML 邮件  
### 3.实现登录页面生成验证码图片<br>
• Kaptcha- 导入 jar 包- 编写 Kaptcha 配置类- 生成随机字符、生成图片<br>
• 编写refresh_kaptcha.js实现页面实时刷新验证码<br>
• 将验证码以session形式存入服务端进行验证<br>
### 4.实现登录，退出功能
#### • 在MySQL下新建login_ticket表作为登录凭证进行存储，并编写相应实体类、Mapper和Service
#### • 编写页面login.html实现下列逻辑
• 访问登录页面- 点击顶部区域内的链接，打开登录页面<br>
• 登录- 验证账号、密码、验证码- 成功时，生成登录凭证，发放给客户端- 失败时，跳转回登录页<br>
• 退出- 将登录凭证修改为失效状态- 跳转至登录首页<br>
### 5.显示登录信息
#### • 应用拦截器实现，编写LoginTicketInterceptor类
• 拦截器应用逻辑- 在请求开始时查询登录用户- 在本次请求中持有用户数据- 在模板视图上显示用户数据- 在请求结束时清理用户数据<br>
• 建立CookieUtil类封装获取Cookie方法，建立HostHolder类基于ThreadLocal实现多线程下的每个线程都能获取独立的User示例
### 6.实现用户修改头像和密码功能
#### • 编写setting.html用于作为用户修改账号信息页面及相应Controller实现下述逻辑
• 上传文件- 请求：必须是POST请求- 表单：enctype=“multipart/form-data”- Spring MVC：通过 MultipartFile 处理上传文件<br>
• 开发步骤- 访问账号设置页面- 上传头像- 获取头像<br>
• 用户输入原始密码- 原始密码效验- 用户输入新密码和二次确认新密码- 效验无误后同数据库更改密码- 设置登录状态Cookie失效<br>
### 7.考虑系统安全性，拦截用户未登录时对相关应用板块的访问
• 使用自定义注解，配置拦截器LoginRequiredInterceptor拦截未登录的访问
## 三、开发社区核心功能
### 1.使用Tire树实现敏感词过滤器
• 将敏感词过滤器封装为SensitiveFilter类存放到util中，过滤器中内置前缀树数据结构，从文件txt中读入敏感词构建前缀树，使用双指针遍历字符串，单指针遍历前缀树的方法，通过StringBuilder构建字符串的形式，完成对String中的敏感词过滤
### 2.使用AJAX技术开发发表帖子功能
• fastjson2- 导入jar包- 在CommunityUtil类中封装JSON处理逻辑<br>
• 完善discuss_post相应的Mapper和Service，编写相应Controller处理帖子插入数据库操作<br>
• 在index首页中完成前端发布帖子页面，编写index.js实现客户端和服务器异步通信，实现发布帖子实时置于首页
### 3.实现查看帖子详细
• 编写相应Controller，Service及Mapper实现访问逻辑<br>
• 编写discussPost-detail.html作为前端展示帖子静态页面
### 4.实现显示帖子评论和评论的回复
• 在数据库中创建Comment表，对该表进行定义<br>
• 数据层mapper- 根据实体查询一页评论数据- 根据实体查询评论的数量<br>
• 业务层service- 处理查询评论的业务和处理查询评论数量的业务<br>
• 表现层- 显示帖子详情数据时，同时显示该帖子所有的评论数据
### 5.实现发布评论功能
• 数据层mapper- 增加评论数据- 修改帖子的评论数量<br>
• 业务层service- 处理添加评论的业务：先增加评论、再更新帖子的评论数量，同时在此处应用Spring的声明式事务管理，将事务隔离级别设置为READ_COMMITTED<br>
• 表现层- 处理添加评论数据的请求- 设置添加评论的表单
### 6.实现私信查看功能  
#### 功能设计
• 私信列表- 查询当前用户的会话列表，每个会话只显示一条最新的私信- 支持分页显示<br>
• 私信详情- 查询某个会话所包含的私信- 支持分页显示
#### 实现过程
• 在数据库中创建Message表，对该表进行定义<br>
• 数据层mapper- 查询当前用户的会话列表,针对每个会话只返回一条最新的私信- 查询某个会话所包含的私信列表- 查询当前用户的会话数量- 查询某个会话所包含的私信数量- 查询未读私信的数量<br>
• 业务层service- 处理查询当前用户会话列表的业务- 处理查询某个会话包含的私信列表的业务- 处理查询当前用户会话数量的业务- 处理查询某个会话所包含私信数量的业务- 处理查询未读私信的业务<br>
• 表现层- 显示消息页面，分为系统通知和用户私信页面letter.html，和展示特定会话页面letter-detail.html（都复用分页功能）
### 7.实现私信发送功能
#### 功能设计
• 发送私信- 采用异步的方式发送私信- 发送成功后刷新私信列表
• 设置已读- 访问私信详情时，将显示的私信设置为已读状态
#### 实现过程
• 数据层mapper- 插入一个新增消息- 更新消息状态<br>
• 业务层service- 处理插入一个新增消息的业务- 处理更新消息状态的业务<br>
• 表现层- 在MessageController中设置发送消息路径- 编写letter.js实现异步发送私信
### 8.实现统一异常处理
• 编写404.html错误页和500.html错误页<br>
• 利用Spring的统一异常处理机制，创建ExceptionAdvice类用于统一捕获Controller的异常,异常后导向500.html页面<br>
• @ExceptionHandler - 用于修饰方法，该方法会在Controller出现异常后被调用，用于处理捕获到的异常
### 9.实现统一记录日志
• 应用Spring AOP，创建ServiceLogAspect类，在所有Service被访问前@Before进行日志记录，记录某用户在某时刻访问了某功能
## 四、引入Redis作为缓存高效实现功能与重构功能
### 1.引入Redis
• 引入依赖- spring-boot-starter-data-redis<br>
• 配置Redis- 配置数据库参数- 编写配置类，构造RedisTemplate
### 2.实现点赞功能
#### 功能设计
• 点赞- 支持对帖子、评论点赞- 第1次点赞，第2次取消点赞<br>
• 首页点赞数量- 统计帖子的点赞数量<br>
• 详情页点赞数量- 统计点赞数量- 显示点赞状态
#### 实现过程
• 数据层直接使用RedisTemplate，使用set集合对赞进行存储<br>
• 创建RedisKeyUtil类封装到util中，负责生成Redis的key，规定如下：like:entity:entityType:entityId  ->set(userId)<br>
• 业务层创建LikeService- 处理进行点赞的业务- 处理查询实体点赞数量的业务- 处理查询某用户对某实体点赞状态的业务<br>
• 表现层- 在DiscussPostController中添加点赞相关的处理- 编写discuss.js实现异步点赞- 完善discuss-detail.html的页面点赞显示
### 3.统计用户收到的赞
• 重构点赞功能- 以用户为key，记录点赞数量-like:user:userId  ->int- increment(key)，decrement(key) <br>
• 编写个人主页profile.html并配置UserController- 将项目中所有出现的用户头像链接到相对应的用户主页- 以用户为key，查询点赞数量
### 4.实现关注、取消关注功能
• 需求- 开发关注、取消关注功能- 统计用户的关注数、粉丝数<br>
• 关键- 若A关注了B，则A是B的Follower（粉丝），B是A的Followee（目标）- 关注的目标可以是用户、帖子、题目等，在实现时将这些目标抽象为实体<br>
• 实现- 使用Redis中的Zset存储，关注者：followee:userId:entityType ->zset(entityId,now)- 粉丝：follower:entityType:entityId ->zset(userId,now)<br>
-创建FollowService，在其中定义follow关注方法向Redis添加数据，定义unfollow取消关注方法移除数据，和三种查询方法- 创建FollowController完成页面映射调用Service<br>
-处理UserController与profile.html使得关注数量，粉丝数量正确显示
### 5.实现关注列表与粉丝列表
• 业务层实现对Redis的两个查询方法- 查询某个用户关注的人，支持分页- 查询某个用户的粉丝，支持分页<br>
• 表现层处理followee.html、follower.html正确处理页面显示- 处理“查询关注的人”、“查询粉丝”请求- 编写“查询关注的人”、“查询粉丝”模板<br>
### 6.优化登录板块
• 使用Redis存储验证码,修改Service和Controller- 验证码需要频繁的访问与刷新，对性能要求较高- 验证码不需永久保存，通常在很短的时间后就会失效- 考虑到进行分布式部署时，存在Session共享的问题<br>
• 使用Redis存储登录凭证,修改Service和Controller- 处理每次请求时，都要查询用户的登录凭证，访问的频率非常高<br>
• 使用Redis缓存用户信息- 处理每次请求时，都要根据凭证查询用户信息，访问的频率非常高- 查询时先访问缓存，缓存没有再访问数据库将数据记入缓存中，当数据发生修改时，采用“先更新数据库，再删除缓存策略”
## 五、引入Kafka作为消息队列实现消息通知
### 1.引入Kafka
• 安装并配置zookeeper，安装scale<br>
• 启动zookeeper后配置并启动kafka<br>
• 引入依赖- spring-kafka- 进行配置
### 2.使用Kafka发送系统通知
• 触发事件- 评论后，发布通知- 点赞后，发布通知- 关注后，发布通知<br>
• 处理事件- 封装事件对象Event类- 开发事件的生产者EventProducer- 开发事件的消费者EventConsumer<br>
• 在相应三种Controller上添加对应事件响应
### 3.实现显示系统通知
#### 需求
• 通知列表- 显示评论、点赞、关注三种类型的通知<br>
• 通知详情- 分页显示某一类主题所包含的通知<br>
• 未读消息- 在页面头部显示所有的未读消息数量
#### 具体实现
• 数据层mapper- 按类别查找最新通知- 查找通知数量- 查找未读通知数量- 查找通知列表<br>
• 业务层service- 处理Mapper的四个方法<br>
• 表现层- 在MessageController中设置两个页面逻辑- 完善letter.html- 创建notice.html- 创建notice-detail.html -建立拦截器MessageInterceptor拦截实现正式显示未读消息数
## 六、引入ElasticSearch作为分布式搜索引擎实现社区搜索
### 1.引入ElasticSearch
• 安装并配置ElasticSearch，发现Java版本不兼容，选择降低为Java8确保兼容性<br>
• 引入依赖spring-boot-starter-data-elasticsearch并配置<br>
• 解决Redis和ES的底层Netty依赖冲突<br>
### 2.实现帖子查找
