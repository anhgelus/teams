package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.network.Packet;

public class TeamPackets {

    private TeamPackets() {
    }

    // Client
    public static final Packet.PacketKey<TeamDataPacket> TEAM_DATA_PACKET = new Packet.PacketKey<>(TeamDataPacket.class, "message_team_data");
    public static final Packet.PacketKey<TeamInvitePacket> TEAM_INVITE_PACKET = new Packet.PacketKey<>(TeamInvitePacket.class, "message_team_invite");

    // Server
    public static final Packet.PacketKey<TeamJoinPacket> TEAM_INVITE_RESPONSE_PACKET = new Packet.PacketKey<>(TeamJoinPacket.class, "message_team_invite_response");
}
