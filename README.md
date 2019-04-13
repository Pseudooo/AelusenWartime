# AelusenWartime
AelusenWartime is a plugin that handles the notifications of Wartimes that take place on the server. When a Wartime is set to start the server-administrator is able to define n wartimes with their respective times.

## Contents
 - [Configuration](#Configuration)
 - [Commands](#Commands)

# Configuration

The configuration will be setup approximately like so:
```
timezone: "Etc/GMT+0"
wartimes:
  morning-wartime:
    wartime-start: '08:00'
    wartime-end: '09:00'
    wartime-active-text: "&a&lThis is the morning wartime!"
    bar-color: GREEN
  wartime-2:
    wartime-start: '18:00'
    wartime-end: '19:00'
    wartime-active-text: "&a&lThis is the evening wartime!"
    bar-color: RED
```
The Timezone indicates what timezone should be used for the times provided in the configuration. Any number of wartimes can be defined although they should not have any degree of overlap with one another, however they can last as long as possible. Due to the nature of the times in hand it's impossible to make the duration of a wartime several days but however wrapping midnight is allowed (Meaning 23:00 -> 01:00 is a valid wartime).

The names of the wartime can be anything that's valid within the YAMLConfiguration also.

wartime-active-text is the text that'll be shown in the boss bar throughout the duration of the wartime.

bar-color is the color of the bar, possible colors are:
 - BLUE
 - GREEN
 - PINK
 - PURPLE
 - RED
 - WHITE
 - YELLOW
 
# Commands
 
### listwartimes
 
This command lists all of the incoming wartimes and shows the time remaining in a number of units. This has a planned update of being formatted appropriately as a date.
permission: aelusen.wartime.listwartimes
 
### wartimereload
 
Reloads the configuration file.
permission: aelusen.wartime.reload
