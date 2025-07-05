### 支付模块

```text
本版本发布时间为 2021-05-01  适配jdk版本为 1.8
```

#### 1 配置
##### 1.1 添加依赖
```
<dependency>
    <groupId>com.na</groupId>
    <artifactId>na-pay</artifactId>
    <version>1.0.0</version>
</dependency>
        
或者

<dependency>
    <groupId>com.na</groupId>
    <artifactId>na-pay</artifactId>
    <version>1.0.0</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/../lib/na-pay-1.0.0.jar</systemPath>
</dependency>

相关依赖

        <dependency>
            <groupId>com.alipay.sdk</groupId>
            <artifactId>alipay-sdk-java</artifactId>
            <version>4.38.197.ALL</version>
        </dependency>
```

##### 1.2 配置
```yaml
na:
  pay:
    aliAppId: dddddd
    aliAppPrivateKey: MIIxxxx
    aliPublicKey: MIIBxxxx
    aliNotifyUrl: e
    aliReturnUrl: f
    aliAppCertPublicKey: ali/appCertPublicKey_202xxxx9.crt
    aliCertPublicKey: ali/alipayCertPublicKey_RSA2.crt
    aliRootCert: ali/alipayRootCert.crt
    # 默认为 true 在项目中读取 证书
    #    aliCertProject:
    # 默认为 false ， 秘钥模式
    aliCert: true
    # 默认 生产环境 https://openapi.alipay.com/gateway.do
#    aliGatewayUrl:


```

##### 1.3 使用
```java
    @Autowired
private INaPayExeService naPayExeService;
@Autowired
private INaCallbackInAsyncService naCallbackInAsyncService;

@Autowired
//    @Qualifier("myPayOrderGlobalServiceImpl")
private INaPayOrderGlobalService naPayOrderGlobalService;

@GetMapping("/test")
@NaAnonymousAccess
public void test(HttpServletResponse response) throws AlipayApiException, IOException {
    NaOrderPayDto naOrderPayDto = NaOrderPayDto.builder()
            .orderId("f4ewew23423f5erew")
            .title("测试")
            .totalAmount("0.01")
            .response(response)
            .build();

    response.setContentType("text/html;charset=UTF-8");
    response.getWriter().write(naPayExeService.payH5(naOrderPayDto, null).getAlipayTradeWapPayResponse().getBody());
    response.getWriter().flush();
    response.getWriter().close();
}

// 回调
@PostMapping("/huidiao")
@NaAnonymousAccess
public void test2() throws AlipayApiException {
    naCallbackInAsyncService.orderCallbackInAsync(naPayOrderGlobalService,null,null);
}



//@Service("myPayOrderGlobalServiceImpl")
@Service
public class MyPayOrderGlobalServiceImpl implements INaPayOrderGlobalService {
    @Override
    public NaPayStatus checkOrder(NaOrderCallbackInAsyncDto naOrderCallbackInAsyncDto, NaAutoPayConfig naAutoPayConfig) throws AlipayApiException {
        return null;
    }

    @Override
    public NaPayStatus updateOrder(NaOrderCallbackInAsyncDto naOrderCallbackInAsyncDto, NaAutoPayConfig naAutoPayConfig) {
        return null;
    }
}
```


# 【注意】启动类配置
```
如果你的包名不是以com.na开头的，需要配置
@ComponentScan(basePackages = {"com.na", "com.ziji.baoming"}) // 扫描多个包路径
```
