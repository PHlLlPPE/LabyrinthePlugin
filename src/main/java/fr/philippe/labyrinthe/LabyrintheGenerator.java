package fr.philippe.labyrinthe;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.*;

public class LabyrintheGenerator {

    private final int largeur;
    private final int hauteur;
    private final World world;
    private final Location origin;
    private final Random random = new Random();
    private final boolean[][] visitees;

    public LabyrintheGenerator(World world, Location origin, int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.world = world;
        this.origin = origin;
        this.visitees = new boolean[largeur][hauteur];
    }

    public void genererLabyrinthe() {
        genererMurs();          // Étape 1 : Remplir la zone de murs (stone)
        genererDepuis(0, 0);    // Étape 2 : Creuser les chemins dans les murs
        creerEntreeEtSortie();  // Ajouter entrée et sortie
    }

    private void genererMurs() {
        int baseY = origin.getBlockY();

        for (int x = 0; x < largeur * 2 + 1; x++) {
            for (int z = 0; z < hauteur * 2 + 1; z++) {
                for (int h = 0; h < 3; h++) {  // Mur de 3 blocs de haut
                    Block block = world.getBlockAt(origin.getBlockX() + x, baseY + h, origin.getBlockZ() + z);
                    block.setType(Material.STONE);
                }
            }
        }
    }

    private void genererDepuis(int x, int y) {
        visitees[x][y] = true;

        List<int[]> directions = Arrays.asList(
            new int[]{0, -1}, // nord
            new int[]{0, 1},  // sud
            new int[]{-1, 0}, // ouest
            new int[]{1, 0}   // est
        );

        Collections.shuffle(directions, random);

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx >= 0 && nx < largeur && ny >= 0 && ny < hauteur && !visitees[nx][ny]) {
                enleverMurEntre(x, y, nx, ny);
                genererDepuis(nx, ny);
            }
        }
    }

    private void enleverMurEntre(int x1, int y1, int x2, int y2) {
        int bx = origin.getBlockX() + x1 * 2 + 1;
        int bz = origin.getBlockZ() + y1 * 2 + 1;

        setBlockAir(bx, bz);
        setBlockAir(bx + (x2 - x1), bz + (y2 - y1));
        setBlockAir(bx + 2 * (x2 - x1), bz + 2 * (y2 - y1));
    }

    private void setBlockAir(int x, int z) {
        int y = origin.getBlockY(); // Générer au niveau du sol
        for (int h = 0; h < 3; h++) {  // espace pour marcher confortablement
            world.getBlockAt(x, y + h, z).setType(Material.AIR);
        }
    }

    private void creerEntreeEtSortie() {
        // Entrée (côté départ)
        int entreeX = origin.getBlockX() + 1;
        int entreeZ = origin.getBlockZ();
        ouvrirPassage(entreeX, entreeZ);

        // Sortie (côté opposé du labyrinthe)
        int sortieX = origin.getBlockX() + largeur * 2 - 1;
        int sortieZ = origin.getBlockZ() + hauteur * 2;
        ouvrirPassage(sortieX, sortieZ);
    }

    private void ouvrirPassage(int x, int z) {
        int y = origin.getBlockY();
        for (int h = 0; h < 3; h++) {
            world.getBlockAt(x, y + h, z).setType(Material.AIR);
        }
    }
}