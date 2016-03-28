package com.jcdesimp.landlord.landMap;


import com.jcdesimp.landlord.Landlord;
import com.jcdesimp.landlord.persistantData.OwnedLand;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * File created by jcdesimp on 3/1/14.
 */
@SuppressWarnings("SpellCheckingInspection")
public class LandMap {

    Player mapViewer;
    //Scoreboard playerMap;
    int schedulerId;
    Chunk currChunk;
    List<OwnedLand> nearbyLand;
    String currDir;
    private Landlord plugin;

    public LandMap(Player p, Landlord plugin) {
        this.plugin = plugin;
        this.mapViewer=p;
        this.currChunk = p.getLocation().getChunk();
        //System.out.println("CURR: "+currChunk);
        this.nearbyLand = Landlord.getInstance().getDatabase().find(OwnedLand.class)
                .where()
                .ge("x", this.currChunk.getX()-3).le("x", this.currChunk.getX()+3)
                .ge("z", this.currChunk.getZ()-3).le("z",this.currChunk.getZ()+3)
                .eq("worldName", this.currChunk.getWorld().getName())
                .findList();
        //displayMap(mapViewer);
        this.currDir = getPlayerDirection(mapViewer);
        displayMap(mapViewer);


        /*this.schedulerId = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Landlord.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                if(!currDir.equals(getPlayerDirection(mapViewer)) || !currChunk.equals(mapViewer.getLocation().getChunk())){
                    displayMap(mapViewer);
                    currDir = getPlayerDirection(mapViewer);
                }

            }
        }, 0L, 7L);*/

        this.schedulerId = new BukkitRunnable() {
            public void run() {
                if(!currDir.equals(getPlayerDirection(mapViewer)) || !currChunk.equals(mapViewer.getLocation().getChunk())){
                    displayMap(mapViewer);
                    currDir = getPlayerDirection(mapViewer);
                }
            }
        }.runTaskTimer(Landlord.getInstance(), 0L, 7L).getTaskId();

        displayMap(this.mapViewer);


    }

    public static String getPlayerDirection(Player playerSelf){
        String dir;
        float y = playerSelf.getLocation().getYaw();
        if( y < 0 ){y += 360;}
        y %= 360;
        int i = (int)((y+8) / 22.5);
        if(i == 0){dir = "south";}
        else if(i == 1){dir = "south southwest";}
        else if(i == 2){dir = "southwest";}
        else if(i == 3){dir = "west southwest";}
        else if(i == 4){dir = "west";}
        else if(i == 5){dir = "west northwest";}
        else if(i == 6){dir = "northwest";}
        else if(i == 7){dir = "north northwest";}
        else if(i == 8){dir = "north";}
        else if(i == 9){dir = "north northeast";}
        else if(i == 10){dir = "northeast";}
        else if(i == 11){dir = "east northeast";}
        else if(i == 12){dir = "east";}
        else if(i == 13){dir = "east southeast";}
        else if(i == 14){dir = "southeast";}
        else if(i == 15){dir = "south southeast";}
        else {dir = "south";}
        return dir;
    }

