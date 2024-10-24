# ZombiesInfection

The plugin adds an infection to your server, if a player is attacked by a zombie, then after a while the player gets the effect of poisoning.

**Spigot/Paper 1.12.2 - 1.21.3**

## Commands
- `/zombiesinfection reload` - Reload the plugin

## Resolution
- `zombiesinfection.reload` - Gives the player the right to reload your plugin. (default: op)
- `zombiesinfection.effect` - If you want the player not to receive any effects, then remove this permission from him. (default: true)

## CONFIG

- `n * 20` - means that the entered number will be multiplied by 20.

#### Example
*(the example is taken from **ZIConfig.java:64** line)*
```java
return number * 20;
```

- `debug` - Enables debugging mode, it outputs additional information to the console, which may be useless
- `if-player-infected' - If the player is infected
  - `message` - Enable a message every time a player is hit and he is infected
  - `play-sound` - Turn on the sound playback every time a player is beaten, and at the same time he is infected
  - `update-timer` - Enable timer update every time
- `effect-will-applied-after` - after what time the effect will be given (the higher the value, the longer it will be stored in **HashMap**)
- `delay` and `period` - read [HERE](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/scheduler/BukkitScheduler.html#runTaskTimer(org.bukkit.plugin.Plugin,java.lang.Runnable,long,long))
- `random_time_from_range` is a sector for working with the number selection area
 - `enable` - enable this selector *(if it is disabled, the time will be counted from `from_effect`)*
 - `range_from` - the minimum value for the time
 - `range_to` - the maximum value for the time
- `potion-gived` - These effects will be given to the player when his infection is over
 - `POISON` - ENUM is an enumeration of the effect, you can find it [HERE](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html)
   - `potion-duration` - Duration of the effect (n * 20)
 - `BLINDNESS` - ENUM is an enumeration of the effect, you can find it [HERE](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html)
   - `potion-duration` - Duration of the effect (n * 20)
 - `HUNGER` - ENUM is an enumeration of the effect, you can find it [HERE](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html)
   - `potion-duration` - Duration of the effect (n * 20)
- `sound-played` - The sector with sounds
  - `sound-in-attack` - This sound will be played while the zombie is hitting you.
    - `enable` - Enable this sector
    - `sound` - ENUM is an enumeration of the effect, you can find it [HERE](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html)
    - `volume` - The volume of the sound
    - `pitch` - The pitch of the sound
  - `sound-in-effect-gived` - This sound will be played while the effects are being issued.
    - `enable` - Enable this sector
    - `sound` - ENUM is an enumeration of the effect, you can find it [HERE](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html)
    - `volume` - The volume of the sound
    - `pitch` - The pitch of the sound
- `messages` is a sector with messages from the plugin (not a *debugging* message).
 - `plugin_reload` is a message when restarting the plugin
 - `no_permission` is a message when the player does not have rights to participate in the team.
 - `no_command_args` is a message when a player enters a command without a nested command
 - `no_found_command` is a message when the nested command is not found
 - `you_infected` is a message when the player is infected and the timer is running
 - `effect-gived` is a message that is displayed when you get the effects

### Example settings
```yaml
config_version: 2

# Enable debug mode (output additional messages to the console)
debug: false

# Settings for messages and sounds when a player is infected
if-player-infected:
  # Enable displaying the infection message
  message: false
  # Enable playing the sound when infected
  play-sound: false
  # Update the timer when infected
  update-timer: true

# Number of times the effect will be applied
effect-will-applied-after: 5

# Delay in server ticks before executing the first repeat (n * 20)
delay: 5

# Period in server ticks for task repetition (n * 20)
period: 5

# Enable a random time range for effect application
random_time_from_range:
  # Enable a random time range
  enable: true
  # Lower limit of the time range in seconds
  range_from: 5
  # Upper limit of the time range in seconds
  range_to: 10

# Settings for potions that will be given
potion-gived:
  # Settings for poison potion
  POISON:
    # Potion duration in seconds (n * 20 ticks)
    potion-duration: 60
  # Settings for blindness potion
  BLINDNESS:
    # Potion duration in seconds (n * 20 ticks)
    potion-duration: 5
  # Settings for hunger potion
  HUNGER:
    # Potion duration in seconds (n * 20 ticks)
    potion-duration: 30

# Settings for sounds that will be played
sound-played:
  # Enable the sound when attacked
  sound-in-attack:
    # Enable the attack sound
    enable: true
    # Sound name from Minecraft resources
    sound: "AMBIENT_CAVE"
    # Sound volume (from 0 to 100)
    volume: 100.0
    # Sound pitch (from 0 to 2)
    pitch: 1.0
  # Enable the sound when the effect is given
  sound-in-effect-gived:
    # Enable the effect sound
    enable: true
    # Sound name from Minecraft resources
    sound: "ENTITY_ENDERMEN_DEATH"
    # Sound volume (from 0 to 100)
    volume: 100.0
    # Sound pitch (from 0 to 2)
    pitch: 1.0

# Messages for different events
messages:
  # Plugin reload success message
  plugin_reload: "§2Plugin successfully reloaded"
  # Insufficient permissions message
  no_permission: "§cYou do not have permission"
  # Missing subcommand message
  no_command_args: "§cUsage: /%s <subcommand> <args>"
  # Invalid command message
  no_found_command: "§cThe command was not found"
  # Infection message
  you_infected: "§4§lYOU ARE INFECTED!!"
  # Effect applied message
  effect-gived: "§c§lTHE INFECTION IS SPREADING"
```

## Known issues
- Optimization (an eternal problem)
- Level potion effect not work

## Zombie support
- An ordinary zombie
- Zombie resident
