技术架构：  
• Spring Boot  
• Spring、Spring MVC、MyBatis  
• Redis、Kafka、Elasticsearch  
• Spring Security、Spring Actuator  
开发环境：  
• 构建工具：Apache Maven  
• 集成开发工具：IntelliJ IDEA  
• 数据库：MySQL、Redis  
• 应用服务器：Apache Tomcat  
• 版本控制工具：Git  
开发流程记录：
一、开发网站首页
1.搭建数据库
• 使用MySQL，建表导入数据  
• 配置MyBatis，编写相应的Mapper  
2.

二、开发注册登录模块
1.注册时发送邮件  
• 邮箱设置 - 启用客户端SMTP服务  
• Spring Email - 导入 jar 包  - 邮箱参数配置  - 使用 JavaMailSender 发送邮件  
• 模板引擎  - 使用 Thymeleaf 发送 HTML 邮件  
