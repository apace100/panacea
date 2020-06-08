package io.github.apace100.panacea.component;

import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface AltarLookupComponent extends Component {

    List<BlockPos> getAltars(String altarId);
    List<BlockPos> getAltarsExcluding(String altarId, BlockPos posExclude);
    void addAltar(BlockPos pos, String altarId);
    void removeAltar(BlockPos pos, String altarId);
}
