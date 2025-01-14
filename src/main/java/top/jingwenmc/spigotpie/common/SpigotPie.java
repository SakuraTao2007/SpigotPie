package top.jingwenmc.spigotpie.common;

import lombok.Getter;
import top.jingwenmc.spigotpie.PieDistroConfigurations;
import top.jingwenmc.spigotpie.common.instance.SimpleInstanceManager;

import java.util.logging.Level;

public class SpigotPie {
    @Getter
    private static PieEnvironment environment;

    public static void loadPlugin(PieEnvironment environment) {
        SpigotPie.environment = environment;
        environment.getLogger().info("Starting Library Spigot Pie...");
        environment.getLogger().info("正在加载Spigot Pie运行库...");
        char[] packageName = new char[]{'t','o','p','.','j','i','n','g','w','e','n','m','c','.','s','p','i','g','o','t','p','i','e','.','c','o','m','m','o','n'};
        if(!environment.isAsDedicatePlugin() && SpigotPie.class.getName().startsWith(new String(packageName))) {
            environment.getLogger().log(Level.SEVERE,"Failed to load Spigot Pie.");
            environment.getLogger().log(Level.SEVERE,"Spigot Pie加载失败.");
            environment.getLogger().log(Level.SEVERE,"可能的原因: 潜在的包名冲突");
            environment.getLogger().log(Level.SEVERE,"Reason: Possible Package Conflict");
            throw new IllegalStateException("Exception during PreLoad: Developer notice: ensure to use relocation while not using plugin mode!");
        }

        //start pie
        environment.getLogger().info("Loading managed instances...");
        environment.getLogger().info("正在加载受管理的实例...");
        try {
            SimpleInstanceManager.init();
            environment.getLogger().info("Managed instances load complete!");
            environment.getLogger().info("加载受管理的实例完成!");
            environment.getLogger().info("========================================\n"+
            "   _____       _             __     ____  _    \n" +
            "  / ___/____  (_)___ _____  / /_   / __ \\(_)__ \n" +
            "  \\__ \\/ __ \\/ / __ `/ __ \\/ __/  / /_/ / / _ \\\n" +
            " ___/ / /_/ / / /_/ / /_/ / /_   / ____/ /  __/\n" +
            "/____/ .___/_/\\__, /\\____/\\__/  /_/   /_/\\___/ \n" +
            "    /_/      /____/    Version:"+ PieDistroConfigurations.DISTRO_VERSION);
            environment.getLogger().info("========================================");
            environment.getLogger().info("Spigot Pie运行库加载完成!");
            environment.getLogger().info("Library Spigot Pie Load Complete!");
        } catch (Exception e) {
            environment.getLogger().log(Level.SEVERE,"Failed to load Spigot Pie.");
            environment.getLogger().log(Level.SEVERE,"Spigot Pie加载失败.");
            environment.getLogger().log(Level.SEVERE,"Check 'Caused by:' to learn more");
            environment.getLogger().log(Level.SEVERE,"查看'Caused by:'了解更多");
            throw new RuntimeException("Exception during Load: Unknown Exception",e);
        }
    }
}
