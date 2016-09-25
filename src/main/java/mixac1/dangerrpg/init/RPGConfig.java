package mixac1.dangerrpg.init;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mixac1.dangerrpg.DangerRPG;
import mixac1.dangerrpg.api.entity.EntityAttribute;
import mixac1.dangerrpg.api.entity.LvlEAProvider;
import mixac1.dangerrpg.api.item.ItemAttribute;
import mixac1.dangerrpg.capability.data.RPGEntityData;
import mixac1.dangerrpg.capability.data.RPGEntityData.EntityAttrParams;
import mixac1.dangerrpg.capability.data.RPGItemData;
import mixac1.dangerrpg.capability.data.RPGItemData.ItemAttrParams;
import mixac1.dangerrpg.capability.ea.EntityAttributes;
import mixac1.dangerrpg.client.gui.GuiMode;
import mixac1.dangerrpg.util.IMultiplier.IMulConfigurable;
import mixac1.dangerrpg.util.IMultiplier.MulType;
import mixac1.dangerrpg.util.RPGHelper;
import mixac1.dangerrpg.util.Utils;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class RPGConfig
{
    public static File dir;

    private static ArrayList<RPGConfigCommon> configs = new ArrayList<RPGConfigCommon>();

    public static void load(FMLPreInitializationEvent e)
    {
        dir = new File((File) FMLInjectionData.data()[6], "config/".concat(DangerRPG.MODID));
        if (dir.exists()) {
            if (!dir.isDirectory()) {
                dir.delete();
            }
        }
        else {
            dir.mkdir();
        }

        configs.add(new MainConfig("MainConfig"));
        configs.add(new ItemConfig("ItemConfig"));
        configs.add(new EntityConfig("EntityConfig"));
    }

    @SideOnly(Side.CLIENT)
    public static void loadClient(FMLPreInitializationEvent e)
    {
        configs.add(new ClientConfig("ClientConfig"));
    }

    public static void postLoadPre(FMLPostInitializationEvent e)
    {
        for (RPGConfigCommon config : configs) {
            config.postLoadPre();
        }
    }

    public static void postLoadPost(FMLPostInitializationEvent e)
    {
        for (RPGConfigCommon config : configs) {
            config.postLoadPost();
        }
    }

    public static class MainConfig extends RPGConfigCommon
    {
        public static boolean   mainEnableInfoLog   = true;

        public MainConfig(String fileName)
        {
            super(fileName);
        }

        @Override
        protected void init()
        {
            category.setComment("GENERAL INFO:\n"
                    + "\n"
                    + "How do config multipliers ('.mul')\n"
                    + "You can use two types of multiplier:\n"
                    + "ADD 'value' - adding value to the input parameter.\n"
                    + "MUL 'value' - multiplication the input parameter by the value.\n"
                    + "HARD - not for using. There is a hard expression, but you can change it using ADD or MUL\n"
                    + "\n");

            super.init();
        }

        @Override
        public void load()
        {
            mainEnableInfoLog = config.getBoolean("mainEnableInfoLog", category.getName(), mainEnableInfoLog,
                    "Enable writing info message to log (true/false)");

            save();
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ClientConfig extends RPGConfigCommon
    {
        public static boolean   guiIsEnableHUD          = true;
        public static int       guiPlayerHUDOffsetX     = 10;
        public static int       guiPlayerHUDOffsetY     = 10;
        public static boolean   guiPlayerHUDIsInvert    = false;
        public static int       guiEnemyHUDOffsetX      = 10;
        public static int       guiEnemyHUDOffsetY      = 10;
        public static boolean   guiEnemyHUDIsInvert     = true;
        public static int       guiChargeOffsetX        = 0;
        public static int       guiChargeOffsetY        = 45;
        public static boolean   guiChargeIsCentered     = true;
        public static boolean   guiTwiceHealthManaBar   = true;
        public static int       guiDafaultHUDMode       = 1;
        public static int       guiDamageForTestArmor   = 25;

        public static boolean   neiShowShapedRecipe     = false;

        public ClientConfig(String fileName)
        {
            super(fileName);
        }

        @Override
        public void load()
        {
            guiIsEnableHUD = config.getBoolean("guiIsEnableHUD", category.getName(), guiIsEnableHUD,
                    "Enable RPG HUD (true/false)");

            guiPlayerHUDOffsetX = config.getInt("guiPlayerHUDOffsetX", category.getName(), guiPlayerHUDOffsetX, 0, Integer.MAX_VALUE,
                    "Change X offset of player's HUD");

            guiPlayerHUDOffsetY = config.getInt("guiPlayerHUDOffsetY", category.getName(), guiPlayerHUDOffsetY, 0, Integer.MAX_VALUE,
                    "Change Y offset of player's HUD");

            guiPlayerHUDIsInvert = config.getBoolean("guiPlayerHUDIsInvert", category.getName(), guiPlayerHUDIsInvert,
                    "Change side of player's HUD (true/false)");

            guiEnemyHUDOffsetX = config.getInt("guiEnemyHUDOffsetX", category.getName(), guiEnemyHUDOffsetX, 0, Integer.MAX_VALUE,
                    "Change X offset of enemy's HUD");

            guiEnemyHUDOffsetY = config.getInt("guiEnemyHUDOffsetY", category.getName(), guiEnemyHUDOffsetY, 0, Integer.MAX_VALUE,
                    "Change Y offset of enemy's HUD");

            guiEnemyHUDIsInvert = config.getBoolean("guiEnemyHUDIsInvert", category.getName(), guiEnemyHUDIsInvert,
                    "Change side of enemy's HUD (true/false)");

            guiChargeOffsetX = config.getInt("guiChargeOffsetX", category.getName(), guiChargeOffsetX, 0, Integer.MAX_VALUE,
                    "Change X offset of charge bar");

            guiChargeOffsetY = config.getInt("guiChargeOffsetY", category.getName(), guiChargeOffsetY, 0, Integer.MAX_VALUE,
                    "Change Y offset of charge bar");

            guiChargeIsCentered = config.getBoolean("guiChargeIsCentered", category.getName(), guiChargeIsCentered,
                    "Charge bar need centering (true/false)");

            guiTwiceHealthManaBar = config.getBoolean("guiTwiceHealthManaBar", category.getName(), guiTwiceHealthManaBar,
                    "Twice health-mana bar (true/false)");

            guiDamageForTestArmor = config.getInt("guiDamageForTestArmor", category.getName(), guiDamageForTestArmor, 0, Integer.MAX_VALUE,
                    "Default damage value for calculate resistance in armor bar.");

            guiDafaultHUDMode = config.getInt("guiDafaultHUDMode", category.getName(), guiDafaultHUDMode, 0, 3,
                    "Set default HUD mode:\n[0] - normal\n[1] - normal digital\n[2] - simple\n[3] - simple digital\n");
            GuiMode.set(guiDafaultHUDMode);

            neiShowShapedRecipe = config.getBoolean("neiShowShapedRecipe", category.getName(), neiShowShapedRecipe,
                    "Is show default recipes in RPG workbench (need NEI) (true/false)");

            save();
        }
    }

    public static class ItemConfig extends RPGConfigCommon
    {
        public static boolean   isAllItemsRPGable   = false;
        public static boolean   canUpInTable        = true;
        public static int       maxLevel            = 100;
        public static int       startMaxExp         = 100;
        public static float     expMul              = 1.15f;

        public static HashSet<String> activeRPGItems = new HashSet<String>();

        public ItemConfig(String fileName)
        {
            super(fileName);
        }

        @Override
        protected void init()
        {
            category.setComment("FAQ:\n"
                    + "Q: How do activate RPG item?\n"
                    + "A: Take name of item frome the 'itemList' and put it to the 'activeRPGItems' list.\n"
                    + "Or you can enable flag 'isAllItemsRPGable' for active all items.\n"
                    + "\n"
                    + "Q: How do congigure any item?\n"
                    + "A: Take name of item frome the 'itemList' and put it to the 'needCustomSetting' list.\n"
                    + "After this, run the game, exit from game and reopen this config.\n"
                    + "You be able find generated element for configure that item.");
            super.init();
        }

        @Override
        public void load()
        {
            isAllItemsRPGable = config.getBoolean("isAllItemsRPGable", category.getName(), isAllItemsRPGable,
                    "All weapons, tools , armors are RPGable (dangerous) (true/false)");

            canUpInTable = config.getBoolean("canUpInTable", category.getName(), canUpInTable,
                    "Items can be upgrade in LevelUp Table without creative mode (true/false) \nLevelUp Table is invisible now");

            maxLevel = config.getInt("maxLevel", category.getName(), maxLevel, 1, Integer.MAX_VALUE,
                    "Set max level of RPG items");

            startMaxExp = config.getInt("startMaxExp", category.getName(), startMaxExp, 0, Integer.MAX_VALUE,
                    "Set start needed expirience for RPG items");

            expMul = config.getFloat("expMul", category.getName(), expMul, 0f, Float.MAX_VALUE,
                    "Set expirience multiplier for RPG items");

            save();
        }

        @Override
        public void postLoadPre()
        {
            ArrayList<String> names = RPGHelper.getItemNames(RPGCapability.rpgItemRegistr.keySet(), true);
            Property prop = getPropertyStrings("activeRPGItems", names.toArray(new String[names.size()]),
                    "Set active RPG items (activated if 'isAllItemsRPGable' is false) (true/false)", false);
            if (!isAllItemsRPGable) {
                activeRPGItems = new HashSet<String>(Arrays.asList(prop.getStringList()));
            }

            save();
        }

        @Override
        public void postLoadPost()
        {
            HashMap<Item, RPGItemData> map = RPGCapability.rpgItemRegistr.getActiveElements();

            customConfig(map);

            ArrayList<String> names = RPGHelper.getItemNames(map.keySet(), true);
            getPropertyStrings("activeRPGItems", names.toArray(new String[names.size()]),
                    "Set active RPG items (activated if 'isAllItemsRPGable' is false) (true/false)", true);

            names = RPGHelper.getItemNames(RPGCapability.rpgItemRegistr.keySet(), true);
            getPropertyStrings("itemList", names.toArray(new String[names.size()]),
                    "List of all items, which can be RPGable", true);

            save();
        }

        protected void customConfig(HashMap<Item, RPGItemData> map)
        {
            String str = "customSetting";

            Property prop = getPropertyStrings("needCustomSetting", new String[] {Items.diamond_sword.delegate.name()},
                    "Set items, which needs customization", true);
            HashSet<String> needCustomSetting = new HashSet<String>(Arrays.asList(prop.getStringList()));

            if (!needCustomSetting.isEmpty()) {
                for (Entry<Item, RPGItemData> item : map.entrySet()) {
                    if (needCustomSetting.contains(item.getKey().delegate.name())) {
                        ConfigCategory cat = config.getCategory(Utils.toString(str, ".", item.getKey().delegate.name()));
                        if (!item.getValue().isSupported) {
                            cat.setComment("Warning: it isn't support from mod");
                        }
                        for (Entry<ItemAttribute, ItemAttrParams> ia : item.getValue().map.entrySet()) {
                            ia.getValue().value = getRPGAttributeValue(cat.getName(), ia);
                            if (ia.getValue().mul != null) {
                                ia.getValue().mul = getRPGMultiplier(cat.getName(), ia);
                            }
                        }
                    }
                }
            }
        }

        protected float getRPGAttributeValue(String category, Entry<ItemAttribute, ItemAttrParams> attr)
        {
            Property prop = config.get(category, attr.getKey().name, attr.getValue().value);
            prop.comment = " [default: " + attr.getValue().value + "]";
            float value = (float) prop.getDouble();
            if (attr.getKey().isValid(value)) {
                return value;
            }
            else {
                prop.set(attr.getValue().value);
                return attr.getValue().value;
            }
        }

        protected IMulConfigurable getRPGMultiplier(String category, Entry<ItemAttribute, ItemAttrParams> attr)
        {
            String defStr = attr.getValue().mul.toString();
            Property prop = config.get(category, attr.getKey().name.concat(".mul"), defStr);
            prop.comment = " [default: " + defStr + "]";
            String str = prop.getString();

            if (!defStr.equals(str)) {
                IMulConfigurable mul = MulType.getMul(str);
                if (mul != null) {
                    return mul;
                }
            }

            prop.set(defStr);
            return attr.getValue().mul;
        }
    }

    public static class EntityConfig extends RPGConfigCommon
    {
        public static boolean isAllEntitiesRPGable      = false;
        public static int     entityLvlUpFrequency      = 50;
        public static int     playerLoseLvlCount        = 3;
        public static int     playerStartManaValue      = 10;
        public static int     playerStartManaRegenValue = 1;

        public static HashSet<String> activeRPGEntities = new HashSet<String>();

        public EntityConfig(String fileName)
        {
            super(fileName);
        }

        @Override
        protected void init()
        {
            category.setComment("FAQ:\n"
                    + "Q: How do activate RPG entity?\n"
                    + "A: Take name of entity frome the 'entityList' and put it to the 'activeRPGEntities' list.\n"
                    + "Or you can enable flag 'isAllEntitiesRPGable' for active all entities.\n"
                    + "\n"
                    + "Q: How do congigure any entity?\n"
                    + "A: Take name of entity frome the 'entityList' and put it to the 'needCustomSetting' list.\n"
                    + "After this, run the game, exit from game and reopen this config.\n"
                    + "You be able find generated element for configure that entity.");
            super.init();
        }

        @Override
        public void load()
        {
            isAllEntitiesRPGable = config.getBoolean("isAllEntitiesRPGable", category.getName(), isAllEntitiesRPGable,
                    "All entities are RPGable (true/false)");

            entityLvlUpFrequency = config.getInt("entityLvlUpFrequency", category.getName(), entityLvlUpFrequency, 1, Integer.MAX_VALUE,
                    "Set frequency of RPG entity level up");

            playerLoseLvlCount = config.getInt("playerLoseLvlCount", category.getName(), playerLoseLvlCount, 0, Integer.MAX_VALUE,
                    "Set number of lost points of level when player die");

            playerStartManaValue = config.getInt("playerStartManaValue", category.getName(), playerStartManaValue, 0, Integer.MAX_VALUE,
                    "Set start mana value");

            playerStartManaRegenValue = config.getInt("playerStartManaRegenValue", category.getName(), playerStartManaRegenValue, 0, Integer.MAX_VALUE,
                    "Set start mana regeneration value");

            save();
        }

        @Override
        public void postLoadPre()
        {
            ArrayList<String> names = RPGHelper.getEntityNames(RPGCapability.rpgEntityRegistr.keySet(), true);
            Property prop = getPropertyStrings("activeRPGEntities", names.toArray(new String[names.size()]),
                    "Set active RPG entities (activated if 'isAllEntitiesRPGable' is false) (true/false)", false);
            if (!isAllEntitiesRPGable) {
                activeRPGEntities = new HashSet<String>(Arrays.asList(prop.getStringList()));
            }
            save();
        }

        @Override
        public void postLoadPost()
        {
            playerConfig();

            HashMap<Class<? extends EntityLivingBase>, RPGEntityData> map = RPGCapability.rpgEntityRegistr.getActiveElements();

            customConfig(map);

            ArrayList<String> names = RPGHelper.getEntityNames(map.keySet(), true);
            getPropertyStrings("activeRPGEntities", names.toArray(new String[names.size()]),
                    "Set active RPG entities (activated if 'isAllEntitiesRPGable' is false) (true/false)", true);

            names = RPGHelper.getEntityNames(RPGCapability.rpgEntityRegistr.keySet(), true);
            getPropertyStrings("entityList", names.toArray(new String[names.size()]),
                    "List of all entities, which can be RPGable", true);

            save();
        }

        public void playerConfig()
        {
            String str = "customPlayerSetting";

            for (LvlEAProvider lvlProv : RPGCapability.rpgEntityRegistr.get(EntityPlayer.class).lvlProviders) {
                String catStr = Utils.toString(str, ".", lvlProv.attr.name);
                lvlProv.maxLvl = config.getInt("maxLvl", catStr, lvlProv.maxLvl, 0, Integer.MAX_VALUE, "");
                lvlProv.startExpCost = config.getInt("startExpCost", catStr, lvlProv.startExpCost, 0, Integer.MAX_VALUE, "");
                if (lvlProv.mulValue instanceof IMulConfigurable) {
                    lvlProv.mulValue = getRPGMultiplier(catStr, "value", lvlProv.attr, (IMulConfigurable) lvlProv.mulValue);
                }
                lvlProv.mulExpCost = getRPGMultiplier(catStr, "expCost", lvlProv.attr, lvlProv.mulExpCost);
            }
        }

        protected void customConfig(HashMap<Class<? extends EntityLivingBase>, RPGEntityData> map)
        {
            String str = "customSetting";

            Property prop = getPropertyStrings("needCustomSetting", new String[] {(String) EntityList.classToStringMapping.get(EntityZombie.class)},
                    "Set entities, which needs customization", true);
            HashSet<String> needCustomSetting = new HashSet<String>(Arrays.asList(prop.getStringList()));

            if (!needCustomSetting.isEmpty()) {
                String entityName;
                for (Entry<Class<? extends EntityLivingBase>, RPGEntityData> entity : map.entrySet()) {
                    if (!EntityPlayer.class.isAssignableFrom(entity.getKey())
                        && needCustomSetting.contains(entityName = (String) EntityList.classToStringMapping.get(entity.getKey()))) {
                        ConfigCategory cat = config.getCategory(Utils.toString(str, ".", entityName));
                        if (!entity.getValue().isSupported) {
                            cat.setComment("Warning: it isn't support from mod");
                        }
                        for (Entry<EntityAttribute, EntityAttrParams> ea : entity.getValue().attributes.entrySet()) {
                            if (ea.getKey() != EntityAttributes.LVL) {
                                ea.getValue().mulValue = getRPGMultiplier(cat.getName(), ea.getKey().name, ea.getKey(), ea.getValue().mulValue);
                            }
                        }
                    }
                }
            }
        }

        protected IMulConfigurable getRPGMultiplier(String category, String name, EntityAttribute attr, IMulConfigurable mul)
        {
            String defStr = mul.toString();
            Property prop = config.get(category, name.concat(".mul"), defStr);
            prop.comment = " [default: " + defStr + "]";
            String str = prop.getString();

            if (!defStr.equals(str)) {
                IMulConfigurable mul1 = MulType.getMul(str);
                if (mul1 != null) {
                    return mul1;
                }
            }

            prop.set(defStr);
            return mul;
        }
    }

    public static abstract class RPGConfigCommon
    {
        protected Configuration config;
        protected ConfigCategory category;

        protected RPGConfigCommon(String fileName)
        {
            config = new Configuration(new File(dir, fileName.concat(".cfg")), DangerRPG.VERSION, true);

            category = config.getCategory(fileName);

            init();
        }

        protected void init()
        {
            load();
            save();
        }

        protected void load() {}

        public void postLoadPre() {}

        public void postLoadPost() {}

        public void save()
        {
            if (config.hasChanged()) {
                config.save();
            }
        }

        protected Property getPropertyStrings(String categoryName, String[] defValue, String comment, boolean needClear)
        {
            ConfigCategory cat = config.getCategory(categoryName);
            if (needClear) {
                cat.clear();
            }

            Property prop = config.get(cat.getQualifiedName(), "list", defValue);
            prop.comment = comment != null ? comment : "";
            return prop;
        }
    }
}
