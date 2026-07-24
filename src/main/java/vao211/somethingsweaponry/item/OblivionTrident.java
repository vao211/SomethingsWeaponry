package vao211.somethingsweaponry.item;

import net.minecraft.item.Item;
import net.minecraft.item.TridentItem;

public class OblivionTrident extends TridentItem {
    public OblivionTrident(Item.Settings settings){
        super(settings);
    }
    @Override
    public int getEnchantability() {
        return ModToolMaterials.OBLIVION.getEnchantability();
    }
}
