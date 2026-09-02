# 🧍‍♂️ Ridable Players - Scarpet

[![Minecraft](https://img.shields.io/badge/Minecraft-1.20+-green?logo=minecraft)](https://www.minecraft.net/)
[![Carpet](https://img.shields.io/badge/Carpet-Script-green?logo=github)](https://github.com/gnembon/fabric-carpet)
[![License](https://img.shields.io/github/license/Tarquitet/Ridable-Players-Scarpet?color=blue)](LICENSE)
[![Release](https://img.shields.io/github/v/release/Tarquitet/Ridable-Players-Scarpet?color=orange)](https://github.com/Tarquitet/Ridable-Players-Scarpet/releases)
[![Downloads](https://img.shields.io/github/downloads/Tarquitet/Ridable-Players-Scarpet/total?color=yellow)](https://github.com/Tarquitet/Ridable-Players-Scarpet/releases)

A lightweight, vanilla-friendly Scarpet script that allows players to ride on top of other players in vertical stacks. Inspired by **FSit** and **FSit Continued**, but built entirely with Carpet's Scarpet API.

---

## ✨ Features

| Feature | Description |
| :--- | :--- |
| 🏗️ **Vertical Stacking** | Right-click to mount. Multiple players can stack safely up to a defined limit. |
| 🎯 **Smart Following** | The entire stack dynamically follows the root player with precise positional updates. |
| ⬇️ **Shift to Dismount** | Press `Shift` to safely get off the stack and return to the ground. |
| 🔄 **Auto-Reorganization** | If the root player shifts or disconnects, the first rider automatically becomes the new root. |
| 🛡️ **Crash Protection** | Built-in anti-cycle detection prevents mounting your own stack, avoiding infinite loops. |
| 🧹 **Auto-Cleanup** | Detects and removes orphaned armor stands every 10 seconds to keep the world clean. |
| 🌍 **100% Vanilla-Friendly** | Zero client-side mods required. Works seamlessly on any server running the Carpet mod. |

---

## 📸 Showcase

<div align="center">
  <img width="685" alt="Ridable Players Early Screenshot" src="https://github.com/user-attachments/assets/253b32e1-2238-4cd0-98d7-6baa4c10dda6" style="border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.2);"/>
  <p><em>Early concept screenshot showcasing the core stacking mechanic.</em></p>
</div>

<br>

<div align="center">
  <a href="https://youtu.be/etgxicrdKrk" target="_blank">
    <img src="https://img.youtube.com/vi/etgxicrdKrk/maxresdefault.jpg" alt="Ridable Players Demo" width="600" style="border-radius: 12px; box-shadow: 0 8px 16px rgba(0,0,0,0.3); transition: transform 0.2s;">
  </a>
  <p><em>Watch the script in action (early development version). Current release includes major stability improvements.</em></p>
</div>

---

## 📋 Requirements & Installation

| Requirement | Detail |
| :--- | :--- |
| **Minecraft** | 1.20 or higher |
| **Mod Loader** | Fabric |
| **Dependency** | [Carpet Mod](https://www.curseforge.com/minecraft/mc-mods/carpet) (Scarpet API enabled) |

**Installation Steps:**
1. Download the latest `ride-players.sc` from [Releases](https://github.com/Tarquitet/Ridable-Players-Scarpet/releases).
2. Place it in your world's scripts folder: `.minecraft/saves/<your_world>/scripts/`
3. Load it in-game: `/script load ride-players`

---

## 🎮 How to Use

| Action | Result |
| :--- | :--- |
| **Right-click a player** (empty hand) | You mount on top of them. |
| **Right-click a player in a stack** | You mount at the very top of their stack. |
| **Press `Shift`** (while riding) | You dismount and fall to the ground. |
| **Root player presses `Shift`** | The stack reorganizes: the first rider becomes the new root. |

> 💡 **Pro Tip:** Use `/script in ride-players run clear_all()` to forcefully remove all seats and reset the system if needed.

---

## ⚠️ Limitations & Trade-offs

| Limitation | Technical Explanation |
| :--- | :--- |
| 🌀 **Teleportation** | Long-distance teleports (Ender Pearl, Chorus) may temporarily dismount riders due to vanilla entity mechanics. |
| 🌍 **Dimension Changes** | Crossing portals reorganizes the stack in the original dimension (riders don't teleport with the root). This is an intentional, safe fallback. |
| 📏 **Height Limit** | Stack tracking is safely capped at 20 players to prevent performance degradation. |
| 👁️ **Visual Jitter** | A slight 1-2 tick delay is normal. Scarpet cannot access client-side interpolation or quaternions. **Per-tick teleportation** was chosen as the most reliable method over velocity-based movement, which causes physics glitches and overshoot. |

---

<details>
<summary>💡 <strong>The Vision: Why build this instead of just using the FSit mod?</strong></summary>
<br>
<p>Born from a technical challenge on the <strong>Builtechraft</strong> server, this project pushes the limits of pure vanilla Minecraft. While mods easily handle player riding via client-side hooks, Scarpet must fight vanilla engine limitations (like forced dismounts and lack of smooth movement APIs).</p>
<p>By utilizing efficient linked lists, crash-proof cycle detection, and smart fallbacks, this script delivers a "mod-like" experience with <strong>zero client downloads</strong>. It proves that deep knowledge of game mechanics can replicate complex features while keeping Minecraft's soul 100% intact.</p>
</details>

<br>

## 🛠️ Technical Details

| Component | Implementation |
| :--- | :--- |
| 🧠 **Data Structure** | Linked List (`global_child` / `global_parent` maps) for O(1) stack operations. |
| 🛡️ **Cycle Detection** | `_find_root()` has a hard 64-iteration limit to guarantee no infinite loops. |
| 🧹 **Orphan Sweeping** | `_sweep_orphans()` runs every 200 ticks (10s) to clean up lost armor stands. |

---

## 🗺️ Roadmap (TO-DO)

| Status | Goal |
| :---: | :--- |
| 🚧 | Port core logic to a pure Datapack for maximum vanilla compatibility (no Carpet required). |
| 💡 | Replace the Scarpet right-click trigger with a vanilla item interaction (e.g., Fishing Rod or Carrot on a Stick). |

---

## 📜 License & Credits

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

- **Inspired by:** [FSit](https://modrinth.com/project/fsit) and [FSit Continued](https://modrinth.com/
project/fsit-continued)
- **Built with:** [Carpet Mod](https://github.com/gnembon/fabric-carpet) Scarpet API
- **Created by:** [@Tarquitet](https://github.com/Tarquitet)

## 🐛 Issues & Contributions

- Found a bug? Open an issue on the [GitHub Issues](https://github.com/Tarquitet/Ridable-Players-Scarpet/issues) page!
- Contributions and Pull Requests are always welcome. Please read [CONTRIBUTING.md](CONTRIBUTING.md) before submitting.

---
*Made with ❤️ for the Minecraft community.*
