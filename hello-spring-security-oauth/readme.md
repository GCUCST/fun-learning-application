启动后：
1.授权码模式（最安全）
网页端:(获取授权码，get)
http://localhost:8080/oauth/authorize?client_id=c1&&response_type=code&scope=all&redirect_uri=http://www.baidu.com
postman:(填入授权码，post)
http://localhost:8080/oauth/token?client_id=c1&client_secret=secret&grant_type=authorization_code&code=6rc7jG&redirect_uri=http://www.baidu.com

2.密码模式(客户端信任，泄露密码)
申请令牌（postman，post）
http://localhost:8080/oauth/token?client_id=c1&client_secret=secret&grant_type=password&username=lisi&password=123

3.客户端模式（完全信任）
申请令牌（post）
http://localhost:8080/oauth/token?client_id=c1&client_secret=secret&grant_type=client_credentials




开启两个服务

然后带上token访问资源服务器
localhost:9001/findAll