package org.vinz243.tesa.context;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CommandContext extends Context {
    private final String[] remainingArgs;

    public CommandContext(EntityPlayer player, World world, String[] remainingArgs) {
        super(player, world);
        this.remainingArgs = remainingArgs;
    }

    public String[] getRemainingArgs() {
        return remainingArgs;
    }
}
