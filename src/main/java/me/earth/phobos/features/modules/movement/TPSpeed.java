



package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.util.math.*;
import me.earth.phobos.event.events.*;
import me.earth.phobos.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class TPSpeed extends Module
{
    private final Setting<Mode> mode;
    private final Setting<Double> speed;
    private final Setting<Double> fallSpeed;
    private final Setting<Boolean> turnOff;
    private final Setting<Integer> tpLimit;
    private final double[] selectedPositions;
    private int tps;
    
    public TPSpeed() {
        super("TpSpeed",  "Teleports you.",  Module.Category.MOVEMENT,  true,  false,  false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", Mode.NORMAL));
        this.speed = (Setting<Double>)this.register(new Setting("Speed", 0.25, 0.1, 10.0));
        this.fallSpeed = (Setting<Double>)this.register(new Setting("FallSpeed", 0.25, 0.1, 10.0,  v -> this.mode.getValue() == Mode.STEP));
        this.turnOff = (Setting<Boolean>)this.register(new Setting("Off", false));
        this.tpLimit = (Setting<Integer>)this.register(new Setting("Limit", 2, 1, 10,  v -> this.turnOff.getValue(),  "Turn it off."));
        this.selectedPositions = new double[] { 0.42,  0.75,  1.0 };
    }
    
    private static boolean collidesHorizontally(final AxisAlignedBB bb) {
        if (TPSpeed.mc.world.collidesWithAnyBlock(bb)) {
            final Vec3d center = bb.getCenter();
            final BlockPos blockpos = new BlockPos(center.x,  bb.minY,  center.z);
            return TPSpeed.mc.world.isBlockFullCube(blockpos.west()) || TPSpeed.mc.world.isBlockFullCube(blockpos.east()) || TPSpeed.mc.world.isBlockFullCube(blockpos.north()) || TPSpeed.mc.world.isBlockFullCube(blockpos.south()) || TPSpeed.mc.world.isBlockFullCube(blockpos);
        }
        return false;
    }
    
    public void onEnable() {
        this.tps = 0;
    }
    
    @SubscribeEvent
    public void onUpdatePlayerWalking(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        if (this.mode.getValue() == Mode.NORMAL) {
            if (this.turnOff.getValue() && this.tps >= this.tpLimit.getValue()) {
                this.disable();
                return;
            }
            if (TPSpeed.mc.player.moveForward != 0.0f || (TPSpeed.mc.player.moveStrafing != 0.0f && TPSpeed.mc.player.onGround)) {
                for (double x = 0.0625; x < this.speed.getValue(); x += 0.262) {
                    final double[] dir = MathUtil.directionSpeed(x);
                    TPSpeed.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(TPSpeed.mc.player.posX + dir[0],  TPSpeed.mc.player.posY,  TPSpeed.mc.player.posZ + dir[1],  TPSpeed.mc.player.onGround));
                }
                TPSpeed.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(TPSpeed.mc.player.posX + TPSpeed.mc.player.motionX,  0.0,  TPSpeed.mc.player.posZ + TPSpeed.mc.player.motionZ,  TPSpeed.mc.player.onGround));
                ++this.tps;
            }
        }
        else if ((TPSpeed.mc.player.moveForward != 0.0f || TPSpeed.mc.player.moveStrafing != 0.0f) && TPSpeed.mc.player.onGround) {
            double pawnY = 0.0;
            final double[] lastStep = MathUtil.directionSpeed(0.262);
            for (double x2 = 0.0625; x2 < this.speed.getValue(); x2 += 0.262) {
                double[] dir2;
                AxisAlignedBB bb;
                double[] selectedPositions;
                int length;
                int j;
                double position;
                for (dir2 = MathUtil.directionSpeed(x2),  bb = Objects.requireNonNull(TPSpeed.mc.player.getEntityBoundingBox()).offset(dir2[0],  pawnY,  dir2[1]); collidesHorizontally(bb); bb = Objects.requireNonNull(TPSpeed.mc.player.getEntityBoundingBox()).offset(dir2[0],  ++pawnY,  dir2[1])) {
                    selectedPositions = this.selectedPositions;
                    for (length = selectedPositions.length,  j = 0; j < length; ++j) {
                        position = selectedPositions[j];
                        TPSpeed.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(TPSpeed.mc.player.posX + dir2[0] - lastStep[0],  TPSpeed.mc.player.posY + pawnY + position,  TPSpeed.mc.player.posZ + dir2[1] - lastStep[1],  true));
                    }
                }
                if (!TPSpeed.mc.world.checkBlockCollision(bb.grow(0.0125,  0.0,  0.0125).offset(0.0,  -1.0,  0.0))) {
                    for (double i = 0.0; i <= 1.0; i += this.fallSpeed.getValue()) {
                        TPSpeed.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(TPSpeed.mc.player.posX + dir2[0],  TPSpeed.mc.player.posY + pawnY - i,  TPSpeed.mc.player.posZ + dir2[1],  true));
                    }
                    --pawnY;
                }
                TPSpeed.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(TPSpeed.mc.player.posX + dir2[0],  TPSpeed.mc.player.posY + pawnY,  TPSpeed.mc.player.posZ + dir2[1],  TPSpeed.mc.player.onGround));
            }
            TPSpeed.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(TPSpeed.mc.player.posX + TPSpeed.mc.player.motionX,  0.0,  TPSpeed.mc.player.posZ + TPSpeed.mc.player.motionZ,  TPSpeed.mc.player.onGround));
        }
    }
    
    public enum Mode
    {
        NORMAL,  
        STEP;
    }
}
