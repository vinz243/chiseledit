package org.vinz243.chiseledit.tess;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TessCommand implements ICommand {
    @Override
    public String getName() {
        return "tess";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "tess";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("tess");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        TesselatorContainer container = TesselatorContainer.getInstance();
        WorldEdit instance = WorldEdit.getInstance();
        LocalSession session = instance.getSessionManager().findByName(Objects.requireNonNull(sender.getCommandSenderEntity()).getName());

        assert session != null;
        Region selection = null;
        try {
            selection = session.getSelection(session.getSelectionWorld());
        } catch (IncompleteRegionException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponentString("Incomplete selection!"));
            return;
        }

        container.revolute(new WorldEditRegion(selection.clone()), sender.getPosition(), sender.getEntityWorld());
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
