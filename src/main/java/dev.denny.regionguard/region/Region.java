package dev.denny.regionguard.region;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Region {

    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    private String ownerName;
    @Setter
    @Getter
    private Position minPosition;
    @Setter
    @Getter
    private Position maxPosition;

    public boolean isOwner(Player player) {
        return player.getName().equalsIgnoreCase(ownerName);
    }

}