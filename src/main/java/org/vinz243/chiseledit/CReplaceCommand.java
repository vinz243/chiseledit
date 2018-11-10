package org.vinz243.chiseledit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;
import mod.chiselsandbits.api.APIExceptions;
import mod.chiselsandbits.api.IBitAccess;
import mod.chiselsandbits.api.IChiselAndBitsAPI;
import mod.chiselsandbits.core.api.BitBrush;
import mod.chiselsandbits.helpers.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.vinz243.tesa.helpers.Vector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CReplaceCommand implements ICommand {
    @Override
    public String getName() {
        return "creplace";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/creplace <in> <out>";
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

        if (args.length != 2) throw new AssertionError();

        final String[] splat = args[0].split(":");
        int sourceId = Integer.parseInt(splat[0]);
        int sourceMeta = splat.length > 1 ? Integer.parseInt(splat[1]) : 0;

        final String[] target = args[1].split(":");
        int targetId = Integer.parseInt(target[0]);
        int targetMeta = target.length > 1 ? Integer.parseInt(target[1]) : 0;

        try {
            assert session != null;
            Region selection = session.getSelection(session.getSelectionWorld());

            selection.forEach((block) -> {
                final World entityWorld = entity.getEntityWorld();
                final BlockPos pos = new Vector(block).toBlockPos();
                final IBlockState state = entityWorld.getBlockState(pos);
                final int id = Block.getIdFromBlock(state.getBlock());
                final int meta = state.getBlock().getMetaFromState(state);

                final IChiselAndBitsAPI api = ChiselAPIAccess.apiInstance;
                IBlockState newState = Block.getBlockById(targetId).getStateFromMeta(targetMeta);

                if (api.isBlockChiseled(entityWorld, pos)) {

                    try {
                        final IBitAccess bitAccess = api.getBitAccess(entityWorld, pos);

                        bitAccess.visitBits(((x, y, z, brush) -> {
                            final IBlockState brushState = brush.getState();

                            final int brushId = Block.getIdFromBlock(brushState.getBlock());
                            final int brushMeta = brushState.getBlock().getMetaFromState(brushState);

                            if (brushId == sourceId && brushMeta == sourceMeta) {
                                return new BitBrush(ModUtil.getStateId(newState));
                            }

                            return brush;
                        }));
                        bitAccess.commitChanges(true);
                    } catch (APIExceptions.CannotBeChiseled cannotBeChiseled) {
                        cannotBeChiseled.printStackTrace();
                    }


                } else if (id == sourceId && meta == sourceMeta) {
                    entityWorld.setBlockState(pos, newState);
                }

            });
        } catch (Exception e) {
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
