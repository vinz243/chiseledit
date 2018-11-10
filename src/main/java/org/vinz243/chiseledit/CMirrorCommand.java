package org.vinz243.chiseledit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.forge.ForgePlayer;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CMirrorCommand implements ICommand {
    @Override
    public String getName() {
        return "cflip";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/cflip <hor|ver>";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Entity entity = Objects.requireNonNull(sender.getCommandSenderEntity());
        WorldEdit instance = WorldEdit.getInstance();

        LocalSession session = instance.getSessionManager().findByName(entity.getName());
        ForgePlayer forgePlayer;
        try {
            Constructor<ForgePlayer> constructor = ForgePlayer.class.getDeclaredConstructor(EntityPlayerMP.class);
            constructor.setAccessible(true);
            forgePlayer = constructor.newInstance((EntityPlayerMP) entity);
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        try {
            assert session != null;
            Region selection = session.getSelection(session.getSelectionWorld());
            AffineTransform transform = new AffineTransform();
            Vector me = instance.getDirection(forgePlayer, "me");
            Vector add = me.positive().multiply(-2).add(1, 1, 1);
            System.out.println(transform.toString());
            selection.forEach((block) -> {
                ChiselUtils.transformBlock(
                        sender.getEntityWorld(),
                        new BlockPos(block.getBlockX(), block.getBlockY(), block.getBlockZ()),
                        transform.scale(add)
                );
            });
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponentString("Error !"));
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
