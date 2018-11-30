package org.vinz243.tesa;

import mod.chiselsandbits.api.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.vinz243.tesa.annotations.CursorTarget;
import org.vinz243.tesa.context.CommandContext;
import org.vinz243.tesa.context.Context;
import org.vinz243.tesa.exceptions.InvalidSyntaxException;
import org.vinz243.tesa.helpers.ChiselAPIAccess;
import org.vinz243.tesa.helpers.TesaCursor;
import org.vinz243.tesa.helpers.Vector;
import org.vinz243.tesa.masks.ComposedMaskFactory;
import org.vinz243.tesa.masks.IMaskable;
import org.vinz243.tesa.masks.MaskFactory;
import org.vinz243.tesa.transforms.NoSuchTransformException;
import org.vinz243.tesa.transforms.Transform;
import org.vinz243.tesa.visu.Visualizer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;

public class TesaManager {

    private static EnumParticleTypes[] PARTICLE_CYCLE = {
            EnumParticleTypes.SMOKE_NORMAL,
            EnumParticleTypes.SPELL_MOB,
            EnumParticleTypes.ENCHANTMENT_TABLE,
            EnumParticleTypes.VILLAGER_HAPPY
    };

    private static final TesaManager sInstance = new TesaManager();

    private final Map<String, Tessellator> tessellators = new HashMap<>();

    public static TesaManager getInstance() {
        return sInstance;
    }

    public void debug(Vector pos, CommandContext context) {
        final List<Transform> transforms = getTransforms(context);

        final String[] args = context.getRemainingArgs();
        int size = args.length > 0 ? Integer.valueOf(args[0]) : 16;

        IntStream.range(0, transforms.size()).forEach((i) -> {
            final Visualizer visualizer = transforms.get(i).getVisualizer();

            visualizer.getVertices(pos.add(-size), pos.add(size)).forEach((vec) -> {
                spawnParticleAt(context, PARTICLE_CYCLE[i % PARTICLE_CYCLE.length], vec);
            });
        });
    }

    private void spawnParticleAt(CommandContext context, EnumParticleTypes type, Vector vec) {
        ((WorldServer) context.getWorld()).spawnParticle(
                (EntityPlayerMP) context.getPlayer(),
                type,
                true, vec.getX(), vec.getY(), vec.getZ(), 3, 0, 0, 0, 0);
    }

    public List<Transform> getTransforms(CommandContext context) {
        return tessellators.get(getPlayerId(context)).getTransforms();
    }

    public void addTransform(String key, CommandContext context) throws NoSuchTransformException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Tessellator tessellator = getTessellator(context);
        tessellator.addTransform(key, context);
    }

    private Tessellator getTessellator(CommandContext context) {
        String id = getPlayerId(context);
        return tessellators.computeIfAbsent(id, k -> new Tessellator());
    }

    public void clearTransforms(CommandContext context) {
        tessellators.get(getPlayerId(context)).clear();
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        final Tessellator tessellator = tessellators.get(event.getEntity().getCachedUniqueIdString());
        if (tessellator != null) {
            tessellator.setEnabled(false);
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent evt) {
        final Context context = new Context(evt.getPlayer(), evt.getWorld());
        final String playerId = getPlayerId(context);

        if (!tessellators.containsKey(playerId)) return;

        final BlockPos pos = evt.getPos();
        System.out.println("OPEN pos = " + pos);
        final IBlockState placedBlock = evt.getPlacedBlock();

        final Tessellator tessellator = tessellators.get(playerId);

        if (tessellator.isEnabled()) {
            tessellator.apply(new Vector(pos), (position) -> {
                System.out.println("position = " + position);
                evt.getWorld().setBlockState(position.getVector().toBlockPos(), placedBlock);
            });
        }
    }

    @SubscribeEvent
    public void onBlockDestroyed(BlockEvent.BreakEvent evt) {
        final Context context = new Context(evt.getPlayer(), evt.getWorld());
        final String playerId = getPlayerId(context);

        if (!tessellators.containsKey(playerId)) return;

        final BlockPos pos = evt.getPos();

        final Tessellator tessellator = tessellators.get(playerId);

        if (tessellator.isEnabled()) {
            tessellator.apply(new Vector(pos), (position) -> {
                evt.getWorld().destroyBlock(position.getVector().toBlockPos(), false);
            });
        }
    }

    @SubscribeEvent
    void onBitModification(EventBlockBitModification event) {
        final Context context = new Context(event.getPlayer(), event.getWorld());
        final String playerId = getPlayerId(context);

        if (!tessellators.containsKey(playerId)) return;

        final Tessellator tessellator = tessellators.get(playerId);

        tessellator.setChiselLocked(false);
    }

    @SubscribeEvent
    void onChiselEdited(EventBlockBitPostModification event) {
        final BlockPos pos = event.getPos();
        final EntityPlayer closestPlayer = event.getWorld().getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 20, false);

        if (closestPlayer == null) return;

        final Context context = new Context(closestPlayer, event.getWorld());
        final String playerId = getPlayerId(context);

        if (!tessellators.containsKey(playerId)) return;

        final Tessellator tessellator = tessellators.get(playerId);


        if (tessellator.isChiselLocked() || !tessellator.isEnabled()) {
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

    }

    private String getPlayerId(Context context) {
        return context.getPlayer().getCachedUniqueIdString();
    }

    public void setMask(CommandContext ct) throws InvalidSyntaxException {
        final Tessellator tessellator = getTessellator(ct);
        final String maskTarget = ct.getRemainingArgs()[0];

        final TesaCursor cursor = tessellator.getCursor();

        final Method[] declaredMethods = cursor.getClass().getDeclaredMethods();

        final Optional<Method> targetMethod = Arrays.stream(declaredMethods).filter((method) -> {
            try {
                final CursorTarget cursorTarget = method.getAnnotation(CursorTarget.class);

                return Arrays.asList(cursorTarget.name()).contains(maskTarget);
            } catch (NullPointerException ignored) {
            }
            return false;
        }).findFirst();

        targetMethod.orElseThrow(InvalidSyntaxException::new);

        final Method method = targetMethod.get();
        try {
            final IMaskable maskable = (IMaskable) method.invoke(cursor);

            final MaskFactory maskFactory;

            switch (ct.getRemainingArgs()[1]) {
                case "i":
                case "in":
                case "input":
                    maskFactory = maskable.getInputMaskFactory();
                    break;
                case "o":
                case "out":
                case "output":
                    maskFactory = maskable.getOutputMaskFactory();
                    break;
                case "b":
                case "both":
                    maskFactory = new ComposedMaskFactory(maskable.getInputMaskFactory(), maskable.getOutputMaskFactory());
                    break;
                default:
                    throw new InvalidSyntaxException("Use either input, output or both");
            }

            switch (ct.getRemainingArgs()[2]) {
                case "add":
                    maskFactory.add(ct.getPos1(), ct.getPos2());
                    break;
                case "sub":
                    maskFactory.sub(ct.getPos1(), ct.getPos2());
                    break;
                case "set":
                    maskFactory.set(ct.getPos1(), ct.getPos2());
                    break;
                case "int":
                    maskFactory.intersection(ct.getPos1(), ct.getPos2());
                    break;
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void popTransform(CommandContext context) {
        getTessellator(context).pop();
    }

    public void disable(CommandContext context) {
        getTessellator(context).setEnabled(false);
    }

    public void enable(CommandContext context) {
        getTessellator(context).setEnabled(true);
    }
}
