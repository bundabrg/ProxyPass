package com.nukkitx.proxypass.network;

import com.nukkitx.protocol.bedrock.BedrockPong;
import com.nukkitx.protocol.bedrock.BedrockServerEventHandler;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.proxypass.ProxyPass;
import com.nukkitx.proxypass.network.bedrock.session.UpstreamPacketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.net.InetSocketAddress;

@Log4j2
@ParametersAreNonnullByDefault
public class ProxyBedrockEventHandler implements BedrockServerEventHandler {

    private final ProxyPass proxy;
    private final BedrockPong ADVERTISEMENT = new BedrockPong();

    public ProxyBedrockEventHandler(ProxyPass proxy) {
        this.proxy = proxy;

        if (proxy.getConfiguration().isEducation()) {
            ADVERTISEMENT.setEdition("MCEE");
        } else {
            ADVERTISEMENT.setEdition("MCPE");
        }
        ADVERTISEMENT.setGameType("Survival");
        ADVERTISEMENT.setVersion(ProxyPass.MINECRAFT_VERSION);
        ADVERTISEMENT.setProtocolVersion(ProxyPass.PROTOCOL_VERSION);
        ADVERTISEMENT.setMotd("ProxyPass");
        ADVERTISEMENT.setPlayerCount(0);
        ADVERTISEMENT.setMaximumPlayerCount(20);
        ADVERTISEMENT.setSubMotd("https://github.com/NukkitX/ProxyPass");

    }

    @Override
    public boolean onConnectionRequest(InetSocketAddress address) {
        return true;
    }

    @Nonnull
    public BedrockPong onQuery(InetSocketAddress address) {
        return ADVERTISEMENT;
    }

    @Override
    public void onSessionCreation(BedrockServerSession session) {
        session.setPacketHandler(new UpstreamPacketHandler(session, this.proxy));
    }
}
