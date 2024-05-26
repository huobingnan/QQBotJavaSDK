package buddha.qqbot;

import buddha.qqbot.message.Intents;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

public class QQBotProperties extends Properties {

    QQBotProperties() {
        // 配置缺省的默认配置
        put("qqbot.message.intents", Intents.all());
        loadFromClasspath();
        loadFromWorkDirProperties();
        loadFromSystemProperties();
        loadFromEnv();
    }

    private void loadFromEnv() {
        Optional.ofNullable(System.getenv("QQ_BOT_APP_ID"))
                .ifPresent(it -> setProperty("qqbot.appId", it));
        Optional.ofNullable(System.getenv("QQ_BOT_SECRET"))
                .ifPresent(it -> setProperty("qqbot.secret", it));
    }

    private void loadFromSystemProperties() {
        Optional.ofNullable(System.getProperty("qqbot.appId"))
                .ifPresent(it -> setProperty("qqbot.appId", it));
        Optional.ofNullable(System.getProperty("qqbot.secret"))
                .ifPresent(it -> setProperty("qqbot.secret", it));
    }

    /**
     * 从当前程序的工作目录加载<b>qqbot.properties</b>文件
     */
    private void loadFromWorkDirProperties() {
        final String workDir = System.getProperty("user.dir");
        final File propsFile = new File(workDir, "qqbot.properties");
        if (!propsFile.exists()) return;
        try (final FileReader fr = new FileReader(propsFile)) {
            load(fr);
        } catch (IOException ex) {
            throw new RuntimeException("QQBot配置文件【qqbot.properties】@ 当前工作目录加载失败", ex);
        }
    }

    private void loadFromClasspath() {
        final InputStream is =
                Thread.currentThread().getContextClassLoader().getResourceAsStream("/qqbot.properties");
        if (is == null) return;
        try {
            load(is);
        } catch (IOException e) {
            throw new RuntimeException("QQBot配置文件【qqbot.properties】@ ClassPath 加载失败", e);
        }
    }
}
