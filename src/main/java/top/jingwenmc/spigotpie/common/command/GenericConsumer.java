package top.jingwenmc.spigotpie.common.command;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class GenericConsumer implements Consumer<CommandItem> {
    private final Object targetObject;
    private final Method targetMethod;
    private final List<Parameter> methodParameters;
    private final int leastRequired;

    public GenericConsumer(Object o, Method m) throws TypeErrorException {
        targetObject = o;
        targetMethod = m;
        methodParameters = new ArrayList<>();
        boolean first = false;
        int leastRequired = 0;
        if(m.getParameterTypes().length == 1 && m.getParameterTypes()[0].getName().equalsIgnoreCase(SupportedTypes.COMMAND_ITEM)) {
            //Using raw CommandItem
            this.leastRequired = -1;
            return;
        }
        for(Parameter p : m.getParameters()){
            if(!first) {
                if(!Objects.equals(p.getType().getName(),SupportedTypes.COMMAND_SENDER)) {
                    throw new TypeErrorException("Unrecognized 1st parameter:"+p.getType().getName());
                }
                first = true;
                continue;
            }
            if(!Objects.equals(p.getType().getName(), SupportedTypes.STRING)) {
                throw new TypeErrorException("Unrecognized type:"+p.getType().getName());
            }
            if(!p.isAnnotationPresent(NotRequiredCommandParam.class)) {
                leastRequired++;
            }
            methodParameters.add(p);
        }
        this.leastRequired = leastRequired;
    }
    @SneakyThrows
    @Override
    public void accept(CommandItem commandItem) {
        if(leastRequired == -1) {
            targetMethod.invoke(targetObject,commandItem);
            return;
        }
        int length = commandItem.getArgs().length;
        if(length<leastRequired){
            commandItem.getSender().sendMessage(ChatColor.RED+"指令的参数不足！至少需要"+leastRequired+"个参数！");
            //TODO:Localized message
        }
        if(length> methodParameters.size()){
            commandItem.getSender().sendMessage(ChatColor.RED+"指令的参数过多！最多需要"+length+"个参数！");
            //TODO:Localized message
        }
        List<Object> parameters = new ArrayList<>();
        parameters.add(commandItem.getSender());
        if(methodParameters.size() - leastRequired != 1) {
            parameters.addAll(Arrays.asList(commandItem.getArgs()));
            for (int i = 0; i < methodParameters.size() - parameters.size() + 1; i++) {
                parameters.add(methodParameters.get(parameters.size() + i).getAnnotation(NotRequiredCommandParam.class).value());
            }
        } else {
            int p = 0;
            for(Parameter pp : methodParameters) {
                if(pp.isAnnotationPresent(NotRequiredCommandParam.class) && length == leastRequired) {
                    parameters.add(pp.getAnnotation(NotRequiredCommandParam.class).value());
                } else {
                    parameters.add(commandItem.getArgs()[p]);
                    p++;
                }
            }
        }
        targetMethod.invoke(targetObject,parameters.toArray());
    }

    public static class SupportedTypes {
        public static final String COMMAND_SENDER = CommandSender.class.getName();
        public static final String STRING = String.class.getName();
        public static final String COMMAND_ITEM = CommandItem.class.getName();

        //future: add more support type & check type
    }
}