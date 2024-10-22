# ZombiesInfection

The plugin adds an infection to your server, if a player is attacked by a zombie, then after a while the player gets the effect of poisoning.

**Spigot/Paper 1.12.2 - 1.21.1**

## Commands
- `/zombiesinfection reload` - Reload the plugin

## Resolution
- `zombiesinfection.reload` - Gives the player the right to reload your plugin. (default: op)
- `zombiesinfection.effect` - If you want the player not to receive any effects, then remove this permission from him. (default: true)

## CONFIG

- `n * 20` - means that the entered number will be multiplied by 20.

#### Example
*(the example is taken from **PlayerInfectionHandler.java:52, 53, 54** line)*
```java
this.potionDuration = this.config.getInt("potion_duration") * 20;
this.delay = this.config.getInt("delay") * 20;
this.period = this.config.getInt("period") * 20;
```

- `debug` - Enables debugging mode, it outputs additional information to the console, which may be useless
- `potion_duration` - The duration of the potion that is given
- `potion_level` - The potion level that is issued
- `from_effect` - after what time the effect will be given (the higher the value, the longer it will be stored in **HashMap**)
- `delay` and `period` - read [HERE](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/scheduler/BukkitScheduler.html#runTaskTimer(org.bukkit.plugin.Plugin,java.lang.Runnable,long,long))
- `random_time_from_range` is a sector for working with the number selection area
- - `enable` - enable this selector *(if it is disabled, the time will be counted from `from_effect`)*
- - `range_from` - the minimum value for the time
- - `range_to` - the maximum value for the time
- `messages` is a sector with messages from the plugin (not a *debugging* message).
- - `plugin_reload` is a message when restarting the plugin
- - `no_permission` is a message when the player does not have rights to participate in the team.
- - `no_command_args` is a message when a player enters a command without a nested command
- - `no_found_command` is a message when the nested command is not found
- - `you_infected` is a message when the player is infected and the timer is running

### Example settings
```yaml
# IF COMMENTS NOT VISIBLE OPEN THIS https://github.com/Kredwi/ZombiesInfection

config_version: 1

# Debug mode
debug: false 

# Potion duration in seconds (n * 20 ticks)
potion_duration: 60

# Number of times to apply the effect
from_effect: 5

# Delay in server ticks before executing the first repeat (n * 20)
delay: 5 

# Period in server ticks of the task (n * 20)
period: 5

# Enable random time range for effect application
random_time_from_range:
 enable: true
 # Range in seconds from which to pick a random time
 range_from: 5 
 # Range in seconds to which to pick a random time
 range_to: 10
 
# Messages for different events
messages:
 # Plugin reload success message
 plugin_reload: "§2The plugin has been successfully reloaded" 
 # Insufficient permissions message
 no_permission: "§cYou don't have permission" 
 # Missing subcommand message
 no_command_args: "§cUsage: /zi <subcommand>" 
 # Invalid command message
 no_found_command: "§cThe command was not found" 
 # Infection message
 you_infected: "§4§lYOU INFECTED!!"
```

## Known issues
- Optimization (an eternal problem)
- Level potion effect not work
- On some versions there are no comments in the config

## Zombie support
- An ordinary zombie
- Zombie resident
