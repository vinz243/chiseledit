package org.vinz243.chiseledit.tess;

import mod.chiselsandbits.api.APIExceptions;
import mod.chiselsandbits.api.EventBlockBitPostModification;
import mod.chiselsandbits.api.IBitAccess;
import mod.chiselsandbits.api.IChiselAndBitsAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.vinz243.chiseledit.ChiselAPIAccess;
import org.vinz243.chiseledit.ChiselUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.IntConsumer;

public class TesselatorContainer {
    private static TesselatorContainer sInstance;

    private List<IRegion> regions = new ArrayList<>();
    private BlockPos axis;
    private boolean destroyBeforeSet = false;
    private long lastTimeWarned;

    public static TesselatorContainer getInstance() {
        if (sInstance == null) {
            sInstance = new TesselatorContainer();
            MinecraftForge.EVENT_BUS.register(sInstance);
        }
        return sInstance;
    }

    void clear() {
        regions.clear();
        axis = null;
    }

    void revolute(IRegion region, BlockPos axis, World world, ReplaceMode mode) {
        regions.clear();
        regions.add(region);

        forEachAngle((angle) -> regions.add(region.transform(new RotationTransform(angle, ChiselUtils.toVec3d(axis)))));

        this.destroyBeforeSet = (mode != ReplaceMode.Default);
        this.axis = axis;

        region.forEach((pos) -> {
            IBlockState blockState = world.getBlockState(pos);
            revoluteBlock(pos, world, blockState);
        });

        this.destroyBeforeSet = (mode == ReplaceMode.AlwaysDestroy);
    }

    @SubscribeEvent
    void chiselPostEdited(EventBlockBitPostModification event) {
        BlockPos pos = event.getPos();
        World world = event.getWorld();
        Optional<IRegion> first = regions.stream().filter((reg) -> reg.contains(pos)).findFirst();

        if (first.isPresent()) {
            forEachAngle((int angle) -> {
                BlockPos out = this.transform(angle, pos);
                IChiselAndBitsAPI bitsAPI = ChiselAPIAccess.apiInstance;
                try {
                    IBitAccess outAccess = bitsAPI.getBitAccess(world, out);
                    IBitAccess inAccess = bitsAPI.getBitAccess(world, pos);

                    inAccess.visitBits((x, y, z, brush) -> {
                        BlockPos outPos = transform(angle, new Vec3d(7.5, 7.5, 7.5), new Vec3d(x, y, z));
                        try {
                            outAccess.setBitAt(outPos.getX(), outPos.getY(), outPos.getZ(), brush);
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

    }

    @SubscribeEvent
    void blockPlaced(BlockEvent.PlaceEvent evt) {
        BlockPos pos = evt.getPos();

        World world = evt.getWorld();
        IBlockState placedBlock = evt.getPlacedBlock();

        Optional<IRegion> first = regions.stream().filter((reg) -> reg.contains(pos)).findFirst();

        if (first.isPresent()) {
            if (destroyBeforeSet) {
                devoluteBlock(pos, world);
            }
            revoluteBlock(pos, world, placedBlock);
        }

        warnIfNecessary(pos, evt.getPlayer());
    }

    @SubscribeEvent
    void blockRemoved(BlockEvent.BreakEvent evt) {
        BlockPos pos = evt.getPos();
        World world = evt.getWorld();

        Optional<IRegion> first = regions.stream().filter((reg) -> reg.contains(pos)).findFirst();

        if (first.isPresent()) {
            devoluteBlock(pos, world);
        }

        warnIfNecessary(pos, evt.getPlayer());
    }

    void updateRegion(IRegion region) {
        regions.clear();
        regions.add(region);

        forEachAngle((angle) -> regions.add(region.transform(new RotationTransform(angle, ChiselUtils.toVec3d(axis)))));
    }

    private void warnIfNecessary(BlockPos pos, EntityPlayer player) {
        double distance = pos.getDistance(axis.getX(), axis.getY(), axis.getZ());
        long millis = System.currentTimeMillis();
        if (distance > 20 && millis - lastTimeWarned > 20e3) {
            lastTimeWarned = millis;
            player.sendMessage(new TextComponentString("Tessellator: still active!"));
        }
    }

    private void revoluteBlock(BlockPos pos, World world, IBlockState placedBlock) {
        forEachAngle((int angle) -> {
            BlockPos out = this.transform(angle, pos);
            world.setBlockState(out, placedBlock);
        });
    }

    private void devoluteBlock(BlockPos pos, World world) {
        forEachAngle((int angle) -> {
            BlockPos out = this.transform(angle, pos);
            world.destroyBlock(out, false);
        });
    }

    private BlockPos transform(int angle, BlockPos pos) {
        return transform(angle, ChiselUtils.toVec3d(pos));
    }

    private BlockPos transform(int angle, Vec3d axis, Vec3d pos) {
        return ChiselUtils.toBlockPos(new RotationTransform(angle, axis).apply(pos));
    }

    private BlockPos transform(int angle, Vec3d pos) {
        return transform(angle, ChiselUtils.toVec3d(axis), pos);
    }

    private void forEachAngle(IntConsumer fn) {
        Arrays.stream(new int[]{90, 180, 270}).forEach(fn);
    }
}
