package org.vinz243.tesa2;

import mod.chiselsandbits.api.APIExceptions;
import mod.chiselsandbits.api.EventBlockBitPostModification;
import mod.chiselsandbits.api.IBitAccess;
import mod.chiselsandbits.api.IChiselAndBitsAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.vinz243.tesa2.helpers.ChiselAPIAccess;
import org.vinz243.tesa2.helpers.Vector;
import org.vinz243.tesa2.transforms.NoSuchTransformException;
import org.vinz243.tesa2.transforms.Transform;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Tesa2Manager {

    private static final Tesa2Manager sInstance = new Tesa2Manager();

    private final Map<String, Tessellator> tessellators = new HashMap<>();

    public static Tesa2Manager getInstance() {
        return sInstance;
    }

    List<Transform> getTransforms(CommandContext context) {
        return tessellators.get(getPlayerId(context)).getTransforms();
    }

    void addTransform(String key, CommandContext context) throws NoSuchTransformException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String id = getPlayerId(context);
        if (!tessellators.containsKey(id)) {
            tessellators.put(id, new Tessellator());
        }
        Tessellator tessellator = tessellators.get(id);
        tessellator.addTransform(key, context);
    }

    void clearTransforms(CommandContext context) {
        tessellators.get(getPlayerId(context)).clear();
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent evt) {
        final Context context = new Context(evt.getPlayer(), evt.getWorld());
        final String playerId = getPlayerId(context);

        if (!tessellators.containsKey(playerId)) return;

        final BlockPos pos = evt.getPos();
        final IBlockState placedBlock = evt.getPlacedBlock();

        tessellators.get(playerId).apply(new Vector(pos), (position) -> {
            evt.getWorld().setBlockState(position.getVector().toBlockPos(), placedBlock);
        });
    }

    @SubscribeEvent
    public void onBlockDestroyed(BlockEvent.BreakEvent evt) {
        final Context context = new Context(evt.getPlayer(), evt.getWorld());
        final String playerId = getPlayerId(context);

        if (!tessellators.containsKey(playerId)) return;

        final BlockPos pos = evt.getPos();

        tessellators.get(playerId).apply(new Vector(pos), (position) -> {
            evt.getWorld().destroyBlock(position.getVector().toBlockPos(), false);
        });
    }

    @SubscribeEvent
    void onChiselEdited(EventBlockBitPostModification event) {
        final BlockPos pos = event.getPos();
        final EntityPlayer closestPlayer = event.getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 20, false);
        final Context context = new Context(Objects.requireNonNull(closestPlayer), event.getWorld());
        final String playerId = getPlayerId(context);

        if (!tessellators.containsKey(playerId)) return;

        final Tessellator tessellator = tessellators.get(playerId);

        if (tessellator.isChiselLocked()) {
            return;
        }

        tessellator.setChiselLocked(true);

        tessellator.apply(new Vector(pos), (result) -> {
            IChiselAndBitsAPI bitsAPI = ChiselAPIAccess.apiInstance;
            try {
                IBitAccess outAccess = bitsAPI.getBitAccess(event.getWorld(), result.getVector().toBlockPos());

                IBitAccess inAccess = bitsAPI.getBitAccess(event.getWorld(), pos);

                inAccess.visitBits((x, y, z, brush) -> {
                    Vector outPos = result.getChiselTransform().multiply(new Vector(x, y, z).add(-7.5)).add(7.5);
                    try {
                        outAccess.setBitAt((int) Math.round(outPos.getX()), (int) Math.round(outPos.getY()), (int) Math.round(outPos.getZ()), brush);
                    } catch (APIExceptions.SpaceOccupied spaceOccupied) {
                        spaceOccupied.printStackTrace();
                    }
                    return brush;
                });

                outAccess.commitChanges(true);
            } catch (APIExceptions.CannotBeChiseled cannotBeChiseled) {
                cannotBeChiseled.printStackTrace();
            }
        });

        tessellator.setChiselLocked(false);
    }

    private String getPlayerId(Context context) {
        return context.getPlayer().getCachedUniqueIdString();
    }
}
