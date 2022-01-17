package a4.papers.chatfilter.chatfilter;

import a4.papers.chatfilter.chatfilter.lang.enumStrings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Commands implements CommandExecutor {

    ChatFilter chatFilter;

    public Commands(ChatFilter instance) {
        chatFilter = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearchat")) {
            if (!sender.hasPermission("chatfilter.clear")) {
                sender.sendMessage(chatFilter.replaceString(chatFilter.mapToString(enumStrings.NO_PERMISSION.s), sender));
            } else if (sender.hasPermission("chatfilter.clear")) {
                for (Player nopermplayer: Bukkit.getServer().getOnlinePlayers()) {
                    if (!nopermplayer.hasPermission("chatfilter.bypass"))
                        for (int i = 0; i < 100; i++) {
                            nopermplayer.sendMessage(" ");
                        }
                }
                for (Player permplayer: Bukkit.getServer().getOnlinePlayers()) {
                    if (permplayer.hasPermission("chatfilter.bypass") || permplayer.hasPermission("chatfilter.view"))
                        permplayer.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.clearChatMessage.s).replace("%player%", sender.getName())));
                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("chatfilter")) {
            if (args.length == 0) {
                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.NO_ARGS.s)));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("chatfilter.reload")) {
                    chatFilter.reloadConfig();
                    chatFilter.saveConfig();
                    chatFilter.byPassWords.clear();
                    chatFilter.byPassDNS.clear();
                    chatFilter.regExDNS.clear();
                    chatFilter.regExWords.clear();
                    chatFilter.regExWords = chatFilter.getConfig().getStringList("filteredWords");
                    chatFilter.regExDNS = chatFilter.getConfig().getStringList("filteredIPandDNS");
                    chatFilter.byPassWords = chatFilter.getConfig().getStringList("bypassWords");
                    chatFilter.byPassDNS = chatFilter.getConfig().getStringList("bypassIP");
                    sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CONFIG_RELOADED.s)));
                } else {
                    sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.NO_PERMISSION.s)));
                }
            } else {
                if (args[0].equals("help")) {
                    sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.HELP_VERSION.s).replace("%version%", chatFilter.getDescription().getVersion())));
                    sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.HOVER_OVER_COMMAND_TITLE.s)));
                    TextComponent clearchat = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_CLEAR.s)));
                    clearchat.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_CLEAR_HOVER.s))).create()));

                    TextComponent messageblacklistw = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_WORD.s)));
                    messageblacklistw.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_WORD_HOVER.s))).create()));
                    messageblacklistw.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chatfilter blacklist add word "));

                    TextComponent messageblacklistip = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_IP.s)));
                    messageblacklistip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_IP_HOVER.s))).create()));
                    messageblacklistip.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chatfilter blacklist add ip "));

                    TextComponent messageblacklistlist = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_LIST.s)));
                    messageblacklistlist.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_IP_HOVER.s))).create()));
                    messageblacklistlist.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chatfilter blacklist list "));

                    TextComponent messagewhitelistword = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_WORD.s)));
                    messagewhitelistword.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_WORD_HOVER.s))).create()));
                    messagewhitelistword.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chatfilter whitelist add word "));

                    TextComponent messagewhitelistip = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_IP.s)));
                    messagewhitelistip.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_IP_HOVER.s))).create()));
                    messagewhitelistip.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chatfilter whitelist add ip "));

                    TextComponent messagewhitelistlist = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_LIST.s)));
                    messagewhitelistlist.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_LIST_HOVER.s))).create()));
                    messagewhitelistlist.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chatfilter whitelist list "));

                    TextComponent messageblacklistremove = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_REMOVE.s)));
                    messageblacklistremove.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_REMOVE_HOVER.s))).create()));
                    messageblacklistremove.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chatfilter blacklist remove "));

                    TextComponent messagewhitelistremove = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_REMOVE.s)));
                    messagewhitelistremove.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_REMOVE_HOVER.s))).create()));
                    messagewhitelistremove.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chatfilter whitelist remove "));

                    TextComponent messagepause = new TextComponent(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_PAUSE.s)));
                    messagepause.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_PAUSE_HOVER.s))).create()));
                    messagepause.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/chatfilter pause"));

                    sender.spigot().sendMessage(clearchat);
                    sender.spigot().sendMessage(messagepause);
                    sender.spigot().sendMessage(messageblacklistw);
                    sender.spigot().sendMessage(messageblacklistip);
                    sender.spigot().sendMessage(messagewhitelistword);
                    sender.spigot().sendMessage(messagewhitelistip);
                    sender.spigot().sendMessage(messageblacklistlist);
                    sender.spigot().sendMessage(messagewhitelistlist);
                    sender.spigot().sendMessage(messageblacklistremove);
                    sender.spigot().sendMessage(messagewhitelistremove);
                    return true;
                }
                if (args[0].equals("clear")) {
                    if (!sender.hasPermission("chatfilter.clear")) {
                        sender.sendMessage(chatFilter.replaceString(chatFilter.mapToString(enumStrings.NO_PERMISSION.s), sender));
                    } else if (sender.hasPermission("chatfilter.clear")) {
                        for (Player nopermplayer: Bukkit.getServer().getOnlinePlayers()) {
                            if (!nopermplayer.hasPermission("chatfilter.bypass"))
                                for (int i = 0; i < 100; i++) {
                                    nopermplayer.sendMessage(" ");
                                }
                        }
                        for (Player permplayer: Bukkit.getServer().getOnlinePlayers()) {
                            if (permplayer.hasPermission("chatfilter.bypass") || permplayer.hasPermission("chatfilter.view"))
                                permplayer.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.clearChatMessage.s).replace("%player%", sender.getName())));
                            return true;
                        }
                    }
                }
                if (args[0].equals("pause")) {
                    if (!sender.hasPermission("chatfilter.pause")) {
                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.NO_PERMISSION.s)));
                        return true;
                    }
                    if (sender.hasPermission("chatfilter.pause")) {
                        if (!chatFilter.chatPause) {

                            for (Player permplayer: Bukkit.getServer().getOnlinePlayers()) {
                                if (permplayer.hasPermission("chatfilter.bypass") || (permplayer.hasPermission("chatfilter.pause")) || permplayer.hasPermission("chatfilter.view"))
                                    permplayer.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.PAUSE_CHAT.s).replace("%player%", sender.getName())));
                            }
                            chatFilter.chatPause = true;
                        } else {
                            for (Player permplayer: Bukkit.getServer().getOnlinePlayers()) {
                                if (permplayer.hasPermission("chatfilter.bypass") || (permplayer.hasPermission("chatfilter.pause")) || permplayer.hasPermission("chatfilter.view"))
                                    permplayer.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.UNPAUSE_CHAT.s).replace("%player%", sender.getName())));
                            }
                            chatFilter.chatPause = false;
                        }
                    }
                } else if (args[0].equals("test")) {
                    if (!sender.hasPermission("chatfilter.test")) {
                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.NO_PERMISSION.s)));
                        return true;
                    }

                    if (args.length == 1) {
                        if (sender.hasPermission("chatfilter.test")) {
                            sender.sendMessage(chatFilter.colour("&test"));
                            return true;
                        }


                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ARGS.s)));
                        return true;
                    }
                    if (args.length == 2) {
                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ARGS_LIST.s)));
                        return true;
                    }
                }


                // cf blacklist
                else if (args[0].equals("blacklist")) {
                    if (!sender.hasPermission("chatfilter.blacklist")) {
                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.NO_PERMISSION.s)));
                        return true;
                    }

                    if (args.length == 1) {
                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ARGS.s)));
                        return true;
                    }

                    if (args[1].equals("list")) {
                        if (args.length == 2) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ARGS_LIST.s)));
                            return true;

                        }
                        if (args[2].equals("ip")) {

                            List < String > strlist = new ArrayList < > (chatFilter.getConfig().getStringList("filteredIPandDNS"));
                            ComponentBuilder message = new ComponentBuilder("");
                            for (String words: strlist) {
                                message.append(ChatColor.WHITE + " " + words + ", ");
                                message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cf blacklist remove ip " + words));
                                message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_LIST_IP_1.s).replace("%ip%", words)))));
                            }
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_LIST_IP_2.s)));
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_LIST_IP_3.s)));
                            sender.spigot().sendMessage(message.create());
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_LIST_IP_4.s)));
                            return true;
                        }
                        if (args[2].equals("word")) {
                            List < String > strlist = new ArrayList < > ();
                            for (String stringlist: chatFilter.regExWords) {
                                String listin = stringlist.replace(")\\b", "#").replace("\\b(", "#");
                                String stripRegex = listin.replace("\\", "").replace("(W|d|_)*", "").replace("+", "").replace("(", "").replace(")", "");
                                strlist.add(stripRegex);
                            }
                            Collections.sort(strlist);
                            ComponentBuilder message = new ComponentBuilder("");
                            for (String words: strlist) {
                                message.append(ChatColor.WHITE + " " + words + ", ");
                                message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cf blacklist remove word " + words));
                                message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_LIST_WORD_1.s).replace("%word%", words)))));
                            }
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_LIST_WORD_2.s)));
                            sender.spigot().sendMessage(message.create());
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_LIST_WORD_3.s)));
                            return true;
                        } else {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ARGS_LIST.s)));
                        }
                        return true;
                    }
                    if (args[1].equals("remove")) {
                        if (!sender.hasPermission("chatfilter.blacklist.remove")) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.NO_PERMISSION.s)));
                            return true;
                        }
                        if (args.length == 2) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_REMOVE_ARG.s)));
                            return true;
                        }
                        if (args[2].equals("word") && args.length == 3) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_REMOVE_WORD_ARG.s)));
                            return true;
                        }
                        if (args[2].equals("ip") && args.length == 3) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_REMOVE_IP_ARG.s)));
                            return true;
                        }
                        if (args[2].equals("word") && args.length > 3) {
                            String ArgsString = String.join(" ", args).toLowerCase().replaceAll("blacklist remove word ", "");
                            List < String > alist = new ArrayList < > ();
                            for (int i = 0; i < ArgsString.length(); i++) {
                                char c = ArgsString.charAt(i);
                                String chars = (new StringBuilder(String.valueOf(c))).toString();
                                if (chars.equals(" ")) {
                                    String str = "%SPACE%+(\\W|\\d|_)*";
                                    alist.add(str);
                                }
                                if (chars.equals("#")) {
                                    String str = "#";
                                    alist.add(str);
                                } else {
                                    String str = c + "+(\\W|\\d|_)*";
                                    alist.add(str);
                                }
                            }
                            String regexString = alist.toString().replaceAll("\\[|\\]|,|\\s", "").replaceAll("%SPACE%", " ");
                            String regexToFilter = regexString;
                            if (!regexToFilter.contains("#")) {
                                regexToFilter = "(" + regexString + ")";
                            } else if (regexToFilter.startsWith("#") && regexString.endsWith("#")) {
                                regexToFilter = regexToFilter.replace("#", "");
                                regexToFilter = "\\b(" + regexToFilter + ")\\b";
                            } else if (regexToFilter.startsWith("#")) {
                                regexToFilter = regexToFilter.replace("#", "\\b(");
                                regexToFilter = regexToFilter + ")";
                            } else if (regexString.endsWith("#")) {
                                regexToFilter = regexToFilter.replace("#", ")\\b");
                                regexToFilter = "(" + regexToFilter;
                            }
                            List < String > list = chatFilter.getConfig().getStringList("filteredWords");
                            if (!list.contains(regexToFilter)) {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_REMOVE_WORD_NO.s).replace("%word%", ArgsString)));
                            } else {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_REMOVE_WORD_REMOVED.s).replace("%word%", ArgsString)));
                                chatFilter.regExWords.remove(regexToFilter);
                                list.remove(regexToFilter);
                                chatFilter.getConfig().set("filteredWords", list);
                                chatFilter.saveConfig();
                                return true;
                            }
                        }
                        if (args[2].equals("ip") && args.length > 3) {
                            List < String > list = chatFilter.getConfig().getStringList("filteredIPandDNS");
                            if (!list.contains(args[3])) {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_REMOVE_IP_NO.s).replace("%ip%", args[3])));
                                return true;
                            } else { // CMD_BLACKLIST_REMOVE_IP_REMOVED
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_REMOVE_IP_REMOVED.s).replace("%ip%", args[3])));
                                chatFilter.regExWords.remove(args[3].toLowerCase());
                                list.remove(args[3].toLowerCase());
                                chatFilter.getConfig().set("filteredIPandDNS", list);
                                chatFilter.saveConfig();
                                return true;
                            }
                        }
                    }
                    if (args[1].equals("add")) {
                        if (args.length == 2) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ADD_ARG.s)));

                            return true;
                        }
                        if (args[2].equals("word") && args.length == 3) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ADD_WORD_ARG.s)));

                            return true;
                        }
                        if (args[2].equals("ip") && args.length == 3) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ADD_IP_ARG.s)));

                            return true;
                        } else if (args[2].equals("word") && args.length > 3) {
                            String ArgsString = String.join(" ", args).toLowerCase().replace("blacklist add word ", "");
                            List < String > reglist = new ArrayList < > ();
                            for (int i = 0; i < ArgsString.length(); i++) {
                                char c = ArgsString.charAt(i);
                                String chars = String.valueOf(c);
                                if (chars.equals(" ")) {
                                    String reg = "%SPACE%+(\\W|\\d|_)*";
                                    reglist.add(reg);
                                }
                                if (chars.equals("#")) {
                                    String reg = "#";
                                    reglist.add(reg);
                                } else {
                                    String reg = c + "+(\\W|\\d|_)*";
                                    reglist.add(reg);
                                }
                            }
                            String regexString = reglist.toString().replaceAll("\\[|\\]|,|\\s", "").replaceAll("%SPACE%", " ");
                            String regexToFilter = regexString;
                            if (!regexToFilter.contains("#")) {
                                regexToFilter = "(" + regexString + ")";
                            } else if (regexToFilter.startsWith("#") && regexString.endsWith("#")) {
                                regexToFilter = regexToFilter.replace("#", "");
                                regexToFilter = "\\b(" + regexToFilter + ")\\b";
                            } else if (regexToFilter.startsWith("#")) {
                                regexToFilter = regexToFilter.replace("#", "\\b(");
                                regexToFilter = regexToFilter + ")";
                            } else if (regexString.endsWith("#")) {
                                regexToFilter = regexToFilter.replace("#", ")\\b");
                                regexToFilter = "(" + regexToFilter;
                            }
                            List < String > list = chatFilter.getConfig().getStringList("filteredWords");
                            if (list.contains(regexToFilter)) {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ADD_WORD_NO.s).replace("%word%", ArgsString)));

                            } else {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ADD_WORD_ADDED.s).replace("%word%", ArgsString)));
                                chatFilter.regExWords.add(regexToFilter);
                                list.add(regexToFilter);
                                chatFilter.getConfig().set("filteredWords", list);
                                chatFilter.saveConfig();
                                return true;
                            }
                        } else if (args[2].equals("ip") && args.length > 3) {
                            String ArgsString = String.join(" ", args).toLowerCase().replaceAll("blacklist add ip ", "");
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ADD_IP_ADDED.s).replace("%ip%", ArgsString)));
                            chatFilter.regExDNS.add(ArgsString);
                            List < String > list = chatFilter.getConfig().getStringList("filteredIPandDNS");
                            list.add(ArgsString);
                            chatFilter.getConfig().set("filteredIPandDNS", list);
                            chatFilter.saveConfig();
                            return true;
                        }
                    } else {
                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_BLACKLIST_ARGS.s)));
                        return true;
                    }
                }
                // cf whitelist
                else if (args[0].equals("whitelist")) {
                    if (!sender.hasPermission("chatfilter.whitelist")) {
                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.NO_PERMISSION.s)));
                        return true;
                    }
                    if (args.length == 1) {
                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ARGS.s)));
                        return true;
                    }
                    if (args[1].equals("list")) {
                        if (args.length == 2) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ARGS_LIST.s)));
                            return true;
                        }
                        if (args[2].equals("word")) {
                            Collections.sort(chatFilter.byPassWords);
                            ComponentBuilder message = new ComponentBuilder("");
                            for (String words: chatFilter.byPassWords) {
                                message.append(ChatColor.WHITE + " " + words + ", ");
                                message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cf whitelist remove word " + words));
                                message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_LIST_WORD_1.s).replace("%word%", words)))));

                            }
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_LIST_WORD_2.s)));
                            sender.spigot().sendMessage(message.create());
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_LIST_WORD_3.s)));
                            return true;
                        }
                        if (args[2].equals("ip")) {
                            Collections.sort(chatFilter.byPassDNS);
                            ComponentBuilder message = new ComponentBuilder("");
                            for (String words: chatFilter.byPassDNS) {
                                message.append(ChatColor.WHITE + " " + words + ", ");
                                message.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cf whitelist remove ip " + words));
                                message.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_LIST_WORD_1.s).replace("%ip%", words)))));

                            }

                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_LIST_IP_2.s)));
                            sender.spigot().sendMessage(message.create());
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_LIST_IP_3.s)));
                            return true;
                        }
                    }
                    if (args[1].equals("add")) {
                        if (args.length == 2) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ADD_ARG.s)));
                            return true;
                        }
                        if (args[2].equals("word") && args.length == 3) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ADD_WORD_ARG.s)));
                            return true;
                        }
                        if (args[2].equals("ip") && args.length == 3) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ADD_IP_ARG.s)));
                            return true;
                        }
                        if (args[2].equals("ip") && args.length > 3) {
                            String ArgsString = String.join(" ", args).toLowerCase().replaceAll("whitelist add ip ", "");
                            List < String > list = chatFilter.getConfig().getStringList("bypassIP");
                            if (list.contains(ArgsString)) {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ADD_IP_NO.s).replace("%ip%", ArgsString)));
                                return true;
                            } else {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ADD_IP_ADDED.s).replace("%ip%", ArgsString)));
                                chatFilter.byPassDNS.add(ArgsString);
                                list.add(ArgsString);
                                chatFilter.getConfig().set("bypassIP", list);
                                chatFilter.saveConfig();
                                return true;

                            }
                        }
                        if (args[2].equals("word") && args.length > 3) {
                            String ArgsString = String.join(" ", args).toLowerCase().replaceAll("whitelist add word ", "");
                            List < String > list = chatFilter.getConfig().getStringList("bypassWords");
                            if (list.contains(ArgsString)) {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ADD_WORD_NO.s).replace("%word%", ArgsString)));
                                return true;

                            } else {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ADD_WORD_ADDED.s).replace("%word%", ArgsString)));
                                chatFilter.byPassWords.add(ArgsString);
                                list.add(ArgsString);
                                chatFilter.getConfig().set("bypassWords", list);
                                chatFilter.saveConfig();
                                return true;
                            }
                        }
                    }
                    if (args[1].equals("remove")) {
                        if (args.length == 2) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_REMOVE_ARG.s)));
                            return true;
                        }
                        if (args[2].equals("word") && args.length == 3) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_REMOVE_WORD_ARG.s)));
                            return true;
                        }
                        if (args[2].equals("ip") && args.length == 3) {
                            sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_REMOVE_IP_ARG.s)));
                            return true;
                        }
                        if (args[2].equals("word") && args.length > 3) {
                            if (!sender.hasPermission("chatfilter.whitelist.remove")) {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.NO_PERMISSION.s)));
                                return true;
                            }
                            String ArgsString = String.join(" ", args).toLowerCase().replaceAll("whitelist remove word ", "");
                            List < String > list = chatFilter.getConfig().getStringList("bypassWords");
                            if (!list.contains(ArgsString)) {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_REMOVE_WORD_NO.s).replace("%word%", ArgsString)));
                            } else {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_REMOVE_WORD_REMOVED.s).replace("%word%", ArgsString)));
                                chatFilter.byPassWords.remove(ArgsString);
                                list.remove(ArgsString);
                                chatFilter.getConfig().set("bypassWords", list);
                                chatFilter.saveConfig();
                                return true;
                            }
                        }
                        if (args[2].equals("ip") && args.length > 3) {
                            List < String > list = chatFilter.getConfig().getStringList("bypassIP");
                            if (!list.contains(args[3])) {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_REMOVE_IP_NO.s).replace("%ip%", args[3])));
                                return true;

                            } else {
                                sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_REMOVE_IP_REMOVED.s).replace("%ip%", args[3])));
                                chatFilter.byPassDNS.remove(args[3].toLowerCase());
                                list.remove(args[3].toLowerCase());
                                chatFilter.getConfig().set("bypassIP", list);
                                chatFilter.saveConfig();
                                return true;
                            }
                        }
                    } else {
                        sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.CMD_WHITELIST_ARGS.s)));
                        return true;
                    }
                } else {
                    sender.sendMessage(chatFilter.colour(chatFilter.mapToString(enumStrings.NO_ARGS.s)));
                }
            }
        }
        return false;
    }
}
