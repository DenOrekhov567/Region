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
        listPlayers.remove(player.getName());

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

        //Если на этой позиции есть регион
        if (region != null) {
            //Если это приватный блок, то
            if (manager.getConfigManager().getRadius(block) != null) {
                event.setCancelled();

                Animation.add(block.getLocation());
                player.sendPopup("§cзапрещено");

                return;
            }

            //Если игрок не является владельцем или участником региона
            if (!region.isOwner(player) && !region.isMember(player)) {
                event.setCancelled();

                Animation.add(block.getLocation());
                player.sendPopup("§cзапрещено");

                return;
            }
            return;
        }

        //Если это приватный блок
        if (manager.getConfigManager().getRadius(block) != null) {
            //Если игрок находится в массив людей, что уже приватят регион
            if (listPlayers.contains(player.getName())) {
                event.setCancelled();

                Animation.add(block.getLocation());

                //Нельзя p. s. чинит баг с тем, что можно поставить сразу несколько приватных блоков
                return;
            }

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

            //Добавляем игрока в лист, что приватят сейчас регион
            listPlayers.add(player.getName());

            //Отправляем игроку форму
            CustomForm form = new CustomForm("§7Регионы")
                    .addLabel(
                            "§7> §aТребования к §aназванию§f:\n" +
                            "§7• §fДлина до §a8 символов§f,\n" +
                            "§7• §fЗапрещены §a^[a-zA-Z0-9_]+$ §f символы\n"
                    )
                    .addInput("§7> §fВведи §aназвание региона");

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

                    player.sendMessage("§7> §fРегион §a" + regionName + " §fуже существует");

                    return;
                }

                //Создаем новый регион
                manager.createRegion((Gamer) targetPlayer, regionName, block);
                targetPlayer.sendMessage("§7> §fРегион §a" + regionName+ " §fбыл создан");

                //Удаляем игрока из списка тех, что приватят в данное время
                listPlayers.remove(player.getName());
            });
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Gamer player = (Gamer) event.getPlayer();
        Block block = event.getBlock();

        RegionManager manager = RegionPlugin.getManager();
        Region region = manager.getRegion(block.getLocation());
        if (region != null) {
            if (!region.isOwner(player) && !region.isMember(player)) {
                Animation.add(block.getLocation());
                event.setCancelled();

                player.sendPopup("§cзапрещено");

                return;
            }

            if (manager.getConfigManager().getRadius(block) != null) {
                if (!region.isOwner(player)) {
                    Animation.add(block.getLocation());
                    event.setCancelled();

                    player.sendMessage("§7> §aТы §fне можешь регион удалить §a" + region.getName() + " §fтак как ты не его владелец");

                    return;
                }

                manager.deleteRegion(region.getName());

                player.sendMessage("§7> §aТы §fудалил регион §a" + region.getName());
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