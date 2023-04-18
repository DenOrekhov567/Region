package dev.denny.region.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import dev.denny.region.command.argument.AddMemberArgument;
import dev.denny.region.command.argument.Argument;
import dev.denny.region.command.argument.InfoArgument;
import dev.denny.region.command.argument.RemoveMemberArgument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBase extends Command {

    private List<Argument> argumentList;

    public CommandBase(String name, String description, String usage) {
        super(name, description, usage);

        getCommandParameters().clear();
        commandParameters.put("parameter1", new CommandParameter[]{
                CommandParameter.newEnum(
                        "команда",
                        new CommandEnum(
                         "command1",
                        "addmember",
                                "remmember"
                        )
                ),
                CommandParameter.newType("регон", CommandParamType.STRING),
                CommandParameter.newType("игрок", CommandParamType.TARGET)
        });
        commandParameters.put("parameter2", new CommandParameter[]{
                CommandParameter.newEnum(
                        "команда",
                        new CommandEnum(
                                "command2",
                                "info"
                        )
                ),
                CommandParameter.newType("регон", CommandParamType.STRING)
        });
        commandParameters.put("parameter3", new CommandParameter[]{
                CommandParameter.newEnum(
                        "команда",
                        new CommandEnum(
                                "command3",
                                "info"
                        )
                )
        });

        commandParameters.put("parameter2", new CommandParameter[] {
                CommandParameter.newEnum(
                        "команда",
                        new CommandEnum(
                         "command4",
                        "addmember",
                                "remmember",
                                "info"
                        )
                ),
                CommandParameter.newType("регон", CommandParamType.STRING),
                CommandParameter.newType("игрок", CommandParamType.STRING)
        });

        argumentList = new ArrayList<>();
        argumentList.add(new AddMemberArgument().setCountPostArgs(2));
        argumentList.add(new RemoveMemberArgument().setCountPostArgs(2));
        argumentList.add(new InfoArgument().setCountPostArgs(0));
    }

    private Argument getArgument(String name) {
        name = name.toLowerCase();
        for (Argument argument : argumentList) {
            if (!argument.getName().equals(name)) {
                continue;
            }
            return argument;
        }
        return null;
    }

    public Boolean executeSafe(Player sender, String[] args) {
        if (!sender.isPlayer()) {
            sender.sendMessage("§7> §fИспользовать можно только игроку");
            return false;
        }

        if(!sender.hasPermission(getPermission())) {
            sender.sendMessage("§7> §fНедостаточно прав");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(getUsage());
            return false;
        }

        Argument argument = getArgument(args[0]);

        if (argument == null) {
            sender.sendMessage(getUsage());

            return false;
        }

        String[] postArgs = Arrays.stream(args).skip(1).toArray(String[]::new);

        if(postArgs.length < argument.getCountPostArgs()) {
            sender.sendMessage("§7> §fДля команды §a" + argument.getName() + " §fтребуются еще аргументы");

            return false;
        }
        return argument.execute(sender, postArgs);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        return executeSafe((Player) sender, args);
    }
}