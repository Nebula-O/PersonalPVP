# PersonalPVP

A Minecraft plugin that provides a PVP option for each player. PersonalPVP provides a many useful options for this
that can be viewed in [config.yml](/src/main/resources/config.yml).
<br>When a player has PVP disabled, they cannot attack or be attacked by other players despite their PVP statuses.
<br>The PVP status of each player can be locked by admins with the `personalpvp.lock` permission by default.
<br>Locking a player's status prevents the player from successfully toggling their PVP status while locked.

[<img src="https://raw.githubusercontent.com/Nebula-O/images/main/banner.png?token=ARDWZIWPNH6WYPEZXVX7LA3AC7X64">](https://discord.gg/JjWDdNdK5F)

### Useful links:
- [Commands and Permissions](https://github.com/Nebula-O/PersonalPVP/wiki/Commands-and-Permissions)
- [Discord Server](https://discord.gg/JjWDdNdK5F)
- [Descriptive config.yml](/src/main/resources/config.yml)
- [Spigot page](https://www.spigotmc.org/resources/personalpvp.88468/)
## Projectiles and potion throwing
When one player (or both) has PVP enabled and they try to attack a player who has PVP disabled (or vice-versa), no damage is dealt.
If they threw a projectile (by shooting an arrow from a bow or throwing a trident at the player) or a splash potion, the projectile will be
returned to the "shooter's" inventory if one of the players had PVP disabled.
For this to occur, these [config](/src/main/resources/config.yml) options should be true respectively.
>prevent:<br>
>&nbsp;&nbsp;throwable-projectiles: true<br>
>&nbsp;&nbsp;potions: true

<br>Fishing rod interactions are also intercepted.
