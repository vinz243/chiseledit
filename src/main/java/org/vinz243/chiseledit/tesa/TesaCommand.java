package org.vinz243.chiseledit.tesa;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
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
import java.util.stream.Stream;

public class TesaCommand implements ICommand {
    @Override
    public String getName() {
        return "tesa";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tesa revol|clear|update -<r|R|i>";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("tesa");
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        String flags = Arrays.stream(args).filter(str -> str.charAt(0) == '-').collect(Collectors.joining());

        ReplaceMode mode = flags.indexOf('R') > -1 ? ReplaceMode.AlwaysDestroy :
                (flags.indexOf('r') > -1 ? ReplaceMode.DestroyBeforeTessellate : ReplaceMode.Default);
        boolean infiniteRegion = flags.indexOf('i') > -1;


        switch (args[0]) {
            case "help":
            case "h":

                TextComponentString component = new TextComponentString("Tessellator usage:\n" +
                        "   -r    Destroy every block before tessellating\n" +
                        "   -R    Destroy each block every revolution" +
                        "   -i    Use an infinite region");
                sender.sendMessage(component);
                break;
            case "update":
                TessellatorContainer.getInstance().updateRegion(getRegion(sender, infiniteRegion));
                sender.sendMessage(new TextComponentString("Tessellator: updated region!"));
                return;
            case "revol":
            case "revolution":
                sender.sendMessage(new TextComponentString("Tessellating with mode " + mode));
                TessellatorContainer container = TessellatorContainer.getInstance();
                IRegion selection = getRegion(sender, infiniteRegion);
                if (selection == null) return;

                container.revolute(selection, sender.getPosition(), sender.getEntityWorld(), mode);

                break;
            case "clear":
            case "clr":
                TessellatorContainer.getInstance().clear();
                sender.sendMessage(new TextComponentString("Cleared tessellator !"));
                break;
            default:
                sender.sendMessage(new TextComponentString("Unable to determine command. Use revol or clear"));
                break;
        }
    }

    private IRegion getRegion(ICommandSender sender, boolean noRegion) {
        WorldEdit instance = WorldEdit.getInstance();
        LocalSession session = instance.getSessionManager().findByName(Objects.requireNonNull(sender.getCommandSenderEntity()).getName());

        assert session != null;
        IRegion selection = null;

        if (noRegion) {
            selection = new InfiniteRegion();
            sender.sendMessage(new TextComponentString("Tesa: using infinite region"));
        } else {
            try {
                selection = new WorldEditRegion(session.getSelection(session.getSelectionWorld()).clone());
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
                sender.sendMessage(new TextComponentString("Incomplete selection!"));
                return null;
            }
        }
        return selection;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        System.out.println(Arrays.toString(args));
        String arg = args[args.length - 1];
        return Stream.of("clear", "revolution", "update", "help").filter(el -> el.startsWith(arg)).collect(Collectors.toList());
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
