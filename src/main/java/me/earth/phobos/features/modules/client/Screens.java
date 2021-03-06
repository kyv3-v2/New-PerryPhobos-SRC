



package me.earth.phobos.features.modules.client;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;

public class Screens extends Module
{
    public static Screens INSTANCE;
    public Setting<Boolean> mainScreen;
    
    public Screens() {
        super("Screens",  "Controls custom screens used by the client.",  Category.CLIENT,  true,  false,  false);
        this.mainScreen = (Setting<Boolean>)this.register(new Setting("MainScreen", false));
        Screens.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
    }
}
