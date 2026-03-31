package com.github.epsilon.managers;

import com.github.epsilon.events.PacketEvent;
import com.github.epsilon.events.TotemPopEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;

public class SyncManager {

    public static final SyncManager INSTANCE = new SyncManager();

    private SyncManager() {
    }

    public void initSync() {
        NeoForge.EVENT_BUS.register(this);
    }

    private static final Minecraft mc = Minecraft.getInstance();

    private final HashMap<String, Integer> popList = new HashMap<>();

    /**
     * TODO: 给他做个Inventory服务端的同步 避免InvUtils切换傻了
     */
    @SubscribeEvent
    private void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null || mc.level == null) return;
        if (event.getPacket() instanceof ClientboundEntityEventPacket packet && packet.getEventId() == EntityEvent.PROTECTED_FROM_DEATH) {
            if (!(packet.getEntity(mc.level) instanceof Player player)) return;
            int pops = popList.merge(player.getName().getString(), 1, Integer::sum);
            NeoForge.EVENT_BUS.post(new TotemPopEvent(player, pops));
        }
    }

}
