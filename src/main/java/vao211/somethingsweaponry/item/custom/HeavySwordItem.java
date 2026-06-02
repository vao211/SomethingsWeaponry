package vao211.somethingsweaponry.item.custom;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class HeavySwordItem extends SwordItem {
    //First attack (cd: 4s) deal 1.5xdmg and disable shield
    //Login in HeavySwordMixin
    public HeavySwordItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }
}

