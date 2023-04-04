package dev.denny.region.animation;

import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.LevelEventPacket;

public class Animation {

    public static void add(Position position) {
        LevelEventPacket packet = new LevelEventPacket();
        packet.evid = LevelEventPacket.EVENT_PARTICLE_BLOCK_FORCE_FIELD;
        packet.x = (float) (position.getX() + 0.5);
        packet.y = (float) (position.getY() + 0.5);
        packet.z = (float) (position.getZ() + 0.5);
        packet.data = 0;
        position.getLevel().addChunkPacket(position.getFloorX() >> 4, position.getFloorZ() >> 4, packet);
    }
}