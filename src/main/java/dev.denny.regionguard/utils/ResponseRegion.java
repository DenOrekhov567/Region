package dev.denny.regionguard.utils;

import lombok.Getter;

public class ResponseRegion {
    @Getter
    public String name;
    @Getter
    public String ownerName;
    @Getter
    public int minX;
    @Getter
    public int minY;
    @Getter
    public int minZ;
    @Getter
    public int maxX;
    @Getter
    public int maxY;
    @Getter
    public int maxZ;
    @Getter
    public String worldName;
}
