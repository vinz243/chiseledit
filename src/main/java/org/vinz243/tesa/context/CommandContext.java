package org.vinz243.tesa.context;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.vinz243.tesa.helpers.Vector;

import javax.annotation.Nullable;

public class CommandContext extends Context {
    private final String[] remainingArgs;
    private final Vector pos1, pos2;

    public CommandContext(EntityPlayer player, World world, String[] remainingArgs) {
        this(player, world, remainingArgs, null, null);
    }

    public CommandContext(EntityPlayer player, World world, String[] remainingArgs, Vector a, Vector b) {
        super(player, world);
        this.remainingArgs = remainingArgs;
        this.pos1 = a;
        this.pos2 = b;
    }

    public String[] getRemainingArgs() {
        return remainingArgs;
    }

    public @Nullable Vector getPos1() {
        return pos1;
    }

    public @Nullable Vector getPos2() {
        return pos2;
    }
}
