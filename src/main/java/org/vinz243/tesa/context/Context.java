package org.vinz243.tesa.context;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class Context {
    private final EntityPlayer player;
    private final World world;

    public Context(EntityPlayer player, World world) {
        this.player = player;
        this.world = world;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }
}
