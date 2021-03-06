



package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.util.*;

public class VanillaSpeed extends Module
{
    public Setting<Double> speed;
    
    public VanillaSpeed() {
        super("VanillaSpeed",  "Speed for vanilla ac's (old ec.me).",  Module.Category.MOVEMENT,  true,  false,  false);
        this.speed = (Setting<Double>)this.register(new Setting("Speed", 1.0, 1.0, 20.0));
    }
    
    public void onUpdate() {
        if (VanillaSpeed.mc.player == null || VanillaSpeed.mc.world == null) {
            return;
        }
        final double[] calc = MathUtil.directionSpeed(this.speed.getValue() / 10.0);
        VanillaSpeed.mc.player.motionX = calc[0];
        VanillaSpeed.mc.player.motionZ = calc[1];
    }
}
