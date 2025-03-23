package fr.philippe.labyrinthe;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        getLogger().info("LabyrinthePlugin activé avec succès !");
        this.getCommand("labyrinthe").setExecutor(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("LabyrinthePlugin désactivé !");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage("§aGénération du labyrinthe lancée !");

            // Récupération de la position actuelle du joueur
            LabyrintheGenerator labyrinthe = new LabyrintheGenerator(player.getWorld(), player.getLocation(), 50, 50);

            // Génération effective du labyrinthe
            labyrinthe.genererLabyrinthe();

            player.sendMessage("§aLabyrinthe terminé !");
        } else {
            sender.sendMessage("Cette commande ne peut être exécutée que par un joueur.");
        }

        return true;
    }
}
