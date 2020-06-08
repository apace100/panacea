package io.github.apace100.panacea.block.entity;

import io.github.apace100.panacea.advancement.CodeTriggerCriterion;
import io.github.apace100.panacea.block.AltarFrameBlock;
import io.github.apace100.panacea.component.AltarLookupComponent;
import io.github.apace100.panacea.registry.ModBlockEntities;
import io.github.apace100.panacea.registry.ModBlocks;
import io.github.apace100.panacea.registry.ModComponents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

public class EnderAltarBlockEntity extends BlockEntity {

    private String altarId;

    public EnderAltarBlockEntity() {
        this(ModBlockEntities.ALTAR);
    }

    public EnderAltarBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    public boolean tryActivate() {
        List<BlockPos> frames = new LinkedList<>();
        if(isValidStructure(world, pos, frames)) {
            if(frames.stream().allMatch(s -> world.getBlockState(s).get(AltarFrameBlock.EYE))) {
                beginTeleportation();
                frames.forEach(p -> AltarFrameBlock.consume(world.getBlockState(p), world, p));
            }
            return true;
        } else {
            frames.forEach(p -> AltarFrameBlock.popEye(world.getBlockState(p), world, p));
        }
        return false;
    }

    private static boolean isValidStructure(World world, BlockPos pos, List<BlockPos> framePositions) {
        BlockPos.Mutable p = new BlockPos.Mutable();
        boolean isValid = true;
        for(int dIndex = 0; dIndex < 4; dIndex++) {
            Direction d = Direction.fromHorizontal(dIndex);
            p.set(pos);
            p.move(Direction.UP);
            p.move(d);
            if(!world.isAir(p)) {
                isValid = false;
            }
            for(int i = 0; i < 2; i++) {
                p.move(Direction.UP);
                if(!world.isAir(p)) {
                    isValid = false;
                }
            }
            p.move(Direction.DOWN, 2);
            p.move(d.rotateYClockwise());
            if(!world.isAir(p)) {
                isValid = false;
            }
            for(int i = 0; i < 2; i++) {
                p.move(Direction.UP);
                if(!world.isAir(p)) {
                    isValid = false;
                }
            }
            p.move(Direction.DOWN, 2);
            p.move(d);
            p.move(d.rotateYClockwise());
            BlockState state = world.getBlockState(p);
            if(state.getBlock() != ModBlocks.ENDER_ALTAR_FRAME) {
                isValid = false;
            } else if(framePositions != null) {
                framePositions.add(p.toImmutable());
            }
        }
        return isValid;
    }

    private void beginTeleportation() {
        AltarLookupComponent lookup = ModComponents.ALTAR_LOOKUP.get(world);
        List<BlockPos> possibleDestinations = lookup.getAltarsExcluding(altarId, pos);
        if(possibleDestinations.size() > 0) {
            BlockPos destination = possibleDestinations.get(world.random.nextInt(possibleDestinations.size()));
            if(isValidStructure(world, destination, null)) {
                Box teleportationBox = getTeleportationBox();
                for (Entity entity : world.getEntities(null, teleportationBox)) {
                    System.out.println(entity.toString());
                    Vec3d offset = entity.getPos().subtract(pos.getX(), pos.getY(), pos.getZ());
                    Vec3d target = offset.add(destination.getX(), destination.getY(), destination.getZ());
                    entity.requestTeleport(target.getX(), target.getY(), target.getZ());
                    if(entity instanceof ServerPlayerEntity) {
                        CodeTriggerCriterion.INSTANCE.trigger((ServerPlayerEntity)entity, "ender_altar");
                    }
                }
            }
        }
    }

    private boolean doCompletelyOverlap(Box a, Box b) {
        Box c = a.union(b);
        return a.equals(c) || b.equals(c);
    }

    private Box getTeleportationBox() {
        return new Box(pos.add(-1, 1, -1), pos.add(1, 3, 1));
    }

    public String getAltarId() {
        return altarId;
    }

    public void setAltarId(String altarId) {
        this.altarId = altarId;
        this.markDirty();
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.altarId = tag.getString("AltarID");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putString("AltarID", this.altarId);
        return tag;
    }

    @Override
    public void markRemoved() {
        if(!world.isClient) {
            AltarLookupComponent lookup = ModComponents.ALTAR_LOOKUP.get(world);
            lookup.removeAltar(pos, altarId);
        }
        super.markRemoved();
    }
}
