package org.vinz243.tesa.commands;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.vinz243.tesa.TesaManager;
import org.vinz243.tesa.context.CommandContext;
import org.vinz243.tesa.exceptions.InvalidSyntaxException;
import org.vinz243.tesa.helpers.StringComponent;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.transforms.NoSuchTransformException;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TesaCommand implements ICommand {
    @Override
    public String getName() {
        return "tesa";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tesa <clear|add|visu|mask|pop> <args>";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        switch (args[0]) {
            case "pop":
                TesaManager.getInstance().popTransform(getContext(sender, args, 1));
                return;
            case "visu":
            case "debug":
                final CommandContext context = getContext(sender, args, 1);
                TesaManager.getInstance().debug(new Vector(context.getPlayer().getPosition()), context);
                return;
            case "list":
                TesaManager.getInstance().getTransforms(getContext(sender, args, 0)).forEach((tr) -> {
                    sender.sendMessage(new StringComponent(tr.toString()));
                });
                return;
            case "clear":
                TesaManager.getInstance().clearTransforms(getContext(sender, args, 0));
                sender.sendMessage(new StringComponent("Cleared tessellator!"));
                return;
            case "add":
                final String transformName = args[1];
                try {
                    TesaManager.getInstance().addTransform(transformName,
                            getContext(sender, args, 1));
                } catch (NoSuchTransformException e) {
                    sender.sendMessage(new StringComponent("Unable to find transform %s", transformName));
                } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    sender.sendMessage(new StringComponent("Unable to instantiate transform"));
                }
                return;
            case "mask":
                final CommandContext ct = getContext(sender, args, 1);
                try {
                    TesaManager.getInstance().setMask(ct);
                } catch (InvalidSyntaxException e) {
                    e.printStackTrace();
                    sender.sendMessage(new StringComponent("Invalid syntax, use: /tesa mask <transform|group|global> <input|output|both> <set|add|sub|int>"));
                }
                return;
        }
    }

    private CommandContext getContext(ICommandSender sender, String[] args, int from) {
        Entity entity = Objects.requireNonNull(sender.getCommandSenderEntity());
        WorldEdit instance = WorldEdit.getInstance();

        LocalSession session = instance.getSessionManager().findByName(entity.getName());

        Region selection = null;
        try {
            selection = session.getSelection(session.getSelectionWorld());
        } catch (IncompleteRegionException | NullPointerException e) {
            return new CommandContext(
                    (EntityPlayer) sender.getCommandSenderEntity(),
                    sender.getEntityWorld(),
                    Arrays.copyOfRange(args, from, args.length),
                    null, null);
        }
        return new CommandContext(
                (EntityPlayer) sender.getCommandSenderEntity(),
                sender.getEntityWorld(),
                Arrays.copyOfRange(args, from, args.length),
                new Vector(selection.getMinimumPoint()), new Vector(selection.getMaximumPoint()));
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
