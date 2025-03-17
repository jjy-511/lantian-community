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
• fastjson2- 导入jar包- 在CommunityUtil类中封装JSON处理逻辑
• 完善discuss_post相应的Mapper和Service，编写相应Controller处理帖子插入数据库操作
• 在index首页中完成前端发布帖子页面，编写index.js实现客户端端和服务器异步通信，实现发布帖子实时置于首页
