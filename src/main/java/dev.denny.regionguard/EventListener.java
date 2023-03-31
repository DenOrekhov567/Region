package dev.denny.regionguard;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import dev.denny.account.player.Gamer;
import dev.denny.regionguard.animation.Animation;
import dev.denny.regionguard.utils.ResponseRegion;
import ru.nukkitx.forms.elements.CustomForm;

import java.util.ArrayList;
import java.util.List;

public class EventListener implements Listener {

    private List<String> listPlayers = new ArrayList<>();
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ResponseRegion region = RegionGuardPlugin.getInstance().getManager().getRegion(player.getPosition());

        if (region != null) {
            if(!region.getOwnerName().equals(player.getName().toLowerCase()) || !RegionGuardPlugin.getInstance().getManager().isMember((Gamer) player, region.getName())) {
                Animation.add(block.getLocation());
                event.setCancelled();

                player.sendPopup("§cТут есть регион");
            } else {
                if(block.getId() == Block.IRON_BLOCK || block.getId() == Block.GOLD_BLOCK || block.getId() == Block.DIAMOND_BLOCK) {
                    Animation.add(block.getLocation());
                    event.setCancelled();

                    player.sendPopup("§cТут есть регион");
                }
            }
        } else {
            if (block.getId() == Block.IRON_BLOCK || block.getId() == Block.GOLD_BLOCK || block.getId() == Block.DIAMOND_BLOCK) {
                if (!listPlayers.contains(player.getName())) {
                    listPlayers.add(player.getName());
                    if (RegionGuardPlugin.getInstance().getManager().isCanCreate((Gamer) player)) {
                        if (!RegionGuardPlugin.getInstance().getManager().isNewRegionIncludeOther(block)) {
                            CustomForm form = new CustomForm("Регионы")
                                    .addLabel("Меню создания своего региона")
                                    .addInput("Введи название региона");

                            form.send(player, (targetPlayer, targetForm, data) -> {
                                if (data == null) {
                                    targetPlayer.sendMessage("> Вы просто закрыли форму!");

                                    Animation.add(block.getLocation());
                                    player.getLevel().setBlock(block.getLocation(), Block.get(0));
                                    player.getLevel().dropItem(block.getLocation(), Item.get(block.getId()));
                                    listPlayers.remove(player.getName());
                                } else {
                                    if (data.get(1) != "") {
                                        String regionName = (String) data.get(1);

                                        if (RegionGuardPlugin.getInstance().getManager().isValidName(regionName)) {
                                            if (!RegionGuardPlugin.getInstance().getManager().isRegionNameExists(regionName)) {
                                                RegionGuardPlugin.getInstance().getManager().createRegion((Gamer) targetPlayer, regionName, block);
                                                targetPlayer.sendTitle("Создание региона...", "успешно");

                                                listPlayers.remove(player.getName());
                                            } else {
                                                targetPlayer.sendMessage("> Регион с именем" + regionName + " уже существует!");

                                                Animation.add(block.getLocation());
                                                player.getLevel().setBlock(block.getLocation(), Block.get(0));
                                                player.getLevel().dropItem(block.getLocation(), Item.get(block.getId()));
                                                listPlayers.remove(player.getName());
                                            }
                                        } else {
                                            targetPlayer.sendMessage("> В названии региона могут присутствовать только буквы, цифры и нижние подчеркивания!");

                                            Animation.add(block.getLocation());
                                            player.getLevel().setBlock(block.getLocation(), Block.get(0));
                                            player.getLevel().dropItem(block.getLocation(), Item.get(block.getId()));
                                            listPlayers.remove(player.getName());
                                        }
                                    } else {
                                        targetPlayer.sendMessage("> Название региона не может быть пустым!");

                                        Animation.add(block.getLocation());
                                        player.getLevel().setBlock(block.getLocation(), Block.get(0));
                                        player.getLevel().dropItem(block.getLocation(), Item.get(block.getId()));
                                        listPlayers.remove(player.getName());
                                    }
                                }
                            });
                        } else {
                            player.sendMessage("> Границы региона пересекают границы другого региона!");

                            Animation.add(block.getLocation());
                            event.setCancelled();
                            listPlayers.remove(player.getName());
                        }
                    } else {
                        player.sendMessage("> У тебя уже максимальное количество приватов - 2!");

                        Animation.add(block.getLocation());
                        event.setCancelled();
                        listPlayers.remove(player.getName());
                    }
                } else {
                    player.sendMessage("> 123");

                    Animation.add(block.getLocation());
                    event.setCancelled();
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        ResponseRegion region = RegionGuardPlugin.getInstance().getManager().getRegion(player.getPosition());
        if (region != null) {
            if(!region.getOwnerName().equals(player.getName().toLowerCase())) {
                Animation.add(block.getLocation());
                event.setCancelled();

                player.sendPopup("§cЗапривачено");
            } else {
                if(block.getId() == Block.IRON_BLOCK || block.getId() == Block.GOLD_BLOCK || block.getId() == Block.DIAMOND_BLOCK) {
                    RegionGuardPlugin.getInstance().getManager().deleteRegion((Gamer) player, region.getName());

                    player.sendMessage("> Вы удалили регион " + region.getName() + "!");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        ResponseRegion region = RegionGuardPlugin.getInstance().getManager().getRegion(player.getPosition());

        if (region != null) {
            if(!region.getOwnerName().equals(player.getName().toLowerCase())) {
                Animation.add(block.getLocation());
                event.setCancelled();

                player.sendPopup("§cЗапривачено");
            }
        }
    }

    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if(entity instanceof Player) {
            Player player = (Player) entity;

            ResponseRegion region = RegionGuardPlugin.getInstance().getManager().getRegion(player.getPosition());
            if (region != null) {
                if(!region.getOwnerName().equals(player.getName().toLowerCase())) {
                    event.setCancelled();

                    player.sendPopup("§cЗапривачено");
                }
            }
        }
    }
}