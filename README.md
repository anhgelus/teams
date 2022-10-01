 
# Teams

A simple mod extending vanilla teams with a management GUI and a HUD showing teammate health / hunger / position
Team Management GUI

Port of [Teams](https://github.com/t2pellet/Teams) by t2pellet for 1.18 to 1.19

## Features
* Commands for creating, removing teams, inviting players, kicking players, listing teams, listing players in team, leaving a team
* GUI for creating a team, leaving a team, requesting to join a team, kicking a player, favouriting a player, etc.
* HUD showing teammate health and hunger
* HUD showing teammates relative position to you
* Toasts for different team events (joining, leaving)
* Advancement sync

## GUI

<details>
  <summary>Click to expand!</summary>
  
  ### Team Creation

  ![Team Creation](https://s10.gifyu.com/images/Minecraft_-1.18.1---Multiplayer-3rd-party-Server-2022-01-06-10-42-07.gif)

  ### Leaving Team

  ![Leaving Team](https://s10.gifyu.com/images/Minecraft_-1.18.1---Multiplayer-LAN-2022-01-06-12-17-04.gif)

  ### Inviting Player

  ![Inviting Player](https://s10.gifyu.com/images/Minecraft_-1.18.1---Multiplayer-3rd-party-Server-2022-01-06-10-50-47.gif)

  ### Requesting to Join a Team

  ![Requesting Team](https://s10.gifyu.com/images/Minecraft_-1.18.1---Multiplayer-3rd-party-Server-2022-01-06-10-47-15.gif)

  ### Accepting an Invite

  ![Accepting Invite](https://s10.gifyu.com/images/Minecraft_-1.18.1---Multiplayer-LAN-2022-01-06-12-14-42_Trim.gif)

  ### Kicking a Player

  ![Kicking Player](https://s10.gifyu.com/images/Minecraft_-1.18.1---Multiplayer-LAN-2022-01-06-12-16-41.gif)
</details>
 
## HUD

There's a compass HUD showing location of nearby teammates, and a status HUD that live updates their health / hunger.

![output(1)](https://user-images.githubusercontent.com/4323034/149008339-9c81f6be-df58-4237-b0ce-c7305fef46e4.gif)


## Commands

* Uses /teams prefix. Commands exist to add a team, remove a team (must be OP), invite players, kick players (must be oldest in team or OP), list teams, list players in team.
* Teams created by this mod have a corresponding scoreboard team, where you can set all the parameters you normally would be able to with a scoreboard team.

 
## Config

Uses Cloth Config API to provide the following options:

![Options](https://i.imgur.com/2ubwufi.png)

The first options are all setting defaults for teams.
