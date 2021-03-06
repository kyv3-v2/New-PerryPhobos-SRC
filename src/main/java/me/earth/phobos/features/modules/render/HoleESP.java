



package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.*;
import net.minecraft.util.math.*;
import java.awt.*;
import me.earth.phobos.util.*;
import java.util.*;

public class HoleESP extends Module
{
    private static HoleESP INSTANCE;
    private final Setting<Integer> holes;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    public Setting<Boolean> ownHole;
    public Setting<Boolean> box;
    private final Setting<Integer> boxAlpha;
    public Setting<Boolean> gradientBox;
    public Setting<Boolean> pulseAlpha;
    private final Setting<Integer> minPulseAlpha;
    private final Setting<Integer> maxPulseAlpha;
    private final Setting<Integer> pulseSpeed;
    public Setting<Boolean> invertGradientBox;
    public Setting<Boolean> outline;
    private final Setting<Float> lineWidth;
    public Setting<Boolean> gradientOutline;
    public Setting<Boolean> invertGradientOutline;
    public Setting<Double> height;
    public Setting<Boolean> safeColor;
    private final Setting<Integer> safeRed;
    private final Setting<Integer> safeGreen;
    private final Setting<Integer> safeBlue;
    private final Setting<Integer> safeAlpha;
    public Setting<Boolean> customOutline;
    private final Setting<Integer> cRed;
    private final Setting<Integer> cGreen;
    private final Setting<Integer> cBlue;
    private final Setting<Integer> cAlpha;
    private final Setting<Integer> safecRed;
    private final Setting<Integer> safecGreen;
    private final Setting<Integer> safecBlue;
    private final Setting<Integer> safecAlpha;
    private boolean pulsing;
    private boolean shouldDecrease;
    private int pulseDelay;
    private int currentPulseAlpha;
    private int currentAlpha;
    
