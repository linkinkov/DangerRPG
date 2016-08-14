package mixac1.dangerrpg.capability.ia;

import mixac1.dangerrpg.api.item.IAStatic;
import mixac1.dangerrpg.capability.ea.PlayerAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class IADamage extends IAStatic
{
    public IADamage(String name)
    {
        super(name);
    }
    
    @Override
    public float get(ItemStack stack, EntityPlayer player)
    {
        return get(stack) + PlayerAttributes.STRENGTH.getValue(player) * ItemAttributes.STR_MUL.get(stack);
    }
}