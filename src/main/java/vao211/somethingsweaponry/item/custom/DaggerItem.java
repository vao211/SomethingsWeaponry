package vao211.somethingsweaponry.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class DaggerItem extends SwordItem {
    //1.3x dmg on back hit
    //Logic in BackstabMixin
    public DaggerItem(ToolMaterial toolMaterial, Settings settings){
        super(toolMaterial, settings);
    }
}
