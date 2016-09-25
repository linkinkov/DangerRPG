package mixac1.dangerrpg.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import mixac1.dangerrpg.DangerRPG;
import mixac1.dangerrpg.api.entity.EntityAttribute.EAFloat;
import mixac1.dangerrpg.api.event.InitRPGEntityEvent;
import mixac1.dangerrpg.capability.RPGableEntity;
import mixac1.dangerrpg.capability.RPGableItem;
import mixac1.dangerrpg.capability.data.RPGEntityProperties;
import mixac1.dangerrpg.capability.data.RPGUUIDs;
import mixac1.dangerrpg.capability.ea.EntityAttributes;
import mixac1.dangerrpg.capability.ea.PlayerAttributes;
import mixac1.dangerrpg.capability.ia.ItemAttributes;
import mixac1.dangerrpg.init.RPGCapability;
import mixac1.dangerrpg.init.RPGConfig.EntityConfig;
import mixac1.dangerrpg.init.RPGNetwork;
import mixac1.dangerrpg.network.MsgSyncConfig;
import mixac1.dangerrpg.network.MsgSyncEntityData;
import mixac1.dangerrpg.util.IMultiplier.IMulConfigurable;
import mixac1.dangerrpg.util.RPGHelper;
import mixac1.dangerrpg.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EventHandlerEntity
{
    @SubscribeEvent
    public void onEntityConstructing(EntityConstructing e)
    {
        if (e.entity instanceof EntityLivingBase && RPGableEntity.isRPGable((EntityLivingBase) e.entity)) {
            RPGEntityProperties.register((EntityLivingBase) e.entity);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent e)
    {
        if (e.entity instanceof EntityLivingBase && RPGableEntity.isRPGable((EntityLivingBase) e.entity)) {
            if (e.entity.worldObj.isRemote) {
                RPGNetwork.net.sendToServer(new MsgSyncEntityData((EntityLivingBase) e.entity));
            }
            else {
                RPGEntityProperties.get((EntityLivingBase) e.entity).serverInit();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone e)
    {
        if (e.wasDeath) {
            RPGEntityProperties.get(e.original).rebuildOnDeath();
        }
        NBTTagCompound nbt = new NBTTagCompound();
        RPGEntityProperties.get(e.original).saveNBTData(nbt);
        RPGEntityProperties.get(e.entityPlayer).loadNBTData(nbt);
    }

    @SubscribeEvent
    public void onInitRPGEntity(InitRPGEntityEvent e)
    {
        ChunkCoordinates spawn = e.entity.worldObj.getSpawnPoint();
        double distance = Utils.getDiagonal(e.entity.posX - spawn.posX, e.entity.posZ - spawn.posZ);

        int lvl = (int) (distance / EntityConfig.entityLvlUpFrequency);
        if (EntityAttributes.LVL.hasIt(e.entity)) {
            EntityAttributes.LVL.setValue(lvl + 1, e.entity);
        }

        IMulConfigurable mul;
        if (EntityAttributes.HEALTH.hasIt(e.entity)) {
            float health = e.entity.getHealth();
            mul = RPGCapability.rpgEntityRegistr.get(e.entity).attributes.get(EntityAttributes.HEALTH).mulValue;
            EntityAttributes.HEALTH.setValue(RPGHelper.multyMul(health, lvl, mul), e.entity);
        }

        EAFloat attr = RPGCapability.rpgEntityRegistr.get(e.entity).rpgComponent.getEAMeleeDamage(e.entity);
        if (attr != null) {
            mul = RPGCapability.rpgEntityRegistr.get(e.entity).attributes.get(attr).mulValue;
            attr.addValue(RPGHelper.multyMul(attr.getValue(e.entity), lvl, mul), e.entity);
        }

        attr = RPGCapability.rpgEntityRegistr.get(e.entity).rpgComponent.getEARangeDamage(e.entity);
        if (attr != null) {
            mul = RPGCapability.rpgEntityRegistr.get(e.entity).attributes.get(attr).mulValue;
            attr.addValue(RPGHelper.multyMul(attr.getValue(e.entity), lvl, mul), e.entity);
        }
    }

    @SubscribeEvent
    public void onLivingJump(LivingJumpEvent e)
    {
        if (e.entityLiving instanceof EntityPlayer) {
            e.entityLiving.motionY += PlayerAttributes.JUMP_HEIGHT.getValue(e.entityLiving);
        }
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent e)
    {
        if (e.entityLiving instanceof EntityPlayer) {
            e.distance -= PlayerAttributes.FALL_RESIST.getValue(e.entityLiving) * 10;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e)
    {
        if (e.phase == TickEvent.Phase.START) {
            DangerRPG.proxy.fireTick(e.side);

            float tmp1, tmp2;
            if (!e.player.worldObj.isRemote) {
                if (e.player != null && (tmp1 = PlayerAttributes.SPEED_COUNTER.getValue(e.player)) > 0) {
                    PlayerAttributes.SPEED_COUNTER.setValue(tmp1 - 1, e.player);
                }

                if (DangerRPG.proxy.getTick(e.side) % 20 == 0) {
                    if ((tmp1 = PlayerAttributes.CURR_MANA.getValue(e.player)) < PlayerAttributes.MANA.getValue(e.player) &&
                        (tmp2 = PlayerAttributes.MANA_REGEN.getValue(e.player)) != 0) {
                        PlayerAttributes.CURR_MANA.setValue(tmp1 + tmp2, e.player);
                    }
                }

                if (DangerRPG.proxy.getTick(e.side) % 100 == 0) {
                    e.player.heal(PlayerAttributes.HEALTH_REGEN.getValue(e.player));
                }

                ItemStack stack = e.player.getCurrentEquippedItem();
                if (stack != null && RPGableItem.isRPGable(stack)) {
                    IAttributeInstance attr = e.player.getEntityAttribute(SharedMonsterAttributes.attackDamage);
                    AttributeModifier mod = attr.getModifier(RPGUUIDs.ADDITIONAL_STR_DAMAGE);
                    if (mod != null) {
                        attr.removeModifier(mod);
                    }
                    AttributeModifier newMod = new AttributeModifier(RPGUUIDs.ADDITIONAL_STR_DAMAGE, "Strenght damage",
                            PlayerAttributes.STRENGTH.getValue(e.player) * ItemAttributes.STR_MUL.get(stack), 0).setSaved(true);
                    attr.applyModifier(newMod);
                }
            }
            else {

            }

            if (e.player.getHealth() > (tmp1 = e.player.getMaxHealth())) {
                e.player.setHealth(tmp1);
            }

            if (PlayerAttributes.CURR_MANA.getValue(e.player) > (tmp1 = PlayerAttributes.MANA.getValue(e.player))) {
                PlayerAttributes.CURR_MANA.setValue(tmp1, e.player);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerLoggedInEvent e)
    {
        RPGNetwork.net.sendTo(new MsgSyncConfig(), (EntityPlayerMP) e.player);
    }
}
