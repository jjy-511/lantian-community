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
