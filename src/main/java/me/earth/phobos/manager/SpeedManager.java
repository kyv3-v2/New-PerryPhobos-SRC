



package me.earth.phobos.manager;

import me.earth.phobos.features.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import me.earth.phobos.features.modules.client.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.util.math.*;

public class SpeedManager extends Feature
{
    public static final double LAST_JUMP_INFO_DURATION_DEFAULT = 3.0;
    public static boolean didJumpThisTick;
    public static boolean isJumping;
    public double firstJumpSpeed;
    public double lastJumpSpeed;
    public double percentJumpSpeedChanged;
    public double jumpSpeedChanged;
    public boolean didJumpLastTick;
    public long jumpInfoStartTime;
    public boolean wasFirstJump;
    public double speedometerCurrentSpeed;
    public HashMap<EntityPlayer,  Double> playerSpeeds;
    
    public SpeedManager() {
        this.wasFirstJump = true;
        this.playerSpeeds = new HashMap<EntityPlayer,  Double>();
    }
    
    public static void setDidJumpThisTick(final boolean val) {
        SpeedManager.didJumpThisTick = val;
    }
    
    public static void setIsJumping(final boolean val) {
        SpeedManager.isJumping = val;
    }
    
    public float lastJumpInfoTimeRemaining() {
        return (Minecraft.getSystemTime() - this.jumpInfoStartTime) / 1000.0f;
    }
    
    public void updateValues() {
        final double distTraveledLastTickX = SpeedManager.mc.player.posX - SpeedManager.mc.player.prevPosX;
        final double distTraveledLastTickZ = SpeedManager.mc.player.posZ - SpeedManager.mc.player.prevPosZ;
        this.speedometerCurrentSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
        if (SpeedManager.didJumpThisTick && (!SpeedManager.mc.player.onGround || SpeedManager.isJumping)) {
            if (!this.didJumpLastTick) {
                this.wasFirstJump = (this.lastJumpSpeed == 0.0);
                this.percentJumpSpeedChanged = ((this.speedometerCurrentSpeed != 0.0) ? (this.speedometerCurrentSpeed / this.lastJumpSpeed - 1.0) : -1.0);
                this.jumpSpeedChanged = this.speedometerCurrentSpeed - this.lastJumpSpeed;
                this.jumpInfoStartTime = Minecraft.getSystemTime();
                this.lastJumpSpeed = this.speedometerCurrentSpeed;
                this.firstJumpSpeed = (this.wasFirstJump ? this.lastJumpSpeed : 0.0);
            }
            this.didJumpLastTick = SpeedManager.didJumpThisTick;
        }
        else {
            this.didJumpLastTick = false;
            this.lastJumpSpeed = 0.0;
        }
        if (Management.getInstance().speed.getValue()) {
            this.updatePlayers();
        }
    }
    
    public void updatePlayers() {
        for (final EntityPlayer player : SpeedManager.mc.world.playerEntities) {
            final int distancer = 20;
            if (SpeedManager.mc.player.getDistanceSq((Entity)player) >= distancer * distancer) {
                continue;
            }
            final double distTraveledLastTickX = player.posX - player.prevPosX;
            final double distTraveledLastTickZ = player.posZ - player.prevPosZ;
            final double playerSpeed = distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ;
            this.playerSpeeds.put(player,  playerSpeed);
        }
    }
    
    public double getPlayerSpeed(final EntityPlayer player) {
        if (this.playerSpeeds.get(player) == null) {
            return 0.0;
        }
        return this.turnIntoKpH(this.playerSpeeds.get(player));
    }
    
    public double turnIntoKpH(final double input) {
        return MathHelper.sqrt(input) * 71.2729367892;
    }
    
    public double getSpeedKpH() {
        double speedometerkphdouble = this.turnIntoKpH(this.speedometerCurrentSpeed);
        speedometerkphdouble = Math.round(10.0 * speedometerkphdouble) / 10.0;
        return speedometerkphdouble;
    }
    
    public double getSpeedMpS() {
        double speedometerMpsdouble = this.turnIntoKpH(this.speedometerCurrentSpeed) / 3.6;
        speedometerMpsdouble = Math.round(10.0 * speedometerMpsdouble) / 10.0;
        return speedometerMpsdouble;
    }
}