    public HoleESP() {
        super("HoleESP",  "Shows safe spots.",  Module.Category.RENDER,  false,  false,  false);
        this.holes = (Setting<Integer>)this.register(new Setting("Holes", 500, 1, 500));
        this.red = (Setting<Integer>)this.register(new Setting("Red", 255, 0, 255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", 0, 0, 255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", 0, 0, 255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", 255, 0, 255));
        this.ownHole = (Setting<Boolean>)this.register(new Setting("OwnHole", false));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", true));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", 100, 0, 255,  v -> this.box.getValue()));
        this.gradientBox = (Setting<Boolean>)this.register(new Setting("GradientBox", true,  v -> this.box.getValue()));
        this.pulseAlpha = (Setting<Boolean>)this.register(new Setting("PulseAlpha", false,  v -> this.gradientBox.getValue()));
        this.minPulseAlpha = (Setting<Integer>)this.register(new Setting("MinPulse", 10, 0, 255,  v -> this.pulseAlpha.getValue()));
        this.maxPulseAlpha = (Setting<Integer>)this.register(new Setting("MaxPulse", 40, 0, 255,  v -> this.pulseAlpha.getValue()));
        this.pulseSpeed = (Setting<Integer>)this.register(new Setting("PulseSpeed", 10, 1, 50,  v -> this.pulseAlpha.getValue()));
        this.invertGradientBox = (Setting<Boolean>)this.register(new Setting("InvertGradientBox", false,  v -> this.gradientBox.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", true));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", 1.0f, 0.1f, 5.0f,  v -> this.outline.getValue()));
        this.gradientOutline = (Setting<Boolean>)this.register(new Setting("GradientOutline", true,  v -> this.outline.getValue()));
        this.invertGradientOutline = (Setting<Boolean>)this.register(new Setting("InvertGradientOutline", false,  v -> this.gradientOutline.getValue()));
        this.height = (Setting<Double>)this.register(new Setting("Height", 0.0, (-2.0), 2.0));
        this.safeColor = (Setting<Boolean>)this.register(new Setting("SafeColor", true));
        this.safeRed = (Setting<Integer>)this.register(new Setting("SafeRed", 0, 0, 255,  v -> this.safeColor.getValue()));
        this.safeGreen = (Setting<Integer>)this.register(new Setting("SafeGreen", 255, 0, 255,  v -> this.safeColor.getValue()));
        this.safeBlue = (Setting<Integer>)this.register(new Setting("SafeBlue", 0, 0, 255,  v -> this.safeColor.getValue()));
        this.safeAlpha = (Setting<Integer>)this.register(new Setting("SafeAlpha", 255, 0, 255,  v -> this.safeColor.getValue()));
        this.customOutline = (Setting<Boolean>)this.register(new Setting("CustomLine", false,  v -> this.outline.getValue()));
        this.cRed = (Setting<Integer>)this.register(new Setting("OL-Red", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cGreen = (Setting<Integer>)this.register(new Setting("OL-Green", 0, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cBlue = (Setting<Integer>)this.register(new Setting("OL-Blue", 0, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("OL-Alpha", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue()));
        this.safecRed = (Setting<Integer>)this.register(new Setting("OL-SafeRed", 0, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
        this.safecGreen = (Setting<Integer>)this.register(new Setting("OL-SafeGreen", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
        this.safecBlue = (Setting<Integer>)this.register(new Setting("OL-SafeBlue", 0, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
        this.safecAlpha = (Setting<Integer>)this.register(new Setting("OL-SafeAlpha", 255, 0, 255,  v -> this.customOutline.getValue() && this.outline.getValue() && this.safeColor.getValue()));
        this.setInstance();
    }
    
    public static HoleESP getInstance() {
        if (HoleESP.INSTANCE == null) {
            HoleESP.INSTANCE = new HoleESP();
        }
        return HoleESP.INSTANCE;
    }
    
    private void setInstance() {
        HoleESP.INSTANCE = this;
    }
    
    public void onRender3D(final Render3DEvent event) {
        int drawnHoles = 0;
        if (!this.pulsing && this.pulseAlpha.getValue()) {
            final Random rand = new Random();
            this.currentPulseAlpha = rand.nextInt(this.maxPulseAlpha.getValue() - this.minPulseAlpha.getValue() + 1) + this.minPulseAlpha.getValue();
            this.pulsing = true;
            this.shouldDecrease = false;
        }
        if (this.pulseDelay == 0) {
            if (this.pulsing && this.pulseAlpha.getValue() && !this.shouldDecrease) {
                ++this.currentAlpha;
                if (this.currentAlpha >= this.currentPulseAlpha) {
                    this.shouldDecrease = true;
                }
            }
            if (this.pulsing && this.pulseAlpha.getValue() && this.shouldDecrease) {
                --this.currentAlpha;
            }
            if (this.currentAlpha <= 0) {
                this.pulsing = false;
                this.shouldDecrease = false;
            }
            ++this.pulseDelay;
        }
        else {
            ++this.pulseDelay;
            if (this.pulseDelay == 51 - this.pulseSpeed.getValue()) {
                this.pulseDelay = 0;
            }
        }
        if (!this.pulseAlpha.getValue() || !this.pulsing) {
            this.currentAlpha = 0;
        }
        if (fullNullCheck()) {
            return;
        }
        for (final BlockPos pos : Phobos.holeManager.getSortedHoles()) {
            if (drawnHoles >= this.holes.getValue()) {
                break;
            }
            if (pos.equals((Object)new BlockPos(HoleESP.mc.player.posX,  HoleESP.mc.player.posY,  HoleESP.mc.player.posZ)) && !this.ownHole.getValue()) {
                continue;
            }
            if (RotationUtil.isInFov(pos)) {
                continue;
            }
            if (this.safeColor.getValue() && Phobos.holeManager.isSafe(pos)) {
                RenderUtil.drawBoxESP(pos,  new Color(this.safeRed.getValue(),  this.safeGreen.getValue(),  this.safeBlue.getValue(),  this.safeAlpha.getValue()),  this.customOutline.getValue(),  new Color(this.safecRed.getValue(),  this.safecGreen.getValue(),  this.safecBlue.getValue(),  this.safecAlpha.getValue()),  this.lineWidth.getValue(),  this.outline.getValue(),  this.box.getValue(),  this.boxAlpha.getValue(),  true,  this.height.getValue(),  this.gradientBox.getValue(),  this.gradientOutline.getValue(),  this.invertGradientBox.getValue(),  this.invertGradientOutline.getValue(),  this.currentAlpha);
            }
            else {
                RenderUtil.drawBoxESP(pos,  new Color(this.red.getValue(),  this.green.getValue(),  this.blue.getValue(),  this.alpha.getValue()),  this.customOutline.getValue(),  new Color(this.cRed.getValue(),  this.cGreen.getValue(),  this.cBlue.getValue(),  this.cAlpha.getValue()),  this.lineWidth.getValue(),  this.outline.getValue(),  this.box.getValue(),  this.boxAlpha.getValue(),  true,  this.height.getValue(),  this.gradientBox.getValue(),  this.gradientOutline.getValue(),  this.invertGradientBox.getValue(),  this.invertGradientOutline.getValue(),  this.currentAlpha);
            }
            ++drawnHoles;
        }
    }
    
    static {
        HoleESP.INSTANCE = new HoleESP();
    }
}
