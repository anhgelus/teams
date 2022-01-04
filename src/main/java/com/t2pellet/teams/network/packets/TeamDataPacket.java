package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.client.core.ClientTeamDB;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;

public class TeamDataPacket extends ClientPacket {

    private static final String TEAM_KEY = "teamName";
    private static final String TYPE_KEY = "type";

    public enum Type {
        ADD,
        REMOVE,
        ONLINE,
        OFFLINE,
        CLEAR
    }

    public TeamDataPacket(Type type, String... teams) {
        NbtList nbtList = new NbtList();
        for (var team : teams) {
            nbtList.add(NbtString.of(team));
        }
        tag.put(TEAM_KEY, nbtList);
        tag.putString(TYPE_KEY, type.name());
    }

    public TeamDataPacket(MinecraftClient client, PacketByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        Type type = Type.valueOf(tag.getString(TYPE_KEY));
        NbtList nbtList = tag.getList(TEAM_KEY, NbtElement.STRING_TYPE);
        for (var elem : nbtList) {
            String team = elem.asString();
            switch (type) {
                case ADD -> ClientTeamDB.INSTANCE.addTeam(team);
                case REMOVE -> ClientTeamDB.INSTANCE.removeTeam(team);
                case ONLINE -> ClientTeamDB.INSTANCE.teamOnline(team);
                case OFFLINE -> ClientTeamDB.INSTANCE.teamOffline(team);
                case CLEAR -> ClientTeamDB.INSTANCE.clear();
            }
        }
    }

}
