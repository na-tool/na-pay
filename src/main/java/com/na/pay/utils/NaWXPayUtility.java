package com.na.pay.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.na.pay.config.NaAutoPayConfig;
import com.na.pay.dto.wx.NaResWXReturnResourceVO;
import com.na.pay.dto.wx.NaResWXReturnVO;
import okhttp3.Headers;
import okhttp3.Response;
import okio.BufferedSource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 微信支付工具类，提供微信支付APIv3相关的加密、签名、验签、
 * JSON处理、请求参数编码、响应处理等功能。
 */
public class NaWXPayUtility {

    // Jackson 的 ObjectMapper 实例，用于JSON序列化和反序列化
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            // 序列化时忽略null字段
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static final char[] SYMBOLS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final SecureRandom random = new SecureRandom();

    /**
     * 将任意对象序列化为 JSON 字符串
     *
     * @param object 需要序列化的对象
     * @return 对象的 JSON 字符串表示
     * @throws RuntimeException 序列化失败时抛出
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * 将 JSON 字符串反序列化为指定类型的对象
     *
     * @param json JSON字符串
     * @param classOfT 目标对象的类型 Class
     * @param <T> 泛型类型
     * @return 反序列化后的对象实例
     * @throws RuntimeException 反序列化失败时抛出
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return objectMapper.readValue(json, classOfT);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize JSON to object", e);
        }
    }

    /**
     * 读取文件内容为字符串，适用于读取密钥文件内容
     *
     * @param keyPath 文件路径
     * @return 文件内容字符串
     * @throws UncheckedIOException 读取文件失败时抛出
     */
    private static String readKeyStringFromPath(String keyPath) {
        try {
            return new String(Files.readAllBytes(Paths.get(keyPath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 从 PKCS#8 格式私钥字符串中加载 PrivateKey 对象
     * 该字符串应包含私钥的完整 PEM 格式内容
     *
     * @param keyString 私钥字符串内容，以 -----BEGIN PRIVATE KEY----- 开头
     * @return PrivateKey 私钥对象
     * @throws UnsupportedOperationException Java环境不支持RSA时抛出
     * @throws IllegalArgumentException 私钥内容无效时抛出
     */
    public static PrivateKey loadPrivateKeyFromString(String keyString) {
        try {
            keyString = keyString.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            return KeyFactory.getInstance("RSA").generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString)));
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 从文件路径加载 PKCS#8 格式私钥
     *
     * @param keyPath 私钥文件路径
     * @return PrivateKey 私钥对象
     */
    public static PrivateKey loadPrivateKeyFromPath(String keyPath) {
        return loadPrivateKeyFromString(readKeyStringFromPath(keyPath));
    }

    /**
     * 从 PKCS#8 格式公钥字符串中加载 PublicKey 对象
     * 该字符串应包含公钥的完整 PEM 格式内容
     *
     * @param keyString 公钥字符串内容，以 -----BEGIN PUBLIC KEY----- 开头
     * @return PublicKey 公钥对象
     * @throws UnsupportedOperationException Java环境不支持RSA时抛出
     * @throws IllegalArgumentException 公钥内容无效时抛出
     */
    public static PublicKey loadPublicKeyFromString(String keyString) {
        try {
            keyString = keyString.replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s+", "");
            return KeyFactory.getInstance("RSA").generatePublic(
                    new X509EncodedKeySpec(Base64.getDecoder().decode(keyString)));
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException(e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 从文件路径加载 PKCS#8 格式公钥
     *
     * @param keyPath 公钥文件路径
     * @return PublicKey 公钥对象
     */
    public static PublicKey loadPublicKeyFromPath(String keyPath) {
        return loadPublicKeyFromString(readKeyStringFromPath(keyPath));
    }

    /**
     * 生成指定长度的随机字符串，字符集为数字和大小写字母,字符集为[0-9a-zA-Z]，可用于安全相关用途
     *
     * @param length 生成的随机字符串长度
     * @return 随机字符串
     */
    public static String createNonce(int length) {
        char[] buf = new char[length];
        for (int i = 0; i < length; ++i) {
            buf[i] = SYMBOLS[random.nextInt(SYMBOLS.length)];
        }
        return new String(buf);
    }

    /**
     * 使用公钥对明文进行 RSA 加密（使用 RSA/ECB/OAEPWithSHA-1AndMGF1Padding 填充方式）
     *
     * @param publicKey 公钥对象
     * @param plaintext 待加密的明文字符串
     * @return 加密后Base64编码字符串
     * @throws IllegalArgumentException 加密失败时抛出
     */
    public static String encrypt(PublicKey publicKey, String plaintext) {
        final String transformation = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";

        try {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalArgumentException("The current Java environment does not support " + transformation, e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("RSA encryption using an illegal publicKey", e);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalArgumentException("Plaintext is too long", e);
        }
    }

    /**
     * 使用私钥对消息进行数字签名
     *
     * @param message 待签名的字符串
     * @param algorithm 签名算法名称，如 "SHA256withRSA"
     * @param privateKey 私钥对象
     * @return 签名后的Base64编码字符串
     * @throws UnsupportedOperationException 不支持指定算法时抛出
     * @throws IllegalArgumentException 私钥非法时抛出
     * @throws RuntimeException 签名过程中出现异常时抛出
     */
    public static String sign(String message, String algorithm, PrivateKey privateKey) {
        byte[] sign;
        try {
            Signature signature = Signature.getInstance(algorithm);
            signature.initSign(privateKey);
            signature.update(message.getBytes(StandardCharsets.UTF_8));
            sign = signature.sign();
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("The current Java environment does not support " + algorithm, e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(algorithm + " signature uses an illegal privateKey.", e);
        } catch (SignatureException e) {
            throw new RuntimeException("An error occurred during the sign process.", e);
        }
        return Base64.getEncoder().encodeToString(sign);
    }

    /**
     * 使用公钥验证数字签名
     *
     * @param message 原始消息字符串
     * @param signature 签名的Base64编码字符串
     * @param algorithm 签名算法名称，如 "SHA256withRSA"
     * @param publicKey 公钥对象
     * @return true表示验证通过，false表示验证失败
     * @throws IllegalArgumentException 公钥非法时抛出
     * @throws UnsupportedOperationException 不支持算法时抛出
     */
    public static boolean verify(String message, String signature, String algorithm,
                                 PublicKey publicKey) {
        try {
            Signature sign = Signature.getInstance(algorithm);
            sign.initVerify(publicKey);
            sign.update(message.getBytes(StandardCharsets.UTF_8));
            return sign.verify(Base64.getDecoder().decode(signature));
        } catch (SignatureException e) {
            return false;
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("verify uses an illegal publickey.", e);
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("The current Java environment does not support" + algorithm, e);
        }
    }

    /**
     * 根据微信支付APIv3请求签名规则构造 Authorization 签名
     *
     * @param mchid 商户号
     * @param certificateSerialNo 商户API证书序列号
     * @param privateKey 商户API证书私钥
     * @param method 请求接口的HTTP方法，请使用全大写表述，如 GET、POST、PUT、DELETE
     * @param uri 请求接口的URL
     * @param body 请求接口的Body
     * @return 构造好的微信支付APIv3 Authorization 头
     */
    public static String buildAuthorization(String mchid, String certificateSerialNo,
                                            PrivateKey privateKey,
                                            String method, String uri, String body) {
        String nonce = createNonce(32);
        long timestamp = Instant.now().getEpochSecond();

        String message = String.format("%s\n%s\n%d\n%s\n%s\n", method, uri, timestamp, nonce,
                body == null ? "" : body);

        String signature = sign(message, "SHA256withRSA", privateKey);

        return String.format(
                "WECHATPAY2-SHA256-RSA2048 mchid=\"%s\",nonce_str=\"%s\",signature=\"%s\"," +
                        "timestamp=\"%d\",serial_no=\"%s\"",
                mchid, nonce, signature, timestamp, certificateSerialNo);
    }

    /**
     * 对字符串进行 URL 编码（UTF-8）
     *
     * @param content 待编码字符串
     * @return 编码后的字符串
     * @throws RuntimeException 编码失败时抛出
     */
    public static String urlEncode(String content) {
        try {
            return URLEncoder.encode(content, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 对参数Map进行 URL 编码，生成 QueryString
     *
     * @param params Query参数Map
     * @return QueryString
     */
    public static String urlEncode(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        int index = 0;
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            result.append(entry.getKey())
                    .append("=")
                    .append(urlEncode(entry.getValue().toString()));
            index++;
            if (index < params.size()) {
                result.append("&");
            }
        }
        return result.toString();
    }

    /**
     * 从应答中提取 Body
     *
     * @param response HTTP 请求应答对象
     * @return 应答中的Body内容，Body为空时返回空字符串
     */
    public static String extractBody(Response response) {
        if (response.body() == null) {
            return "";
        }

        try {
            BufferedSource source = response.body().source();
            return source.readUtf8();
        } catch (IOException e) {
            throw new RuntimeException(String.format("An error occurred during reading response body. Status: %d", response.code()), e);
        }
    }

    /**
     * 根据微信支付APIv3应答验签规则对应答签名进行验证，验证不通过时抛出异常
     *
     * @param wechatpayPublicKeyId 微信支付公钥ID
     * @param wechatpayPublicKey 微信支付公钥对象
     * @param headers 微信支付应答 Header 列表
     * @param body 微信支付应答 Body
     */
    public static void validateResponse(String wechatpayPublicKeyId, PublicKey wechatpayPublicKey,
                                        Headers headers,
                                        String body) {
        String timestamp = headers.get("Wechatpay-Timestamp");
        try {
            Instant responseTime = Instant.ofEpochSecond(Long.parseLong(timestamp));
            // 拒绝过期请求
            if (Duration.between(responseTime, Instant.now()).abs().toMinutes() >= 5) {
                throw new IllegalArgumentException(
                        String.format("Validate http response,timestamp[%s] of httpResponse is expires, "
                                        + "request-id[%s]",
                                timestamp, headers.get("Request-ID")));
            }
        } catch (DateTimeException | NumberFormatException e) {
            throw new IllegalArgumentException(
                    String.format("Validate http response,timestamp[%s] of httpResponse is invalid, " +
                                    "request-id[%s]", timestamp,
                            headers.get("Request-ID")));
        }
        String message = String.format("%s\n%s\n%s\n", timestamp, headers.get("Wechatpay-Nonce"),
                body == null ? "" : body);
        String serialNumber = headers.get("Wechatpay-Serial");
        if (!Objects.equals(serialNumber, wechatpayPublicKeyId)) {
            throw new IllegalArgumentException(
                    String.format("Invalid Wechatpay-Serial, Local: %s, Remote: %s", wechatpayPublicKeyId,
                            serialNumber));
        }
        String signature = headers.get("Wechatpay-Signature");

        boolean success = verify(message, signature, "SHA256withRSA", wechatpayPublicKey);
        if (!success) {
            throw new IllegalArgumentException(
                    String.format("Validate response failed,the WechatPay signature is incorrect.%n"
                                    + "Request-ID[%s]\tresponseHeader[%s]\tresponseBody[%.1024s]",
                            headers.get("Request-ID"), headers, body));
        }
    }

    @SuppressWarnings("unchecked")
    public static Object removeEmptyStrings(Object obj) {
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                Object value = entry.getValue();
                if (value == null) {
                    it.remove();
                } else if (value instanceof String && ((String) value).isEmpty()) {
                    it.remove();
                } else {
                    // 递归处理Map或List等复合类型（这里示例Map）
                    removeEmptyStrings(value);
                }
            }
            return map;
        }
        // 如果是List或其他类型，这里可继续扩展
        return obj;
    }

    public static String decrypt(String associatedData, String nonce, String ciphertext,String apiV3Key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec key = new SecretKeySpec(apiV3Key.getBytes(StandardCharsets.UTF_8), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));
            byte[] result = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证微信支付回调通知
     * @param request
     * @param config
     * @return
     * @throws IOException
     */
    public static Boolean verify(String body,HttpServletRequest request, NaAutoPayConfig config) throws IOException {
        // 1. 从微信支付请求头中提取必要字段（用于验签）
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String signatureBase64 = request.getHeader("Wechatpay-Signature"); // 签名值（Base64 编码）
        String serial = request.getHeader("Wechatpay-Serial");             // 证书序列号

        // 2. 读取请求体（JSON 数据），按 UTF-8 解码并拼接成字符串
//        String body = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))
//                .lines().collect(Collectors.joining("\n"));

        // 3. 构造微信支付签名消息格式：timestamp + "\n" + nonce + "\n" + body + "\n"
        String message = String.format("%s\n%s\n%s\n", timestamp, nonce, body == null ? "" : body);
        // 4. 校验证书序列号是否与配置一致（确保是微信发来的请求）
        if (!config.getWxPayPublicId().equals(serial)) {
            return false;
        }
        // 5. 使用微信平台公钥验证签名合法性（SHA256withRSA 算法）
        return NaWXPayUtility.verify(message, signatureBase64, "SHA256withRSA", config.getPayPublicKey());
    }

    /**
     * 解密微信支付回调通知
     * @param body
     * @param config
     * @return
     * @throws JsonProcessingException
     */
    public static String decrypt(String body,NaAutoPayConfig config) throws JsonProcessingException {
        // 6. 解析请求体 JSON 为业务对象
        NaResWXReturnVO vo = new ObjectMapper().readValue(body, NaResWXReturnVO.class);
        // 7. 从 VO 中获取加密资源字段 resource
        NaResWXReturnResourceVO resource = vo.getResource();
        // 8. 解密 resource 中的密文 ciphertext，得到明文通知数据
        return NaWXPayUtility.decrypt(
                resource.getAssociated_data(),
                resource.getNonce(),
                resource.getCiphertext(),
                config.getWxApiV3Key());
    }


}
