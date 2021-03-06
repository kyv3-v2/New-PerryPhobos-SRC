



package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import java.awt.*;
import me.earth.phobos.util.*;
import me.earth.phobos.features.modules.client.*;
import net.minecraft.util.math.*;

public class BlockHighlight extends Module
{
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    public Setting<Boolean> colorSync;
    public Setting<Boolean> rolling;
    public Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    public Setting<Boolean> outline;
    private final Setting<Float> lineWidth;
    public Setting<Boolean> customOutline;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    private final Setting<Integer> cAlpha;
    
    public BlockHighlight() {
        super("BlockHighlight",  "Highlights the block u look at.",  Module.Category.RENDER,  false,  false,  false);
        this.red = (Setting<Integer>)this.register(new Setting("Red", 0, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 255, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255));
        this.colorSync = (Setting<Boolean>)this.register(new Setting("Sync", false));
        this.rolling = (Setting<Boolean>)this.register(new Setting("Rolling", false,  v -> this.colorSync.getValue()));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", false));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 125, 0, 255,  v -> this.box.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 5.0f,  v -> this.outline.getValue()));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", false,  v -> this.outline.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
    }
    
    public void onRender3D(final Render3DEvent event) {
        final RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos blockpos = ray.getBlockPos();
            if (this.rolling.getValue()) {
                RenderUtil.drawProperGradientBlockOutline(blockpos,  new Color(HUD.getInstance().colorMap.get(0)),  new Color(HUD.getInstance().colorMap.get(this.renderer.scaledHeight / 4)),  new Color(HUD.getInstance().colorMap.get(this.renderer.scaledHeight / 2)),  1.0f);
            }
            else {
                RenderUtil.drawBoxESP(blockpos,  ((boolean)this.colorSync.getValue()) ? Colors.INSTANCE.getCurrentColor() : new Color(this.red.getValue(),  this.green.getValue(),  this.blue.getValue(),  this.alpha.getValue()),  this.customOutline.getValue(),  new Color(this.cRed.getValue(),  this.cGreen.getValue(),  this.cBlue.getValue(),  this.cAlpha.getValue()),  this.lineWidth.getValue(),  this.outline.getValue(),  this.box.getValue(),  this.boxAlpha.getValue(),  false);
            }
        }
    }
}
