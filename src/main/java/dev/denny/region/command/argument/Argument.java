package dev.denny.region.command.argument;

import dev.denny.account.player.Gamer;
import lombok.Getter;

abstract public class Argument {

    @Getter
    public String name;

    public abstract String getName();

    public abstract Boolean execute(Gamer sender, String[] args);
}