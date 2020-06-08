package io.github.apace100.panacea.component;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldAltarLookupComponent implements AltarLookupComponent {

    private HashMap<String, List<BlockPos>> altarLookup = new HashMap<>();

    @Override
    public List<BlockPos> getAltars(String altarId) {
        if(!altarLookup.containsKey(altarId)) {
            return new ArrayList<BlockPos>();
        }
        return new ArrayList<>(altarLookup.get(altarId));
    }

    @Override
    public List<BlockPos> getAltarsExcluding(String altarId, BlockPos posExclude) {
        List<BlockPos> altars = getAltars(altarId);
        altars.remove(posExclude);
        return altars;
    }
    @Override
    public void addAltar(BlockPos pos, String altarId) {
        List<BlockPos> list;
        if(altarLookup.containsKey(altarId)) {
            list = altarLookup.get(altarId);
        } else {
            list = new ArrayList<BlockPos>(2);
            altarLookup.put(altarId, list);
        }
        list.add(pos);
    }

    @Override
    public void removeAltar(BlockPos pos, String altarId) {
        List<BlockPos> list = altarLookup.get(altarId);
        if(list != null) {
            list.remove(pos);
            if(list.size() == 0) {
                altarLookup.remove(altarId);
            }
        }
    }

    @Override
    public void fromTag(CompoundTag compoundTag) {
        ListTag lookupList = (ListTag)compoundTag.get("Lookup");
        ListTag idList = (ListTag)compoundTag.get("IDs");
        int count = lookupList.size();
        assert count == idList.size();
        altarLookup.clear();
        for(int i = 0; i < count; i++) {
            String id = idList.getString(i);
            ListTag positions = lookupList.getList(i);
            List<BlockPos> posList = new ArrayList<>(positions.size());
            for(int j = 0; j < positions.size(); j++) {
                CompoundTag posTag = positions.getCompound(j);
                BlockPos pos = new BlockPos(posTag.getInt("X"), posTag.getInt("Y"), posTag.getInt("Z"));
                posList.add(pos);
            }
            altarLookup.put(id, posList);
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag) {
        int count = altarLookup.size();
        ListTag lookupList = new ListTag();
        int index = 0;
        ListTag idList = new ListTag();
        for(Map.Entry<String, List<BlockPos>> id : altarLookup.entrySet()) {
            ListTag posList = new ListTag();
            for(BlockPos p : id.getValue()) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("X", p.getX());
                tag.putInt("Y", p.getY());
                tag.putInt("Z", p.getZ());
                posList.add(tag);
            }
            lookupList.add(posList);
            idList.add(StringTag.of(id.getKey()));
        }
        compoundTag.put("Lookup", lookupList);
        compoundTag.put("IDs", idList);
        return compoundTag;
    }
}
