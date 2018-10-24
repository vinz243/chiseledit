package org.vinz243.chiseledit;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChiselCommand implements ICommand {
    @Override
    public String getName() {
        return "crot";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/crot <y-axis> [<x-axis>] [<z-axis>]";
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

        assert args.length >= 1;

        try {
            assert session != null;
            Region selection = session.getSelection(session.getSelectionWorld());

            selection.forEach((block) -> {
                ChiselUtils.rotateBlock(
                            sender.getEntityWorld(),
                            new BlockPos(block.getBlockX(), block.getBlockY(), block.getBlockZ()),
                            ChiselUtils.getMatrixForYRotation(Integer.parseInt(args[0]))
                );

                if (args.length > 1) {
                    ChiselUtils.rotateBlock(
                            sender.getEntityWorld(),
                            new BlockPos(block.getBlockX(), block.getBlockY(), block.getBlockZ()),
                            ChiselUtils.getMatrixForXRotation(Integer.parseInt(args[1]))
                    );
                }

                if (args.length > 2) {
                    ChiselUtils.rotateBlock(
                            sender.getEntityWorld(),
                            new BlockPos(block.getBlockX(), block.getBlockY(), block.getBlockZ()),
                            ChiselUtils.getMatrixForZRotation(Integer.parseInt(args[2]))
                    );
                }
            });
        } catch (IncompleteRegionException e) {
            e.printStackTrace();
            sender.sendMessage(new TextComponentString("Incomplete selection !"));
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
