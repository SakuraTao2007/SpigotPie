package top.jingwenmc.spigotpie.spigot.command;

import lombok.SneakyThrows;
import top.jingwenmc.spigotpie.common.instance.*;
import top.jingwenmc.spigotpie.spigot.command.CommandManager;
import top.jingwenmc.spigotpie.common.command.GenericConsumer;
import top.jingwenmc.spigotpie.common.command.PieCommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

@PieComponent(platform = Platform.SPIGOT)
public class CommandProcessor implements PreProcessor {
    @Wire
    CommandManager commandManager;

    @Override
    public void preProcess(Object o) {
        //Didn't use this type of processor
    }

    @SneakyThrows
    @Override
    @Accepts(PieCommand.class)
    public void preProcess(Object o, Method m) {
        PieCommand pieCommand = m.getAnnotation(PieCommand.class);
        if(pieCommand.spigot()) {
            ArrayList<String> paths = new ArrayList<>();
            paths.add(pieCommand.value());
            paths.addAll(Arrays.asList(pieCommand.aliases()));
            for(String s : paths) {
                if(s.isEmpty())continue;
                commandManager.addCommandNode(s,new GenericConsumer(o,m));
            }
        }
    }
}
