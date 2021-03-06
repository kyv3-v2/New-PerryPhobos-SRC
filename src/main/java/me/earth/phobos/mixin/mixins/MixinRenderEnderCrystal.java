



package me.earth.phobos.mixin.mixins;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import me.earth.phobos.features.modules.render.*;
import net.minecraft.client.renderer.*;
import me.earth.phobos.event.events.*;
import org.lwjgl.opengl.*;
import me.earth.phobos.features.modules.client.*;
import java.awt.*;
import me.earth.phobos.util.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ RenderEnderCrystal.class })
public class MixinRenderEnderCrystal
{
    private static final ResourceLocation glint;
    @Shadow
    @Final
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    
    @Redirect(method = { "doRender" },  at = @At(value = "INVOKE",  target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(final ModelBase model,  final Entity entity,  final float limbSwing,  final float limbSwingAmount,  final float ageInTicks,  final float netHeadYaw,  final float headPitch,  final float scale) {
        if (CrystalModifier.INSTANCE.isEnabled()) {
            if ((boolean)CrystalModifier.INSTANCE.animateScale.getValue() && CrystalModifier.INSTANCE.scaleMap.containsKey(entity)) {
                GlStateManager.scale((float)CrystalModifier.INSTANCE.scaleMap.get(entity),  (float)CrystalModifier.INSTANCE.scaleMap.get(entity),  (float)CrystalModifier.INSTANCE.scaleMap.get(entity));
            }
            else {
                GlStateManager.scale((float)CrystalModifier.INSTANCE.scale.getValue(),  (float)CrystalModifier.INSTANCE.scale.getValue(),  (float)CrystalModifier.INSTANCE.scale.getValue());
            }
        }
        if (CrystalModifier.INSTANCE.isEnabled() && (boolean)CrystalModifier.INSTANCE.wireframe.getValue()) {
            final RenderEntityModelEvent event = new RenderEntityModelEvent(0,  model,  entity,  limbSwing,  limbSwingAmount,  ageInTicks,  netHeadYaw,  headPitch,  scale);
            CrystalModifier.INSTANCE.onRenderModel(event);
        }
        if (CrystalModifier.INSTANCE.isEnabled() && (boolean)CrystalModifier.INSTANCE.chams.getValue()) {
            GL11.glPushAttrib(1048575);
            GL11.glDisable(3008);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770,  771);
            GL11.glLineWidth(1.5f);
            GL11.glEnable(2960);
            if (CrystalModifier.INSTANCE.rainbow.getValue()) {
                final Color rainbowColor1 = CrystalModifier.INSTANCE.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : new Color(RenderUtil.getRainbow((int)CrystalModifier.INSTANCE.speed.getValue() * 100,  0,  (int)CrystalModifier.INSTANCE.saturation.getValue() / 100.0f,  (int)CrystalModifier.INSTANCE.brightness.getValue() / 100.0f));
                final Color rainbowColor2 = EntityUtil.getColor(entity,  rainbowColor1.getRed(),  rainbowColor1.getGreen(),  rainbowColor1.getBlue(),  (int)CrystalModifier.INSTANCE.alpha.getValue(),  true);
                if (CrystalModifier.INSTANCE.throughWalls.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f(rainbowColor2.getRed() / 255.0f,  rainbowColor2.getGreen() / 255.0f,  rainbowColor2.getBlue() / 255.0f,  (int)CrystalModifier.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity,  limbSwing,  limbSwingAmount,  ageInTicks,  netHeadYaw,  headPitch,  scale);
                if (CrystalModifier.INSTANCE.throughWalls.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
            }
            else if ((boolean)CrystalModifier.INSTANCE.xqz.getValue() && (boolean)CrystalModifier.INSTANCE.throughWalls.getValue()) {
                final Color hiddenColor = EntityUtil.getColor(entity,  (int)CrystalModifier.INSTANCE.hiddenRed.getValue(),  (int)CrystalModifier.INSTANCE.hiddenGreen.getValue(),  (int)CrystalModifier.INSTANCE.hiddenBlue.getValue(),  (int)CrystalModifier.INSTANCE.hiddenAlpha.getValue(),  true);
                final Color visibleColor = EntityUtil.getColor(entity,  (int)CrystalModifier.INSTANCE.red.getValue(),  (int)CrystalModifier.INSTANCE.green.getValue(),  (int)CrystalModifier.INSTANCE.blue.getValue(),  (int)CrystalModifier.INSTANCE.alpha.getValue(),  true);
                if (CrystalModifier.INSTANCE.throughWalls.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f(hiddenColor.getRed() / 255.0f,  hiddenColor.getGreen() / 255.0f,  hiddenColor.getBlue() / 255.0f,  (int)CrystalModifier.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity,  limbSwing,  limbSwingAmount,  ageInTicks,  netHeadYaw,  headPitch,  scale);
                if (CrystalModifier.INSTANCE.throughWalls.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
                GL11.glColor4f(visibleColor.getRed() / 255.0f,  visibleColor.getGreen() / 255.0f,  visibleColor.getBlue() / 255.0f,  (int)CrystalModifier.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity,  limbSwing,  limbSwingAmount,  ageInTicks,  netHeadYaw,  headPitch,  scale);
            }
            else {
                final Color visibleColor = CrystalModifier.INSTANCE.colorSync.getValue() ? Colors.INSTANCE.getCurrentColor() : EntityUtil.getColor(entity,  (int)CrystalModifier.INSTANCE.red.getValue(),  (int)CrystalModifier.INSTANCE.green.getValue(),  (int)CrystalModifier.INSTANCE.blue.getValue(),  (int)CrystalModifier.INSTANCE.alpha.getValue(),  true);
                if (CrystalModifier.INSTANCE.throughWalls.getValue()) {
                    GL11.glDisable(2929);
                    GL11.glDepthMask(false);
                }
                GL11.glEnable(10754);
                GL11.glColor4f(visibleColor.getRed() / 255.0f,  visibleColor.getGreen() / 255.0f,  visibleColor.getBlue() / 255.0f,  (int)CrystalModifier.INSTANCE.alpha.getValue() / 255.0f);
                model.render(entity,  limbSwing,  limbSwingAmount,  ageInTicks,  netHeadYaw,  headPitch,  scale);
                if (CrystalModifier.INSTANCE.throughWalls.getValue()) {
                    GL11.glEnable(2929);
                    GL11.glDepthMask(true);
                }
            }
            GL11.glEnable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
            GL11.glPopAttrib();
            if (CrystalModifier.INSTANCE.glint.getValue()) {
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GlStateManager.enableAlpha();
                GlStateManager.color(1.0f,  0.0f,  0.0f,  0.13f);
                model.render(entity,  limbSwing,  limbSwingAmount,  ageInTicks,  netHeadYaw,  headPitch,  scale);
                GlStateManager.disableAlpha();
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
            }
        }
        else {
            model.render(entity,  limbSwing,  limbSwingAmount,  ageInTicks,  netHeadYaw,  headPitch,  scale);
        }
        if (CrystalModifier.INSTANCE.isEnabled()) {
            if ((boolean)CrystalModifier.INSTANCE.animateScale.getValue() && CrystalModifier.INSTANCE.scaleMap.containsKey(entity)) {
                GlStateManager.scale(1.0f / CrystalModifier.INSTANCE.scaleMap.get(entity),  1.0f / CrystalModifier.INSTANCE.scaleMap.get(entity),  1.0f / CrystalModifier.INSTANCE.scaleMap.get(entity));
            }
            else {
                GlStateManager.scale(1.0f / (float)CrystalModifier.INSTANCE.scale.getValue(),  1.0f / (float)CrystalModifier.INSTANCE.scale.getValue(),  1.0f / (float)CrystalModifier.INSTANCE.scale.getValue());
            }
        }
    }
    
    static {
        glint = new ResourceLocation("textures/glint.png");
    }
}
