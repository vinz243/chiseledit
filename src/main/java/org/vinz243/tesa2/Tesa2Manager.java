package org.vinz243.tesa2;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.vinz243.tesa2.transforms.NoSuchTransformException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Tesa2Manager {

    private static final Tesa2Manager sInstance = new Tesa2Manager();

    private final Map<String, Tessellator> tessellators = new HashMap<>();

    public static Tesa2Manager getInstance() {
        return sInstance;
    }

    public void addTransform(String key, CommandContext context) throws NoSuchTransformException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String id = getPlayerId(context);
        if (!tessellators.containsKey(id)) {
            tessellators.put(id, new Tessellator());
        }
        Tessellator tessellator = tessellators.get(id);
        tessellator.addTransform(key, context);
    }

    public void clearTransforms(CommandContext context) {
        tessellators.get(getPlayerId(context)).clear();
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent evt) {
        final Context context = new Context(evt.getPlayer(), evt.getWorld());
        final String playerId = getPlayerId(context);

        if (!tessellators.containsKey(playerId)) return;

        BlockPos pos = evt.getPos();

        tessellators.get(playerId).apply(context.getWorld(), pos, evt.getPlacedBlock());
    }

    private String getPlayerId(Context context) {
        return context.getPlayer().getCachedUniqueIdString();
    }
}
