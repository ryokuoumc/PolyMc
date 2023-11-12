package nl.theepicblock.polymc.testmod.automated;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.*;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import xyz.nucleoid.packettweaker.PacketContext;

import java.io.IOException;
import java.util.ArrayList;

public class FakeNetworkHandler extends ServerPlayNetworkHandler {
    public ArrayList<Packet<?>> sentPackets = new ArrayList<>();

    public FakeNetworkHandler(MinecraftServer server, ServerPlayerEntity player) {
        super(server, new ClientConnection(NetworkSide.CLIENTBOUND), player, ConnectedClientData.createDefault(player.getGameProfile()));
    }

    @Override
    public void send(Packet<?> packet, @Nullable PacketCallbacks callbacks) {
        if (packet instanceof BundleS2CPacket bundle) {
            for (var packet2 : bundle.getPackets()) {
                this.send(packet2, callbacks);
            }
            return;
        }

        // reserialize packet
        this.sentPackets.add(reencode(packet));
    }

    public <T extends Packet<?>> T reencode(T packet) {
        if (packet instanceof BundleS2CPacket) {
            throw new IllegalArgumentException("Can't reencode bundles as of now");
        }

        var bytebuf = PacketByteBufs.create();
        PacketContext.setContext(connection, packet);
        var id = NetworkState.PLAY.getHandler(NetworkSide.CLIENTBOUND).getId(packet);
        if (id == -1) {
            throw new UnsupportedOperationException("Can't find packet id of "+packet.getClass() + ". Is it not clientbound?");
        }
        var reconstructedPacket = NetworkState.PLAY.getHandler(NetworkSide.CLIENTBOUND).createPacket(id, bytebuf);

        return (T)reconstructedPacket;
    }
}
