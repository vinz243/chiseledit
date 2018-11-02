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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TessCommand implements ICommand {
    @Override
    public String getName() {
        return "tess";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tess revol|clear -<r|R>";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("tess");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        String flags = Arrays.stream(args).filter(str -> str.charAt(0) == '-').collect(Collectors.joining());

        ReplaceMode mode = flags.indexOf('R') > -1 ? ReplaceMode.AlwaysDestroy :
                (flags.indexOf('r') > -1 ? ReplaceMode.DestroyBeforeTessellate : ReplaceMode.Default);

        sender.sendMessage(new TextComponentString("Tessellating with mode " + mode));

        switch (args[0]) {
            case "help":
            case "h":

                TextComponentString component = new TextComponentString("Tessellator usage:\n" +
                        "   -r    Destroy every block before tessellating\n" +
                        "   -R    Destroy each block every revolution");
                sender.sendMessage(component);
                break;
            case "revol":
            case "revolution":
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

                container.revolute(new WorldEditRegion(selection.clone()), sender.getPosition(), sender.getEntityWorld(), mode);

                break;
            case "clear":
            case "clr":
                TesselatorContainer.getInstance().clear();
                sender.sendMessage(new TextComponentString("Cleared tessellator !"));
                break;
            default:
                sender.sendMessage(new TextComponentString("Unable to determine command. Use revol or clear"));
                break;
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
