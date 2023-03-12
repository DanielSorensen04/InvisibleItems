package me.dalot.invisibleitems;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.dalot.invisibleitems.processors.AllItemsProcessor;
import me.dalot.invisibleitems.processors.HeldItemProcessor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvisibleItems extends JavaPlugin {

    private EquipmentPacketProcessor equipmentPacketProcessor;

    @Override
    public void onEnable() {
        ProtocolLibrary.getProtocolManager()
                .addPacketListener(new PacketAdapter(this, PacketType.Play.Server.ENTITY_EQUIPMENT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (equipmentPacketProcessor == null) return;
                        equipmentPacketProcessor.process(event);
                    }
                });
    }

    public void setEquipmentPacketProcessor(EquipmentPacketProcessor equipmentPacketProcessor) {
        this.equipmentPacketProcessor = equipmentPacketProcessor;
        if (equipmentPacketProcessor != null) {
            equipmentPacketProcessor.onEnable();
        } else {
            EquipmentPacketProcessor.refreshEquipmentOfAllPlayers();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String context = args.length < 1 ? "none" : args[0];

        if (context.equalsIgnoreCase("none")) {
            setEquipmentPacketProcessor(null);
            sender.sendMessage(ChatColor.GREEN + "Fjerner alle hiding packets.");
        } else if (context.equalsIgnoreCase("held")) {
            setEquipmentPacketProcessor(new HeldItemProcessor());
            sender.sendMessage(ChatColor.YELLOW + "Gør nu dit item i hånden usynlig.");
        } else if (context.equalsIgnoreCase("all")) {
            setEquipmentPacketProcessor(new AllItemsProcessor());
            sender.sendMessage(ChatColor.RED + "Gør nu alle items usynlige.");
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "/hide [none | held | all]");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length <= 1) {
            return Arrays.asList("none", "held", "all");
        }

        return new ArrayList<>();
    }

}
