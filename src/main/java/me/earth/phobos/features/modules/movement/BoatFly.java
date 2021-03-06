



package me.earth.phobos.features.modules.movement;

import me.earth.phobos.features.modules.*;
import me.earth.phobos.features.setting.*;
import net.minecraft.util.math.*;
import net.minecraft.network.*;
import me.earth.phobos.event.events.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.play.server.*;

public class BoatFly extends Module
{
    public static BoatFly INSTANCE;
    public Setting<Double> speed;
    public Setting<Double> verticalSpeed;
    public Setting<Boolean> noKick;
    public Setting<Boolean> packet;
    public Setting<Integer> packets;
    public Setting<Integer> interact;
    private int teleportID;
    
    public BoatFly() {
        super("BoatFly",  "Boatfly for 2b.",  Module.Category.MOVEMENT,  true,  false,  false);
        this.speed = (Setting<Double>)this.register(new Setting("Speed", 3.0, 1.0, 10.0));
        this.verticalSpeed = (Setting<Double>)this.register(new Setting("VerticalSpeed", 3.0, 1.0, 10.0));
        this.noKick = (Setting<Boolean>)this.register(new Setting("No-Kick", true));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", true));
        this.packets = (Setting<Integer>)this.register(new Setting("Packets", 3, 1, 5,  v -> this.packet.getValue()));
        this.interact = (Setting<Integer>)this.register(new Setting("Delay", 2, 1, 20));
        BoatFly.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (BoatFly.mc.player == null) {
            return;
        }
        if (BoatFly.mc.world == null || BoatFly.mc.player.getRidingEntity() == null) {
            return;
        }
        BoatFly.mc.player.getRidingEntity();
        BoatFly.mc.player.getRidingEntity().setNoGravity(true);
        BoatFly.mc.player.getRidingEntity().motionY = 0.0;
        if (BoatFly.mc.gameSettings.keyBindJump.isKeyDown()) {
            BoatFly.mc.player.getRidingEntity().onGround = false;
            BoatFly.mc.player.getRidingEntity().motionY = this.verticalSpeed.getValue() / 10.0;
        }
        if (BoatFly.mc.gameSettings.keyBindSprint.isKeyDown()) {
            BoatFly.mc.player.getRidingEntity().onGround = false;
            BoatFly.mc.player.getRidingEntity().motionY = -(this.verticalSpeed.getValue() / 10.0);
        }
        final double[] normalDir = this.directionSpeed(this.speed.getValue() / 2.0);
        if (BoatFly.mc.player.movementInput.moveStrafe != 0.0f || BoatFly.mc.player.movementInput.moveForward != 0.0f) {
            BoatFly.mc.player.getRidingEntity().motionX = normalDir[0];
            BoatFly.mc.player.getRidingEntity().motionZ = normalDir[1];
        }
        else {
            BoatFly.mc.player.getRidingEntity().motionX = 0.0;
            BoatFly.mc.player.getRidingEntity().motionZ = 0.0;
        }
        if (this.noKick.getValue()) {
            if (BoatFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                if (BoatFly.mc.player.ticksExisted % 8 < 2) {
                    BoatFly.mc.player.getRidingEntity().motionY = -0.03999999910593033;
                }
            }
            else if (BoatFly.mc.player.ticksExisted % 8 < 4) {
                BoatFly.mc.player.getRidingEntity().motionY = -0.07999999821186066;
            }
        }
        this.handlePackets(BoatFly.mc.player.getRidingEntity().motionX,  BoatFly.mc.player.getRidingEntity().motionY,  BoatFly.mc.player.getRidingEntity().motionZ);
    }
    
    public void handlePackets(final double x,  final double y,  final double z) {
        if (this.packet.getValue()) {
            final Vec3d vec = new Vec3d(x,  y,  z);
            if (BoatFly.mc.player.getRidingEntity() == null) {
                return;
            }
            final Vec3d position = BoatFly.mc.player.getRidingEntity().getPositionVector().add(vec);
            BoatFly.mc.player.getRidingEntity().setPosition(position.x,  position.y,  position.z);
            BoatFly.mc.player.connection.sendPacket((Packet)new CPacketVehicleMove(BoatFly.mc.player.getRidingEntity()));
            for (int i = 0; i < this.packets.getValue(); ++i) {
                BoatFly.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(this.teleportID++));
            }
        }
    }
    
    @SubscribeEvent
    public void onSendPacket(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketVehicleMove && BoatFly.mc.player.isRiding() && BoatFly.mc.player.ticksExisted % this.interact.getValue() == 0) {
            BoatFly.mc.playerController.interactWithEntity((EntityPlayer)BoatFly.mc.player,  BoatFly.mc.player.ridingEntity,  EnumHand.OFF_HAND);
        }
        if ((event.getPacket() instanceof CPacketPlayer.Rotation || event.getPacket() instanceof CPacketInput) && BoatFly.mc.player.isRiding()) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onReceivePacket(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketMoveVehicle && BoatFly.mc.player.isRiding()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.teleportID = ((SPacketPlayerPosLook)event.getPacket()).teleportId;
        }
    }
    
    private double[] directionSpeed(final double speed) {
        float forward = BoatFly.mc.player.movementInput.moveForward;
        float side = BoatFly.mc.player.movementInput.moveStrafe;
        float yaw = BoatFly.mc.player.prevRotationYaw + (BoatFly.mc.player.rotationYaw - BoatFly.mc.player.prevRotationYaw) * BoatFly.mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX,  posZ };
    }
}
