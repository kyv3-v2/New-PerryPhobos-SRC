



package me.earth.phobos.features.modules.misc;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import me.earth.phobos.*;

public class RPC extends Module
{
    public static RPC INSTANCE;
    public Setting<Boolean> catMode;
    public Setting<Boolean> showIP;
    public Setting<String> state;
    
    public RPC() {
        super("RPC",  "Discord rich presence.",  Category.MISC,  false,  false,  false);
        this.catMode = (Setting<Boolean>)this.register(new Setting("CatMode", false));
        this.showIP = (Setting<Boolean>)this.register(new Setting("ShowIP", true,  "Shows the server IP in your discord presence."));
        this.state = (Setting<String>)this.register(new Setting("State", "Perry's Phobos 1.9.0",  "Sets the state of the DiscordRPC."));
        RPC.INSTANCE = this;
    }
    
    @Override
    public void onEnable() {
        DiscordPresence.start();
    }
    
    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}
