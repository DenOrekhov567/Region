package dev.denny.region.command.argument;

import cn.nukkit.Player;
import lombok.Getter;

abstract public class Argument {

    @Getter
    public String name;

    @Getter
    public Integer countPostArgs;

    @Getter
    public String prefixResponse = "§f[§aРегины§f] ";

    public abstract String getName();

    public abstract Boolean execute(Player sender, String[] args);

    public Argument setCountPostArgs(Integer count) {
        countPostArgs = count;

        return this;
    }
}