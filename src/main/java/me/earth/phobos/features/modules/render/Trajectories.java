



package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import net.minecraft.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.init.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import org.lwjgl.util.glu.*;
import net.minecraft.item.*;
import java.util.*;
import com.google.common.base.*;

public class Trajectories extends Module
{
    private final Setting<Float> size;
    private final Setting<Float> innerSize;
    public Setting slices;
    public Setting red;
    public Setting green;
    public Setting blue;
    public Setting alpha;
    
    public Trajectories() {
        super("Trajectories",  "Draws trajectories.",  Module.Category.RENDER,  false,  false,  false);
        this.size = (Setting<Float>)this.register(new Setting("Size", 1.0f, (-5.0f), 5.0f));
        this.innerSize = (Setting<Float>)this.register(new Setting("Inner Size", 1.0f, (-5.0f), 5.0f));
        this.slices = this.register(new Setting("Slices", 3, 2, 100));
        this.red = this.register(new Setting("Red", 0, 0, 255));
        this.green = this.register(new Setting("Green", 255, 0, 255));
        this.blue = this.register(new Setting("Blue", 0, 0, 255));
        this.alpha = this.register(new Setting("Alpha", 255, 0, 255));
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (Trajectories.mc.world != null && Trajectories.mc.player != null) {
            Trajectories.mc.getRenderManager();
            final double renderPosX = Trajectories.mc.player.lastTickPosX + (Trajectories.mc.player.posX - Trajectories.mc.player.lastTickPosX) * event.getPartialTicks();
            final double renderPosY = Trajectories.mc.player.lastTickPosY + (Trajectories.mc.player.posY - Trajectories.mc.player.lastTickPosY) * event.getPartialTicks();
            final double renderPosZ = Trajectories.mc.player.lastTickPosZ + (Trajectories.mc.player.posZ - Trajectories.mc.player.lastTickPosZ) * event.getPartialTicks();
            Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND);
            if (Trajectories.mc.gameSettings.thirdPersonView == 0 && (Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBow || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemFishingRod || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEnderPearl || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemEgg || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSnowball || Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemExpBottle)) {
                GL11.glPushMatrix();
                final Item item = Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem();
                double posX = renderPosX - MathHelper.cos(Trajectories.mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
                double posY = renderPosY + Trajectories.mc.player.getEyeHeight() - 0.1000000014901161;
                double posZ = renderPosZ - MathHelper.sin(Trajectories.mc.player.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
                double motionX = -MathHelper.sin(Trajectories.mc.player.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(Trajectories.mc.player.rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
                double motionY = -MathHelper.sin(Trajectories.mc.player.rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
                double motionZ = MathHelper.cos(Trajectories.mc.player.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(Trajectories.mc.player.rotationPitch / 180.0f * 3.1415927f) * ((item instanceof ItemBow) ? 1.0 : 0.4);
                final int var6 = 72000 - Trajectories.mc.player.getItemInUseCount();
                float power = var6 / 20.0f;
                power = (power * power + power * 2.0f) / 3.0f;
                if (power > 1.0f) {
                    power = 1.0f;
                }
                final float distance = MathHelper.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
                motionX /= distance;
                motionY /= distance;
                motionZ /= distance;
                final float pow = (item instanceof ItemBow) ? (power * 2.0f) : ((item instanceof ItemFishingRod) ? 1.25f : ((Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.9f : 1.0f));
                motionX *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.75f : 1.5f));
                motionY *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.75f : 1.5f));
                motionZ *= pow * ((item instanceof ItemFishingRod) ? 0.75f : ((Trajectories.mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.EXPERIENCE_BOTTLE) ? 0.75f : 1.5f));
                this.enableGL3D(2.0f);
                GlStateManager.color(this.red.getValue() / 255.0f,  this.green.getValue() / 255.0f,  this.blue.getValue() / 255.0f,  this.alpha.getValue() / 255.0f);
                GL11.glEnable(2848);
                final float size = (float)((item instanceof ItemBow) ? 0.3 : 0.25);
                boolean hasLanded = false;
                Entity landingOnEntity = null;
                RayTraceResult landingPosition = null;
                while (!hasLanded && posY > 0.0) {
                    final Vec3d present = new Vec3d(posX,  posY,  posZ);
                    final Vec3d future = new Vec3d(posX + motionX,  posY + motionY,  posZ + motionZ);
                    final RayTraceResult possibleLandingStrip = Trajectories.mc.world.rayTraceBlocks(present,  future,  false,  true,  false);
                    if (possibleLandingStrip != null && possibleLandingStrip.typeOfHit != RayTraceResult.Type.MISS) {
                        landingPosition = possibleLandingStrip;
                        hasLanded = true;
                    }
                    final AxisAlignedBB arrowBox = new AxisAlignedBB(posX - size,  posY - size,  posZ - size,  posX + size,  posY + size,  posZ + size);
                    final List<Entity> entities = this.getEntitiesWithinAABB(arrowBox.offset(motionX,  motionY,  motionZ).expand(1.0,  1.0,  1.0));
                    for (final Entity entity : entities) {
                        if (entity.canBeCollidedWith() && entity != Trajectories.mc.player) {
                            final AxisAlignedBB var7 = entity.getEntityBoundingBox().expand(0.30000001192092896,  0.30000001192092896,  0.30000001192092896);
                            final RayTraceResult possibleEntityLanding = var7.calculateIntercept(present,  future);
                            if (possibleEntityLanding == null) {
                                continue;
                            }
                            hasLanded = true;
                            landingOnEntity = entity;
                            landingPosition = possibleEntityLanding;
                        }
                    }
                    if (landingOnEntity != null) {
                        GlStateManager.color(this.red.getValue() / 255.0f,  this.green.getValue() / 255.0f,  this.blue.getValue() / 255.0f,  this.alpha.getValue() / 255.0f);
                    }
                    posX += motionX;
                    posY += motionY;
                    posZ += motionZ;
                    motionX *= 0.9900000095367432;
                    motionY *= 0.9900000095367432;
                    motionZ *= 0.9900000095367432;
                    motionY -= ((item instanceof ItemBow) ? 0.05 : 0.03);
                    this.drawLine3D(posX - renderPosX,  posY - renderPosY,  posZ - renderPosZ);
                }
                if (landingPosition != null && landingPosition.typeOfHit == RayTraceResult.Type.BLOCK) {
                    GlStateManager.translate(posX - renderPosX,  posY - renderPosY,  posZ - renderPosZ);
                    final int side = landingPosition.sideHit.getIndex();
                    if (side == 2) {
                        GlStateManager.rotate(90.0f,  1.0f,  0.0f,  0.0f);
                    }
                    else if (side == 3) {
                        GlStateManager.rotate(90.0f,  1.0f,  0.0f,  0.0f);
                    }
                    else if (side == 4) {
                        GlStateManager.rotate(90.0f,  0.0f,  0.0f,  1.0f);
                    }
                    else if (side == 5) {
                        GlStateManager.rotate(90.0f,  0.0f,  0.0f,  1.0f);
                    }
                    final Cylinder c = new Cylinder();
                    GlStateManager.rotate(-90.0f,  1.0f,  0.0f,  0.0f);
                    c.setDrawStyle(100011);
                    if (landingOnEntity != null) {
                        GlStateManager.color(this.red.getValue() / 255.0f,  this.green.getValue() / 255.0f,  this.blue.getValue() / 255.0f,  this.alpha.getValue() / 255.0f);
                        GL11.glLineWidth(2.5f);
                        c.draw(0.5f,  0.5f,  0.0f,  (int)this.slices.getValue(),  1);
                        GL11.glLineWidth(0.1f);
                        GlStateManager.color(this.red.getValue() / 255.0f,  this.green.getValue() / 255.0f,  this.blue.getValue() / 255.0f,  this.alpha.getValue() / 255.0f);
                    }
                    c.draw((float)this.size.getValue(),  (float)this.innerSize.getValue(),  0.0f,  (int)this.slices.getValue(),  1);
                }
                this.disableGL3D();
                GL11.glPopMatrix();
            }
        }
    }
    
    public void enableGL3D(final float lineWidth) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770,  771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        Trajectories.mc.entityRenderer.disableLightmap();
        GL11.glEnable(2848);
        GL11.glHint(3154,  4354);
        GL11.glHint(3155,  4354);
        GL11.glLineWidth(lineWidth);
    }
    
    public void disableGL3D() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154,  4352);
        GL11.glHint(3155,  4352);
    }
    
    public void drawLine3D(final double var1,  final double var2,  final double var3) {
        GL11.glVertex3d(var1,  var2,  var3);
    }
    
    private List<Entity> getEntitiesWithinAABB(final AxisAlignedBB bb) {
        final ArrayList<Entity> list = new ArrayList<Entity>();
        final int chunkMinX = MathHelper.floor((bb.minX - 2.0) / 16.0);
        final int chunkMaxX = MathHelper.floor((bb.maxX + 2.0) / 16.0);
        final int chunkMinZ = MathHelper.floor((bb.minZ - 2.0) / 16.0);
        final int chunkMaxZ = MathHelper.floor((bb.maxZ + 2.0) / 16.0);
        for (int x = chunkMinX; x <= chunkMaxX; ++x) {
            for (int z = chunkMinZ; z <= chunkMaxZ; ++z) {
                if (Trajectories.mc.world.getChunkProvider().getLoadedChunk(x,  z) != null) {
                    Trajectories.mc.world.getChunk(x,  z).getEntitiesWithinAABBForEntity((Entity)Trajectories.mc.player,  bb,  (List)list,  (Predicate)null);
                }
            }
        }
        return list;
    }
}
