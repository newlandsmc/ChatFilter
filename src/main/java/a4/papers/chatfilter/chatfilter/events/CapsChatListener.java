package a4.papers.chatfilter.chatfilter.events;

import a4.papers.chatfilter.chatfilter.ChatFilter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapsChatListener implements Listener {

    ChatFilter chatFilter;

    public CapsChatListener(ChatFilter instance) {
        this.chatFilter = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCaps(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();
        if (chatFilter.deCap) {
            if (event.getPlayer().hasPermission("chatfilter.bypass") || event.getPlayer().hasPermission("chatfilter.bypass.caps"))
                return;
            if (isURL(msg))
                return;
            char[] charArray = msg.toCharArray();
            int counter = 0;
            for (char c : charArray) {
                if (Character.isUpperCase(c)) counter++;
            }
            if (counter >= chatFilter.capsAmount) {
                msg = msg.charAt(0) + msg.substring(1).toLowerCase();
            }
            String newmsg = msg;
            for (Player p : Bukkit.getOnlinePlayers()) {
                String player = p.getName();

                if (msg.toLowerCase().contains(player.toLowerCase())) {
                    newmsg = newmsg.replace(player.toLowerCase(), player);
                }
            }
            event.setMessage(newmsg);
        }
    }

    public boolean isURL(String str) {
        boolean matched = false;
        Pattern p = Pattern.compile(chatFilter.URL_REGEX);
        Matcher m = p.matcher(str);
        if (m.find()) {
            matched = true;
        }
        return matched;
    }
}
