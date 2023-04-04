package dev.denny.region;

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
import cn.nukkit.level.Level;
import dev.denny.account.player.Gamer;
import dev.denny.region.animation.Animation;
import dev.denny.region.manager.RegionManager;
import dev.denny.region.utils.Region;
import ru.nukkitx.forms.elements.CustomForm;

import java.util.ArrayList;
import java.util.List;

public class EventListener implements Listener {

    private List<String> listPlayers = new ArrayList<>();

    private void cancelAction(Gamer player, Block block) {
        Animation.add(block.getLocation());
        Level level = player.getLevel();
        level.setBlock(block.getLocation(), Block.get(0));
        level.dropItem(block.getLocation(), Item.get(block.getId()));
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Gamer player = (Gamer) event.getPlayer();
        Block block = event.getBlock();

        RegionManager manager = RegionPlugin.getManager();
        Region region = manager.getRegion(block.getLocation());

        player.getServer().getLogger().emergency("Place1");

        //Если на этой позиции есть регион
        if (region != null) {
            //Если это приватный блок, то
            if (block.getId() == Block.IRON_BLOCK || block.getId() == Block.GOLD_BLOCK || block.getId() == Block.DIAMOND_BLOCK) {
                event.setCancelled();
                Animation.add(block.getLocation());
                player.sendPopup("§cзапрещено");

                player.getServer().getLogger().emergency("Place2");

                return;
            }

            //Если игрок не является владельцем или участником региона
            if (!region.isOwner(player) && !region.isMember(player)) {
                event.setCancelled();
                Animation.add(block.getLocation());
                player.sendPopup("§cзапрещено");

                player.getServer().getLogger().emergency("Place3");

                return;
            }
            return;
        }

        player.getServer().getLogger().emergency("Place4");

        //Дальше блок, если на этой позиции нет региона

        //Если это приватный блок
        if (block.getId() == Block.IRON_BLOCK || block.getId() == Block.GOLD_BLOCK || block.getId() == Block.DIAMOND_BLOCK) {
            //Если игрок находится в массив людей, что уже приватят регион
            if (listPlayers.contains(player.getName())) {
                //Нельзя p. s. чинит баг с тем, что можно поставить сразу несколько приватных блоков
                return;
            }

            //Добавляем игрока в лист, что приватят сейчас регион
            listPlayers.add(player.getName());

            //Есть ли у игрока права на создание региона
            if (!manager.isCanCreate(player)) {
                event.setCancelled();
                Animation.add(block.getLocation());
                player.sendMessage("§7> §aТы §fпревысил максимальное количество приватов");

                return;
            }

            //Пересекает ли новый регион границы других регионов
            if (manager.isNewRegionIncludeOther(block)) {
                event.setCancelled();
                Animation.add(block.getLocation());
                player.sendMessage("§7> §fГраницы §aнового региона §fпересекают границы §aдругого");

                return;
            }

            //Отправляем игроку форму
            CustomForm form = new CustomForm("Регионы")
                    .addLabel(
                            "§aМеню создания региона\n" +
                            "§7> §fОстался §aпоследний шаг§f...\n" +
                            "§7> §fВведи §aназвание региона §fв поле\n" +
                            "§7> §aТребования§f:\n" +
                            "§7- §fдлина до §a8 символов§f,\n" +
                            "§7- §fтолько §aбуквы§f, §aцифры §fи §aнижние подчеркивания\n"
                    )
                    .addInput("Введи название региона");

            form.send(player, (targetPlayer, targetForm, data) -> {
                Gamer gtargetPlayer = (Gamer) targetPlayer;
                //Игрок закрыл форму
                if (data == null) {
                    cancelAction(gtargetPlayer, block);
                    player.sendMessage("§7> §fСоздание §aнового региона §fотменено");

                    return;
                }

                //Если поле названия региона пустое, то
                if (data.get(1) == "") {
                    cancelAction(gtargetPlayer, block);
                    player.sendMessage("§7> §fВ названии §aнового региона §fне может быть пусто");

                    return;
                }

                //Инициализируем названия региона из поля формы
                String regionName = (String) data.get(1);

                //Если имя содержит что-то кроме букв, цифр и нижних подчеркиваний
                if (!manager.isValidName(regionName)) {
                    cancelAction(gtargetPlayer, block);
                    player.sendMessage("§7> §fВ названии §aнового региона §fне может быть что-то кроме §aбукв§f, §aцифр§f, §aпробелов");

                    return;
                }

                //Если название региона больше 8 символов
                if (regionName.length() > 8) {
                    cancelAction(gtargetPlayer, block);
                    player.sendMessage("§7> §fВ названии §aнового региона §fне может быть больше, чем 8 символов");

                    return;
                }

                //Если регион с таким именем уже существует
                if (manager.isRegionNameExists(regionName)) {
                    cancelAction(gtargetPlayer, block);
                    player.sendMessage("§7> §fРегион §a" + region + " §fуже существует");

                    return;
                }

                //Создаем новый регион
                manager.createRegion((Gamer) targetPlayer, regionName, block);
                targetPlayer.sendMessage("§7> §fРегион §a" + region + " §fбыл создан");

                //Удаляем игрока из списка тех, что приватят в данное время
                listPlayers.remove(player.getName());
            });
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Gamer player = (Gamer) event.getPlayer();
        Block block = event.getBlock();

        player.getServer().getLogger().emergency("Break1");

        Region region = RegionPlugin.getManager().getRegion(block.getLocation());
        if (region != null) {
            if (!region.isOwner(player) && !region.isMember(player)) {
                Animation.add(block.getLocation());
                event.setCancelled();

                player.sendPopup("§cзапрещено");

                player.getServer().getLogger().emergency("Break2");

                return;
            }

            if (block.getId() == Block.IRON_BLOCK || block.getId() == Block.GOLD_BLOCK || block.getId() == Block.DIAMOND_BLOCK) {
                if (region.isOwner(player)) {
                    RegionPlugin.getManager().deleteRegion(player, region.getName());

                    player.sendMessage("§7> §aТы §fудалил регион §a" + region.getName());

                    player.getServer().getLogger().emergency("Break3");

                    return;
                }

                event.setCancelled();
                Animation.add(block.getLocation());

                player.sendMessage("§7> §aТы §fне можешь регион удалить§a" + region.getName() + " §fтак как ты не его владелец");
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Gamer player = (Gamer) event.getPlayer();
        Block block = event.getBlock();

        Region region = RegionPlugin.getManager().getRegion(block.getLocation());

        if (region != null) {
            if (!region.isOwner(player) && !region.isMember(player)) {
                Animation.add(block.getLocation());
                event.setCancelled();

                player.sendMessage("Interact");

                player.sendPopup("§cзапрещено");
            }
        }
    }

    public void onPlayerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Gamer player = (Gamer) entity;

            Region region = RegionPlugin.getManager().getRegion(player.getPosition());
            if (region != null) {
                event.setCancelled();

                player.sendPopup("§cзапрещено");
            }
        }
    }
}