package xyz.pyxismc.manhunt;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.time.Duration;

public class WinListener implements Listener {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Check if the entity is an Ender Dragon
        if (!(event.getEntity() instanceof EnderDragon)) return;

        // Check if the killer is a player
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;

        // Check if the killer is a runner
        if (killer.hasPermission("manhunt.runner")) {
            announceRunnerVictory(killer);
        }
    }

    private void announceRunnerVictory(Player killer) {
        // Get the seed from the overworld
        World world = Bukkit.getWorld("world");
        long seed = world != null ? world.getSeed() : 0;
        Manhunt manhuntPlugin = (Manhunt) Bukkit.getPluginManager().getPlugin("Manhunt");
        String elapsedTime = manhuntPlugin.getStartGameListener().getElapsedTime();
        manhuntPlugin.getStartGameListener().stopTimer();

        // Announce to all runners that they won
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("manhunt.runner")) {
                // Send victory title to runners
                Title title = Title.title(
                        miniMessage.deserialize("<gradient:#0fb800:#39db6c><bold>VICTORY!"),
                        miniMessage.deserialize(""),
                        Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(4), Duration.ofMillis(500))
                );
                player.showTitle(title);

                // Play victory sound
                player.playSound(player.getLocation(), "minecraft:ui.toast.challenge_complete", 1.0f, 1.0f);

                // Send message with seed
                player.sendMessage(miniMessage.deserialize(" "));
                player.sendMessage(miniMessage.deserialize("<#e61717>⚔ <dark_gray>» <gray>The runners team has won the Game <#e61717>" + killer.getName() + " <gray>has defeated the Ender Dragon!"));
                player.sendMessage(miniMessage.deserialize("<#e61717>⚔ <dark_gray>»<gray> World seed <dark_gray>»<#e61717>" + seed));
                player.sendMessage(miniMessage.deserialize("<#e61717>⚔ <dark_gray>»<gray> Game Duration <dark_gray>»<#e61717>" + elapsedTime));
                player.sendMessage(miniMessage.deserialize(" "));
            }
            else if (player.hasPermission("manhunt.hunter")) {
                // Send defeat title to hunters
                Title title = Title.title(
                        miniMessage.deserialize("<#e61717><bold>DEFEAT!"),
                        miniMessage.deserialize(""),
                        Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(4), Duration.ofMillis(500))
                );
                player.showTitle(title);

                // Play defeat sound
                player.playSound(player.getLocation(), "minecraft:entity.wither.death", 0.5f, 0.8f);

                // Send message with seed
                player.sendMessage(miniMessage.deserialize(" "));
                player.sendMessage(miniMessage.deserialize("<#e61717>⚔ <dark_gray>» <gray>The runners team has won the Game <#e61717>" + killer.getName() + " <gray>has defeated the Ender Dragon!"));
                player.sendMessage(miniMessage.deserialize("<#e61717>⚔ <dark_gray>»<gray> World seed <dark_gray>»<#e61717>" + seed));
                player.sendMessage(miniMessage.deserialize("<#e61717>⚔ <dark_gray>»<gray> Game Duration <dark_gray>»<#e61717>" + elapsedTime));
                player.sendMessage(miniMessage.deserialize(" "));
            }
        }
    }
}
