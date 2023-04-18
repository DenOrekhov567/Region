package dev.denny.region.animation;

import cn.nukkit.level.Location;
import cn.nukkit.network.protocol.LevelEventPacket;

public class Animation {

    public static void add(Location location) {
        LevelEventPacket packet = new LevelEventPacket();
        packet.evid = LevelEventPacket.EVENT_PARTICLE_BLOCK_FORCE_FIELD;
        packet.x = (float) (location.getX() + 0.5);
        packet.y = (float) (location.getY() + 0.5);
        packet.z = (float) (location.getZ() + 0.5);
        packet.data = 0;
        location.getLevel().addChunkPacket(location.getFloorX() >> 4, location.getFloorZ() >> 4, packet);
    }
}