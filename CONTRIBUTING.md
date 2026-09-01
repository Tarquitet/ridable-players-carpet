# 🤝 Contributing to Ridable Players - Scarpet

Thank you for considering contributing to **Ridable Players**! Whether you're fixing a bug, adding a feature, or improving documentation, your help is highly appreciated. 

Please take a moment to review this guide to ensure a smooth contribution process.

---

## 🐛 Reporting Bugs

If you find a bug, please open an [Issue](https://github.com/Tarquitet/Ridable-Players-Scarpet/issues) and include as much detail as possible:

- **Minecraft Version** (e.g., 1.20.1)
- **Carpet Mod Version** (e.g., 1.4.112)
- **Steps to Reproduce**: A clear, step-by-step description of how to trigger the bug.
- **Expected Behavior**: What should have happened.
- **Actual Behavior**: What actually happened.
- **Logs/Screenshots**: Any relevant console errors, Scarpet error traces, or videos/images.

---

## 💡 Suggesting Features

Have an idea to make the script better? Open an [Issue](https://github.com/Tarquitet/Ridable-Players-Scarpet/issues) with the `enhancement` label. Please explain:
- What the feature is.
- Why it would be useful to the community.
- Any potential technical challenges you foresee.

---

## 🛠️ Development Workflow

If you want to write code for this project, please follow these steps:

1. **Fork** the repository to your own GitHub account.
2. **Clone** your fork locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/Ridable-Players-Scarpet.git
   cd Ridable-Players-Scarpet
   ```
3. **Create a new branch** for your feature or bugfix:
   ```bash
   git checkout -b feature/your-amazing-feature
   # or
   git checkout -b bugfix/fix-something-specific
   ```
4. **Make your changes** to `ride-players.sc`.
5. **Test thoroughly** in a local Minecraft world (see the Testing Checklist below).
6. **Commit** your changes with a clear, descriptive message:
   ```bash
   git commit -m "feat: add anti-crash cycle detection limit"
   ```
7. **Push** to your fork:
   ```bash
   git push origin feature/your-amazing-feature
   ```
8. **Open a Pull Request** against the `main` branch of the original repository.

---

## 📜 Scarpet Code Style Guidelines

To keep the codebase clean and maintainable, please follow these conventions:

- **Naming**: Use `snake_case` for variables and functions (e.g., `global_child`, `_find_root`).
- **Private Functions**: Prefix helper/internal functions with an underscore (e.g., `_detach_by_uuid`, `_promote_first`).
- **Globals**: Clearly document any new global variables at the top of the script.
- **Comments**: Add inline comments for complex logic (e.g., cycle detection, stack promotion). Explain *why*, not just *what*.
- **Formatting**: Keep lines under ~100 characters when possible. Use consistent indentation (4 spaces).

---

## ✅ Testing Checklist

Before submitting a Pull Request, please verify that your changes do not break existing functionality. Test the following scenarios:

- [ ] Mounting on a player with an empty hand.
- [ ] Stacking multiple players (3+ players).
- [ ] Dismounting by pressing `Shift` (rider).
- [ ] Stack reorganization when the root player presses `Shift`.
- [ ] Player disconnection (both root and rider).
- [ ] Dimension changes (Nether/End portals).
- [ ] Anti-crash protection (trying to click on someone in your own stack).
- [ ] Running `/script in ride-players run clear_all()` cleans up properly.

---

## 📜 License

By contributing to this project, you agree that your contributions will be licensed under the project's [MIT License](LICENSE).

---

Thank you for helping make **Ridable Players** better for the Minecraft community! 🚀

If you have any questions, feel free to ask or report in issues.
