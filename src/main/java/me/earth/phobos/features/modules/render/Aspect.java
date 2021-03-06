



package me.earth.phobos.features.modules.render;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.event.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Aspect extends Module
{
    public Setting<Float> aspect;
    
    public Aspect() {
        super("Aspect",  "Change ur screen aspect like fortnite.",  Module.Category.RENDER,  true,  false,  false);
        this.aspect = (Setting<Float>)this.register(new Setting("Alpha", 1.0f, 0.1f, 5.0f));
    }
    
    @SubscribeEvent
    public void onPerspectiveEvent(final PerspectiveEvent perspectiveEvent) {
        perspectiveEvent.setAspect((float)this.aspect.getValue());
    }
}
