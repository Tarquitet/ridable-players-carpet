# Ridable Players - Scarpet

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20+-green?logo=minecraft)](https://www.minecraft.net/)
[![Carpet](https://img.shields.io/badge/Carpet-Script-green?logo=github)](https://github.com/gnembon/fabric-carpet)
[![License](https://img.shields.io/github/license/Tarquitet/Ridable-Players-Scarpet?color=blue)](LICENSE)
[![Release](https://img.shields.io/github/v/release/Tarquitet/Ridable-Players-Scarpet?color=orange)](https://github.com/Tarquitet/Ridable-Players-Scarpet/releases)
[![Downloads](https://img.shields.io/github/downloads/Tarquitet/Ridable-Players-Scarpet/total?color=yellow)](https://github.com/Tarquitet/Ridable-Players-Scarpet/releases)

A lightweight, vanilla-friendly Scarpet script that allows players to ride on top of other players in vertical stacks. Inspired by the mechanics of **FSit** and **FSit Continued** mods, but built entirely with Carpet's Scarpet API.

## 🎬 Demo

<div align="center">
  <a href="https://youtu.be/etgxicrdKrk" target="_blank">
    <img src="https://img.youtube.com/vi/etgxicrdKrk/maxresdefault.jpg" alt="Ridable Players Demo" width="600" style="border-radius: 12px; box-shadow: 0 8px 16px rgba(0,0,0,0.3); transition: transform 0.2s;">
  </a>
  <p><em>Note: This video showcases an early development version of the script. The current release includes significant improvements in stability, crash protection, and stack reorganization.</em></p>
</div>

## ✨ Features

- 🏗️ **Vertical Stacking**: Right-click another player to mount on top of them. Multiple players can stack infinitely (up to a safe limit).
- 🎯 **Smart Following**: The entire stack dynamically follows the root player (the one at the bottom) with smooth positional updates.
- ⬇️ **Shift to Dismount**: Press `Shift` to safely get off the stack.
- 🔄 **Auto-Reorganization**: If the root player shifts or disconnects, the first rider automatically becomes the new root, keeping the stack intact.
- 🛡️ **Crash Protection**: Built-in anti-cycle detection prevents players from mounting their own stack, avoiding infinite loops and server crashes.
- 🧹 **Auto-Cleanup**: Automatically detects and removes orphaned armor stands every 10 seconds to keep the world clean.
- 🌍 **Vanilla-Friendly**: No client-side mods required. Works seamlessly on any server running the Carpet mod.

## 📋 Requirements

- **Minecraft**: 1.20 or higher
- **Mod Loader**: Fabric
- **Dependency**: [Carpet Mod](https://www.curseforge.com/minecraft/mc-mods/carpet) (Scarpet API must be enabled)

## 🚀 Installation

1. Download the latest `ride-players.sc` file from the [Releases](https://github.com/Tarquitet/Ridable-Players-Scarpet/releases) page.
2. Place the file inside your world's `scripts/` folder:  
   `.minecraft/saves/<your_world>/scripts/ride-players.sc`
3. Load the script in-game by running:
   ```mcfunction
   /script load ride-players
   ```

## 🎮 How to Use

| Action | Result |
| :--- | :--- |
| **Right-click a player** (with empty hand) | You mount on top of them. |
| **Right-click a player in a stack** | You mount at the very top of their stack. |
| **Press `Shift`** (while riding) | You dismount and fall to the ground. |
| **Root player presses `Shift`** | The stack reorganizes: the first rider becomes the new root. |

### 💡 Pro Tips
- To build a tower: Player A stands still. Player B right-clicks A. Player C right-clicks B (or A).
- Use `/script in ride-players run clear_all()` if you ever need to forcefully remove all seats and reset the system.

## ⚠️ Limitations

- **Teleportation**: If the root player teleports a long distance (e.g., Ender Pearl, Chorus Fruit), Minecraft's vanilla mechanics may temporarily dismount riders. They will need to remount.
- **Dimension Changes**: When the root player changes dimensions (e.g., Nether Portal), the stack reorganizes in the original dimension (riders do not teleport with the root). This is an intentional, vanilla-friendly fallback.
- **Height Limit**: The script safely caps stack tracking at 20 players to prevent performance issues.
- **Seat Desynchronization (Visual Jitter)**: You may notice a slight visual delay or "jitter" on the armor stand seats when the root player moves, especially at high speeds or with fast turns. This is **not a bug** — it is an inherent limitation of how Minecraft's client-server architecture works:
  - The server updates the seat position via teleport (`modify pos`) each tick, and the client interpolates the movement. This creates a natural 1-2 tick delay between the root player's movement and the seat's visual update.
  - **Why not use smooth interpolation or quaternions?** Minecraft entities do not expose quaternion rotation or client-side interpolation controls through Scarpet. The only available options are:
    - **Teleport per tick** (current approach): Precise but has minor visual jitter.
    - **Velocity-based movement** (`modify velocity`): Smoother visually but causes overshoot, oscillation, and is unreliable with gravity/collisions.
    - **Native passenger mounting** (armor stand riding the player): Perfect sync but locks the seat to a fixed body offset — no control over stack height.
  - After extensive testing, **per-tick teleportation** was chosen as the best balance between precision, reliability, and performance. The jitter is minimal during normal walking and only noticeable during fast movement or elytra flight.

## 🛠️ Technical Details

For developers and server admins interested in how it works under the hood:
- **Data Structure**: Uses a highly efficient Linked List (`global_child` / `global_parent` maps) for O(1) stack operations and reorganization.
- **Cycle Detection**: The `_find_root()` function includes a hard limit of 64 iterations to guarantee no infinite loops can occur.
- **Orphan Sweeping**: The `_sweep_orphans()` function runs every 200 ticks (10 seconds) to clean up any armor stands that lost their rider unexpectedly.

## 📜 License

This project is licensed under the **MIT License**. You are free to use, modify, and distribute this script, provided you include the original copyright notice. See the [LICENSE](LICENSE) file for details.

## 🙏 Credits & Inspiration

- **Inspired by**: [FSit](https://modrinth.com/project/fsit) and [FSit Continued](https://modrinth.com/project/fsit-continued) mods.
- **Built with**: [Carpet Mod](https://github.com/gnembon/fabric-carpet) and its powerful Scarpet API.
- **Created by**: [@Tarquitet](https://github.com/Tarquitet)

## 🐛 Issues & Contributions

Found a bug or have a feature request? Please open an issue on the [GitHub Issues](https://github.com/Tarquitet/Ridable-Players-Scarpet/issues) page! (only fixed at the moment)  
Contributions and Pull Requests are always welcome. Please read [CONTRIBUTING.md](CONTRIBUTING8.md) before submitting.
Figuring out how to developing into a datapack (if someone don't want a carpet script or more vanilly friendly).

---
*Made with ❤️ for the Minecraft community.*
