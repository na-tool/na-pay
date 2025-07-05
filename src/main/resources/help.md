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
    # 默认为 false ， 全局回调
    callback: true
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

```jave
支付宝回调验签

        Map<String, String> paramsMap = PayHelper.paramsToMap(orderCallbackInAsyncDto.getRequest());

            /**
             * 验证签名
             */
            String sign = paramsMap.get("sign");
            String tradeNo = paramsMap.get("out_trade_no");
            String contentV1 = AlipaySignature.getSignCheckContentV1(paramsMap);
            Boolean signVerified = AlipaySignature.rsa256CheckContent(contentV1,sign,autoPayConfig.getAliPublicKey(),"UTF-8");

            NaPayStatus payEnum;
            /**
             * 1. 判断是否已经支付过直接返回
             * 2. 验证一些数据
             */
            if (!signVerified) {
                return NaPayStatus.SIGN_VERIFY_FAIL;
            } else {
                /**
                 * 验证支付状态
                 * String tradeStatus = request.getParameter("trade_status");
                 */
                String tradeStatus = paramsMap.get("trade_status");
                if (NaPayStatus.ALIPAY_TRADE_SUCCESS.enMsg().equals(tradeStatus)) {
                    /**
                     * 更新订单状态，执行一些业务逻辑
                     */
                    orderCallbackInAsyncDto.setOrderId(tradeNo);
                }
            }

        }

        return NaPayStatus.DATA_VERIFY_SUCCESS;

```

```java
微信支付验签
        NaAutoPayConfig config = NaAutoPayConfig.builder()
                .wxMchId("2121212")
                .wxAppId("wx1545454")
                .wxApiCertPath("D:\\apiclient_key.pem")
                .wxApiSerialNo("2CdsfsfsdfsdsdC")
                .wxPayPublicKeyPath("D:\\pub_key.pem")
                .wxPayPublicId("PUB_KEY_ID_045577757")
                .wxApiV3Key("gdfgdfgfdgdfgdgdfgdfgd")
                .wxNotifyUrl("http://na-tool.cn/app-service/api/app/pay/returnWX")
                .build();

        String body = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))
                .lines().collect(Collectors.joining("\n"));

        if(!NaWXPayUtility.verify(body,request, config)){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"code\":\"FAIL\",\"message\":\"签名验证失败\"}");
        }


        String plainText = NaWXPayUtility.decrypt(body, config);
        System.out.println("解密后的内容: " + plainText);
```


# 【注意】启动类配置
```
如果你的包名不是以com.na开头的，需要配置
@ComponentScan(basePackages = {"com.na", "com.ziji.baoming"}) // 扫描多个包路径
```
