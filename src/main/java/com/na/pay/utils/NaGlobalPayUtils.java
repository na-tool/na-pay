package com.na.pay.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class NaGlobalPayUtils {

    /**
     * 检查字符串是否只包含中文、英文和数字
     * @param input 待检查的字符串
     * @return 如果字符串只包含中文、英文和数字，返回true；否则返回false
     */
    public static boolean isValidString(String input) {
        if (input == null || input.isEmpty()) {
            return false; // 空字符串或null不符合要求
        }
        /**
         * 定义正则表达式，只允许中文、英文和数字
         */
        String REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";
        Pattern PATTERN = Pattern.compile(REGEX);
        return PATTERN.matcher(input).matches();
    }

    /**
     *  获取当前时间戳（秒级，基于 Asia/Shanghai 时区）
     * @return 当前时间的秒级时间戳字符串
     */
    public static String getCurrentTimestampInSecondsAsString() {
        Long currentTimeMillis = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"))
                .toInstant()
                .toEpochMilli();
        /**
         * 转换为秒级别
         */
        Long currentSeconds = currentTimeMillis / 1000;
        /**
         * 将秒级别时间戳转换为字符串
         */
        return Long.toString(currentSeconds);
    }

    /**
     * 获取指定路径的绝对路径
     *
     * @param path 任意相对或绝对路径
     * @return 转换后的绝对路径字符串
     */
    public static String getAbsolutePath(String path) {
        Path absolutePath = Paths.get(path).toAbsolutePath();
        return absolutePath.toString();
    }

    /**
     * 获取资源文件在项目中运行时的绝对路径（支持 Windows 和 Linux）
     *
     * @param filePath 类路径下的资源文件相对路径（如 "certs/appCertPublicKey.crt"）
     * @return 文件的绝对路径
     * @throws IOException 获取文件失败时抛出
     */
    public static String getProjectAbsolutePath(String filePath) throws IOException {
        /**
         * 获取 ClassPathResource
         */
        Resource resource = new ClassPathResource(filePath);
        /**
         * 获取全路径
         */
        String osName = System.getProperty("os.name").toLowerCase();

        String fullPath = osName.startsWith("win") ?
                resource.getFile().getAbsolutePath() : getJarFilePath(filePath);
        return fullPath;
    }

    /**
     * 从 JAR 包中的资源路径读取指定文件，并复制到项目当前工作目录中对应的位置。
     * 如果目标文件所在目录不存在，则自动创建。
     * 如果目标文件不存在或大小为 0，则从 JAR 包中的资源提取并复制到工作目录。
     *
     * @param filePath 类路径下资源文件的相对路径（如 "fonts/NotoSerifSC-Regular.ttf"）
     * @return 复制后的文件绝对路径；如果出错，返回空字符串
     */
    private static String getJarFilePath(String filePath) {
        try {
            // 获取当前工作目录（JAR 所在目录）
            Path currentWorkingDir = Paths.get(System.getProperty("user.dir"));

            // 构造目标文件路径
            Path targetPath = currentWorkingDir.resolve(filePath);
            File targetFile = targetPath.toFile();

            // 确保上级目录存在
            if (!targetFile.getParentFile().exists()) {
                boolean created = targetFile.getParentFile().mkdirs();
                if (created) {
                    System.out.println("目录已创建: " + targetFile.getParentFile());
                }
            }

            // 如果目标文件不存在或为空，则从 classpath 中提取资源复制
            if (!targetFile.exists() || targetFile.length() == 0) {
                System.out.println("准备复制资源文件到: " + targetFile.getAbsolutePath());

                // 注意：路径必须是相对 resources 根目录的路径
                try (InputStream in = NaGlobalPayUtils.class.getClassLoader().getResourceAsStream(filePath)) {
                    if (in == null) {
                        System.err.println("资源未找到: " + filePath);
                        return "";
                    }
                    Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("资源复制完成: " + targetFile.getAbsolutePath());
                }
            }

            return targetPath.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getPath(String aliAppCertPublicKey, boolean aliCertProject) throws IOException {
        if (aliCertProject) {
            return getProjectAbsolutePath(aliAppCertPublicKey);
        } else {
            return getAbsolutePath(aliAppCertPublicKey);
        }
    }

    /**
     * 解析 HTTP 请求中的参数，
     * 并将所有参数封装成一个键值对的 Map 返回。
     *
     * @param request HTTP 请求对象，包含请求参数
     * @return 返回一个 Map，键为参数名，值为对应的第一个参数值（字符串）
     */
    public static Map<String, String> paramsToMap(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        /**
         * 获取http请求里面的所有键
         */
        Set<String> keySet = parameterMap.keySet();
        /**
         * 专门用来放置请求里面的参数
         */
        Map<String,String> paramsMap = new HashMap<>();
        for(String key : keySet){
            paramsMap.put(key, request.getParameter(key));
        }
        System.out.println("*************");
        System.out.println(paramsMap);
        return paramsMap;
    }
}
