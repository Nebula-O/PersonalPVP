package xyz.cosmicity.personalpvp.managers;

import org.bukkit.Bukkit;
import xyz.cosmicity.personalpvp.Config;
import xyz.cosmicity.personalpvp.PPVPPlugin;
import xyz.cosmicity.personalpvp.Utils;
import xyz.cosmicity.personalpvp.storage.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskManager {

    private static List<UUID> ignoredValues = new ArrayList<>();
    private static final List<UUID> onlineUuids = new ArrayList<>();

    private static int actionbarTask= -1;

    public static void stop() {
        if(actionbarTask==-1) return;
        PPVPPlugin.inst().getServer().getScheduler().cancelTask(actionbarTask);
    }

    private static int hours = 0, minutes = 0;
    private static String suffix = "";
    private static void setHours(final int hrs) {
        hours = hrs;
    }private static void setMins(final int mins) {
        minutes = mins;
    }private static void setSuffix(final String str) {
        suffix = str;
    }

    public static void start() {
        actionbarTask = PPVPPlugin.inst().getServer().getScheduler().scheduleSyncRepeatingTask(PPVPPlugin.inst(), () -> {
            String actionbarMessage = Config.actionbar_message();
            if (actionbarMessage.contains("<worldtime>")) {
                int time = (int) (Bukkit.getWorld(Config.worldtime_in_world()).getTime()) + 6000, tmpHours, tmpMinutes;
                final String sfx = (24000 <= time && time <= 30000) || time <= 12000 ? "am" : "pm";
                tmpHours = (int) (time / 1000f) - 12;
                tmpHours += tmpHours < 1 ? 12 : (tmpHours > 12 ? -12 : 0);
                tmpMinutes = (int) Math.floor((time % 1000) / 16.7);
                setHours(tmpHours);
                setMins(tmpMinutes);
                setSuffix(sfx);
            }
            if(onlineUuids.size()>0) {
                onlineUuids.stream().filter(TaskManager::ignoredNegative).forEach(u -> sendUpdate(u, PPVPPlugin.inst()));
            }
            }, 20L, 17L);//17L
    }

    public static boolean ignoredPositive(final UUID u) {
        return Config.default_actionbar_status() == ignoredValues.contains(u);
    }
    public static boolean ignoredNegative(final UUID u) {
        return Config.default_actionbar_status() != ignoredValues.contains(u);
    }

    public static void sendUpdate(final UUID u, final PPVPPlugin pl) {
        if(ignoredPositive(u)) return;
        if(Bukkit.getPlayer(u)==null || ignoredPositive(u)) return;
        sendInstantUpdate(u);
    }

    public static void sendInstantUpdate(final UUID u) {
        Utils.send(Bukkit.getPlayer(u), Utils.parse(Config.actionbar_message(),
                "pvpprefix",
                Config.actionbar_pvp_prefixes()[PVPManager.pvpPositive(u) ? 0 : 1],
                "pvpstatus",
                Config.actionbar_pvp_statuses()[PVPManager.pvpPositive(u) ? 0 : 1],
                "worldtime", (hours<10?"0":"")+hours+":"+(minutes<10?"0":"")+minutes+suffix),
                false, true);
    }
    public static void sendInstantUpdate(final UUID u, final Message msg) {
        Utils.send(msg.parse("pvpprefix",Config.actionbar_pvp_prefixes()[PVPManager.pvpPositive(u) ? 0 : 1],
                "pvpstatus",Config.actionbar_pvp_statuses()[PVPManager.pvpPositive(u) ? 0 : 1],
                "worldtime", (hours<10?"0":"")+hours+":"+(minutes<10?"0":"")+minutes+suffix),Bukkit.getPlayer(u));
    }

    public static boolean toggleHidden(final UUID uuid) {
        boolean c = ignoredValues.contains(uuid);
        if(c) ignoredValues.remove(uuid);
        else ignoredValues.add(uuid);
        return Config.default_actionbar_status() == c;
    }

    public static void sendJoinDuration(final UUID u, final PPVPPlugin pl) {
        for(int i=0;i<Config.actionbar_login_duration()+1;i++) {
            pl.getServer().getScheduler().scheduleSyncDelayedTask(pl, () -> TaskManager.sendInstantUpdate(u), i * 20L);
        }
    }

    public static void blockedAttack(final UUID... us) {
        if(Config.actionbar_attack_duration()<1) return;
        PPVPPlugin pl = PPVPPlugin.inst();
        Message reminder = Config.pvp_on_reminder();
        for(UUID u : us) {
            if (ignoredPositive(u) && !PVPManager.isPvpEnabled(u)) {
                for (int i = 0; i < Config.actionbar_attack_duration()+1; i++) {
                    pl.getServer().getScheduler().scheduleSyncDelayedTask(pl, () -> TaskManager.sendInstantUpdate(u, reminder), i * 20L);
                }
            }
        }
    }

    public static List<UUID> ignoredValues() {return ignoredValues;}

    public static void load() {
        if(Utils.loaded().get(1)!=null) ignoredValues = Utils.loaded().get(1);
    }

    public static void addUuid(final UUID u) {
        onlineUuids.add(u);
    }
    public static void remUuid(final UUID u) {
        onlineUuids.remove(u);
    }

}
