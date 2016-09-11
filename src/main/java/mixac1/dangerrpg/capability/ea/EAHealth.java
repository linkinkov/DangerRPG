package mixac1.dangerrpg.capability.ea;

import java.util.UUID;

import mixac1.dangerrpg.api.entity.EntityAttribute.EAFloat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class EAHealth extends EAFloat
{
    private final UUID ID = UUID.fromString("fd6315bf-9f57-46cb-bb38-4aacb5d2967a");

    public EAHealth(String name)
    {
        super(name);
    }

    @Override
    public String getDisplayValue(EntityLivingBase entity)
    {
        return String.format("%.1f", entity.getMaxHealth());
    }

    @Override
    public void apply(EntityLivingBase entity, Float value)
    {
        if (!entity.worldObj.isRemote) {
            float tmp = entity.getHealth() / entity.getMaxHealth();

            IAttributeInstance attr= entity.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            AttributeModifier mod = attr.getModifier(ID);
            if (mod != null) {
                attr.removeModifier(mod);
            }
            AttributeModifier newMod = new AttributeModifier(ID, name, getValueRaw(entity), 0).setSaved(true);
            attr.applyModifier(newMod);

            entity.setHealth(entity.getMaxHealth() * tmp);
        }
    }
}
