package mixac1.dangerrpg.item;

public class RPGItemComponent
{
    public static final RPGItemComponent NULL      = new RPGItemComponent();

    public static final RPGToolComponent TRAINING  = new RPGToolComponent();

    public static final RPGToolComponent SWORD     = new RPGToolComponent();
    public static final RPGToolComponent NAGINATA  = new RPGToolComponent();
    public static final RPGToolComponent KATANA    = new RPGToolComponent();
    public static final RPGToolComponent SCYTHE    = new RPGToolComponent();
    public static final RPGToolComponent HAMMER    = new RPGToolComponent();
    public static final RPGToolComponent TOMAHAWK  = new RPGToolComponent();
    public static final RPGToolComponent KNIFE     = new RPGToolComponent();

    public static final RPGToolComponent AXE       = new RPGToolComponent();
    public static final RPGToolComponent PICKAXE   = new RPGToolComponent();
    public static final RPGToolComponent SHOVEL    = new RPGToolComponent();
    public static final RPGToolComponent HOE       = new RPGToolComponent();
    public static final RPGToolComponent MULTITOOL = new RPGToolComponent();

    public static final RPGBowComponent  BOW        = new RPGBowComponent();
    public static final RPGBowComponent  SHADOW_BOW = new RPGBowComponent();
    public static final RPGBowComponent  SNIPER_BOW = new RPGBowComponent();

    static
    {
     /* TOOLS             mDmg    mSpeed  magic   strMul  agiMul  intMul   knBack  reach */

        TRAINING.init    (0.0F,   10.0F,  0.0F,   0.5F,   1.0F,   0.25F,   10.0F,  2.5F);

        SWORD.init       (4.0F,   10.0F,  0.0F,   0.5F,   1.0F,   0.25F,   1.0F,   0.0F);
        NAGINATA.init    (4.0F,   8.0F,   0.0F,   0.5F,   0.8F,   0.25F,   1.1F,   1.5F);
        KATANA.init      (3.5F,   11.0F,  0.0F,   0.5F,   1.1F,   0.25F,   0.5F,   0.0F);
        SCYTHE.init      (5.0F,   7.0F,   0.0F,   0.65F,  0.7F,   0.25F,   1.2F,   1.0F);
        HAMMER.init      (6.0F,   3.0F,   0.0F,   0.8F,   0.3F,   0.25F,   1.5F,   0.0F);
        TOMAHAWK.init    (3.5F,   10.5F,  0.0F,   0.4F,   1.1F,   0.25F,   0.4F,   0.0F);
        KNIFE.init       (1.0F,   12.5F,  0.0F,   0.25F,  1.25F,  0.25F,   0.1F,   0.0F);

        AXE.init         (3.0F,   9.0F,   0.0F,   0.55F,  0.8F,   0.25F,   1.0F,   0.0F);
        PICKAXE.init     (2.0F,   10.0F,  0.0F,   0.3F,   0.8F,   0.25F,   1.0F,   0.0F);
        SHOVEL.init      (1.0F,   10.0F,  0.0F,   0.3F,   0.8F,   0.25F,   1.0F,   0.0F);
        HOE.init         (1.0F,   10.0F,  0.0F,   0.4F,   0.8F,   0.25F,   1.0F,   0.0F);
        MULTITOOL.init   (3.0F,   10.0F,  0.0F,   0.55F,  0.8F,   0.25F,   1.0F,   0.0F);

     /* BOWS              mDmg    mSpeed  magic   strMul  agiMul  intMul   knBack  reach   rDmg    rSpeed  rPow    durab   ench */

        BOW.init         (1.0F,   10.0F,  0.0F,   0.16F,  1.0F,   0.25F,   1.0F,   0.0F,   2.0F,   20.0F,  3.0F,   -0F,    3F);
        SHADOW_BOW.init  (4.0F,   10.0F,  0.0F,   0.16F,  1.0F,   0.25F,   1.0F,   0.0F,   2.5F,   16.0F,  3.0F,   500F,   5F);
        SNIPER_BOW.init  (1.0F,   10.0F,  0.0F,   0.16F,  1.0F,   0.25F,   1.0F,   0.0F,   4.0F,   40.0F,  3.5F,   1000F,  10F);
    }

    public static class RPGToolComponent extends RPGItemComponent
    {
        public float meleeDamage;
        public float meleeSpeed;
        public float magic;
        public float strMul;
        public float agiMul;
        public float intMul;
        public float knBack;
        public float reach;

        private void init(float meleeDamage, float meleeSpeed, float magic, float strMul,
                          float agiMul, float intMul, float knBack, float reach)
        {
            this.meleeDamage = meleeDamage;
            this.meleeSpeed  = meleeSpeed;
            this.magic       = magic;
            this.strMul      = strMul;
            this.agiMul      = agiMul;
            this.intMul      = intMul;
            this.knBack      = knBack;
            this.reach       = reach;
        }
    }

    public static class RPGGunComponent extends RPGToolComponent
    {
        public float shotDamage;
        public float shotSpeed;
        public float shotPower;

        private void init(float meleeDamage, float meleeSpeed, float magic, float strMul,
                          float agiMul, float intMul, float knBack, float reach,
                          float shotDamage, float shotSpeed, float shotPower)
        {
            super.init(meleeDamage, meleeSpeed, magic, strMul, agiMul, intMul, knBack, reach);
            this.shotDamage = shotDamage;
            this.shotSpeed  = shotSpeed;
            this.shotPower  = shotPower;
        }
    }

    public static class RPGBowComponent extends RPGGunComponent implements IWithoutToolMaterial
    {
        private RPGICWithoutTM itemComponent = new RPGICWithoutTM();

        private void init(float meleeDamage, float meleeSpeed, float magic, float strMul,
                          float agiMul, float intMul, float knBack, float reach,
                          float shotDamage, float shotSpeed, float shotPower,
                          float durab, float ench)
        {
            super.init(meleeDamage, meleeSpeed, magic, strMul, agiMul, intMul,
                       knBack, reach, shotDamage, shotSpeed, shotPower);
            itemComponent.init(durab, ench);
        }

        @Override
        public float getMaxDurability()
        {
            return itemComponent.durab;
        }

        @Override
        public float getEnchantability()
        {
            return itemComponent.ench;
        }
    }

    public static class RPGICWithoutTM extends RPGItemComponent implements IWithoutToolMaterial
    {
        public float durab;
        public float ench;

        private void init(float durab, float ench)
        {
            this.durab = durab;
            this.ench  = ench;
        }

        @Override
        public float getMaxDurability()
        {
            return durab;
        }

        @Override
        public float getEnchantability()
        {
            return ench;
        }
    }

    public static interface IWithoutToolMaterial
    {
        public float getMaxDurability();

        public float getEnchantability();
    }
}