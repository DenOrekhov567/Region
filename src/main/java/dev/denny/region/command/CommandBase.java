package dev.denny.region.command;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import dev.denny.account.player.Gamer;
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
        commandParameters.put("regionParameter1", new CommandParameter[]{
                CommandParameter.newEnum(
                        "regionEnum",
                        new CommandEnum(
                         "команда",
                        "addmember",
                                "remmember",
                                "info"
                        )
                ),
                CommandParameter.newType("регон", CommandParamType.STRING),
                CommandParameter.newType("игрок", CommandParamType.TARGET)
        });

        commandParameters.put("regionParameter2", new CommandParameter[] {
                CommandParameter.newEnum(
                        "regionEnum",
                        new CommandEnum(
                         "команда",
                        "addmember",
                                "remmember",
                                "info"
                        )
                ),
                CommandParameter.newType("регон", CommandParamType.STRING),
                CommandParameter.newType("имя", CommandParamType.STRING)
        });

        argumentList = new ArrayList<>();
        argumentList.add(new AddMemberArgument());
        argumentList.add(new RemoveMemberArgument());
        argumentList.add(new InfoArgument());
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

    public Boolean executeSafe(Gamer sender, String[] args) {
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
        return argument.execute(sender, Arrays.stream(args).skip(1).map(String::toLowerCase).toArray(String[]::new));
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        return executeSafe((Gamer) sender, args);
    }
}