    public static String[][] getMapDir(String dir){

        String[][] mapDir = new String[][]{
                {"▓", "▒", "▒", "∞", "▒", "▒", "▓"},
                {"▒", "▓", "▒", "∞", "▒", "▓", "▒"},
                {"▒", "▒", "▓", "∞", "▓", "▒", "▒"},
                {"▓", "▒", "▓", "█", "▒", "▓", "▒"},
                {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
        };

        if(dir.equals("west")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"∞", "∞", "∞", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("west northwest")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"∞", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "∞", "∞", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("northwest")){
            mapDir = new String[][]{
                    {"∞", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "∞", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "∞", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("north northwest")){
            mapDir = new String[][]{
                    {"▓", "∞", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "∞", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "∞", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("north")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "∞", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "∞", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "∞", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("north northeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "∞", "▓"},
                    {"▒", "▓", "▒", "▓", "∞", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "∞", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("northeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "∞"},
                    {"▒", "▓", "▒", "▓", "▒", "∞", "▒"},
                    {"▒", "▒", "▓", "▒", "∞", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("east northeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "∞"},
                    {"▒", "▒", "▓", "▒", "∞", "∞", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("east")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                /**/{"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "∞", "∞", "∞"},
                    {"▓", "▓", "▒", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("east southeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "∞", "∞", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "▒", "∞"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("southeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "∞", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "▓", "∞", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "∞"}
            };
        } else if(dir.equals("south southeast")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "▒", "∞", "▓", "▓"},
                    {"▓", "▒", "▓", "▓", "∞", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "∞", "▒"}
            };
        } else if(dir.equals("south")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "▒", "∞", "▒", "▓", "▓"},
                    {"▓", "▒", "▓", "∞", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "∞", "▓", "▓", "▒"}
            };
        } else if(dir.equals("south southwest")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "∞", "▒", "▒", "▓", "▓"},
                    {"▓", "▒", "∞", "▓", "▓", "▒", "▓"},
                    {"▒", "∞", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("southwest")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "▓", "∞", "▒", "▒", "▓", "▓"},
                    {"▓", "∞", "▓", "▓", "▓", "▒", "▓"},
                    {"∞", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        } else if(dir.equals("west southwest")){
            mapDir = new String[][]{
                    {"▓", "▒", "▒", "▒", "▒", "▒", "▓"},
                    {"▒", "▓", "▒", "▓", "▒", "▓", "▒"},
                    {"▒", "▒", "▓", "▒", "▓", "▒", "▒"},
                    {"▓", "▒", "▓", "\u2062", "▒", "▓", "▒"},
                    {"▓", "∞", "∞", "▒", "▒", "▓", "▓"},
                    {"∞", "▒", "▓", "▓", "▓", "▒", "▓"},
                    {"▒", "▓", "▓", "▓", "▓", "▓", "▒"}
            };
        }

        return mapDir;
    }

    public Player getMapViewer() {
        return mapViewer;
    }

    public void removeMap() {
        mapViewer.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        Bukkit.getServer().getScheduler().cancelTask(schedulerId);

    }

    private Scoreboard displayMap(Player p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        //Scoreboard board = manager.getMainScoreboard();
        Team team = board.registerNewTeam("teamname");
        team.addPlayer(p);

        Objective objective = board.registerNewObjective("Land Map", "dummy");
        /*ChatColor.STRIKETHROUGH+""+ChatColor.DARK_GREEN+
        "=== "+ChatColor.RESET+""+ChatColor.DARK_GREEN +"Land Map"
                +ChatColor.STRIKETHROUGH+""+ChatColor.DARK_GREEN+" ==="*/
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH +
                "==" + ChatColor.RESET + "" + ChatColor.GOLD + " " +
                "Land Map"                               //mess !WARN! if len > 14 map will not render !Enforce
                + " " + ChatColor.YELLOW + "" + ChatColor.STRIKETHROUGH + "==");
        String[] mapData = buildMap(p);
        for (int i = 0; i < mapData.length; i++) {
            if (mapData[i].length() < 21) {
                for (int f = 0; f < (21 - mapData[i].length()); f++) {
                    mapData[i] += ChatColor.RESET;
                }
            }


            //todo THIS BETTER NOT STAY!!!!!!
            class myOfflinePlayer implements OfflinePlayer {
                String name;

                public myOfflinePlayer(String name) {
                    this.name = name;
                }

                public Player getPlayer() {
                    return null;
                }

                public boolean hasPlayedBefore() {
                    return false;
                }

                public String getName() {
                    return name;
                }

                public UUID getUniqueId() {
                    return null;
                }

                public long getFirstPlayed() {
                    return 0;
                }

                public boolean isBanned() {
                    return false;
                }

                @Deprecated
                public void setBanned(boolean b) {
                    return;
                }

                public Map<String, Object> serialize() {
                    return null;
                }

                public boolean isWhitelisted() {
                    return true;
                }

                public void setWhitelisted(boolean b) {
                    return;
                }

                public Location getBedSpawnLocation() {
                    return null;
                }

                public boolean isOnline() {
                    return false;
                }

                public long getLastPlayed() {
                    return 0;
                }

                public boolean isOp() {
                    return false;
                }

                public void setOp(boolean b) {
                    return;
                }

            }

            //OfflinePlayer ofp = Bukkit.getOfflinePlayer(mapData[i].substring(5,17));
            //todo String index out of range (17) ??? What broke?
            OfflinePlayer ofp = new myOfflinePlayer(mapData[i].substring(5, 17));
            //String ofp = mapData[i].substring(5,17);

            Score score = objective.getScore(ofp.getName());

            score.setScore(mapData.length - i);

            Team t = board.registerNewTeam(i + "");
            t.setPrefix(mapData[i].substring(0, 5));
            t.setSuffix(mapData[i].substring(17));
            t.addPlayer(ofp);
            t.setDisplayName(mapData[i]);


            //Score score = objective.getScore(Bukkit.getOfflinePlayer(i + ""));
            //score.setScore((mapData.length)-i);
        }
        //Score score = objective.getScore(Bukkit.getOfflinePlayer()); //Get a fake offline player
        //board.
        p.setScoreboard(board);

        return board;
    }

    public void updateMap(){
        nearbyLand = Landlord.getInstance().getDatabase().find(OwnedLand.class)
                .where()
                .ge("x", currChunk.getX()-4).le("x", currChunk.getX()+4)
                .ge("z", currChunk.getZ()-4).le("z",currChunk.getZ()+4)
                .eq("worldName", currChunk.getWorld().getName())
                .findList();
        currChunk = mapViewer.getLocation().getChunk();
        currDir = "";
    }

    private String[] buildMap(Player p) {
        //String st ="▒▒▒▓▒▒▒\n▒▒▓▓▓▒▒\n▒▓▓▓▓▓▒\n▓▓▓█▓▓▓\n▓▓▒▒▒▓▓\n▓▒▒▒▒▒▓\n▒▒▒▒▒▒▒";

        final int radius = 3;

        String[][] mapBoard = getMapDir(getPlayerDirection(p));

        String[] mapRows = new String[mapBoard.length + 3];

        if(!currChunk.equals(mapViewer.getLocation().getChunk())){
            updateMap();
        }
        for(int z = 0; z < mapBoard.length; z++){
            String row = "";

            //if curr chunk
            for(int x = 0; x < mapBoard[z].length; x++){
                List<OwnedLand> filteredList =
                        Landlord.getInstance().getDatabase().filter(OwnedLand.class)
                                .eq("x", currChunk.getX()-radius+x)
                                .eq("z", currChunk.getZ()-radius+z)
                                .eq("worldName", currChunk.getWorld().getName())
                                .filter(nearbyLand);
                String currSpot = mapBoard[z][x];


                if(!filteredList.isEmpty()){
                    OwnedLand ol = filteredList.get(0);
                    if(ol.ownerUUID().equals(p.getUniqueId())){
                        currSpot = ChatColor.GREEN + currSpot;
                    } else if(ol.isFriend(p)){
                        currSpot = ChatColor.YELLOW + currSpot;
                    } else {
                        currSpot = ChatColor.RED + currSpot;
                    }
                } else {
                    if(currSpot.equals("∞") || currSpot.equals("\u2062")){
                        currSpot = ChatColor.RESET + currSpot;
                    } else {
                    currSpot = ChatColor.GRAY + currSpot;
                    }
                }

                //System.out.println(currSpot);
                row += currSpot;

            }

            //if currchunk changed
            mapRows[z] = row;

        }

        //TODO

        mapRows[mapRows.length - 3] = ChatColor.GREEN + "█-";         //mess if >28 map won't render
        mapRows[mapRows.length - 2] = ChatColor.YELLOW + "█-";   //mess if >28 map won't render
        mapRows[mapRows.length - 1] = ChatColor.RED + "█-";       //mess if >28 map won't render
        //mapRows[0] = "";

        FileConfiguration messages = plugin.getMessageConfig();

        final String yours = messages.getString("map.labels.yours");
        final String friends = messages.getString("map.labels.friends");
        final String others = messages.getString("map.labels.others");

        if (yours.length() <= 26) {
            mapRows[mapRows.length - 3].concat(yours);
        } else {
            mapRows[mapRows.length - 3].concat("Yours");
        }

        if (friends.length() <= 26) {
            mapRows[mapRows.length - 2].concat(friends);
        } else {
            mapRows[mapRows.length - 2].concat("Friends'");
        }

        if (others.length() <= 26) {
            mapRows[mapRows.length - 1].concat(friends);
        } else {
            mapRows[mapRows.length - 1].concat("Others'");
        }

        return mapRows;
    }

}
