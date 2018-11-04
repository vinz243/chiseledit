package org.vinz243.tesa.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.vinz243.tesa.TesaManager;
import org.vinz243.tesa.context.CommandContext;
import org.vinz243.tesa.helpers.StringComponent;
import org.vinz243.tesa.transforms.NoSuchTransformException;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TesaCommand implements ICommand {
    @Override
    public String getName() {
        return "tesa";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/tesa <clear|add> <args>";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<>();
    }


    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        switch (args[0]) {
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
        }
    }

    private CommandContext getContext(ICommandSender sender, String[] args, int from) {
        return new CommandContext(
                (EntityPlayer) sender.getCommandSenderEntity(),
                sender.getEntityWorld(),
                Arrays.copyOfRange(args, from, args.length - 1)
        );
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
