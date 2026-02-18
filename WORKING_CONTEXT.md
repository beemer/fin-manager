# Working Context & Preferences

**Last Updated:** February 18, 2026

---

## Development Workflow

### Feature Branch & Pull Request Process

When working on features or changes:

1. **Create feature branch** from `main`:
   ```
   git checkout -b feature/descriptive-name
   ```

2. **Make changes** and commit:
   ```
   git add .
   git commit -m "descriptive commit message"
   ```

3. **Push and create PR** via GitHub CLI:
   ```
   git push origin feature/descriptive-name
   gh pr create --title "PR Title" --body "Description"
   ```

4. **Review & Merge** - User reviews PR before merging

---

## Communication Preferences

### Code Output
- ❌ **Don't print code/file contents** on chat screen
- ✅ **Generate files** in workspace directly
- ✅ **Reference files via links** (e.g., [src/Main.java](src/Main.java))
- ✅ **User reviews all changes via GitHub PR**

### Progress Updates
- Keep updates brief and factual
- Confirm completion with file references, not code excerpts
- Link to PR for review

---

## Project References

- **Architecture & Tech Stack:** See [.PROJECT_CONTEXT.md](.PROJECT_CONTEXT.md)
- **Setup & Running:** See [README.md](README.md)
- **Repository:** https://github.com/beemer/fin-manager
- **Auth:** GitHub CLI is configured (`gh auth status` shows authenticated)

---

## Build & Test Standards

Before PR submission:
- ✅ Run `mvn clean package` to verify build
- ✅ Run `mvn test` to verify all tests pass
- ✅ Frontend builds correctly
- ✅ No compiler/test errors

---

## Common Commands

| Task | Command |
|------|---------|
| Create feature branch | `git checkout -b feature/name` |
| View changes | `git status` |
| Commit changes | `git commit -m "message"` |
| Push branch | `git push origin feature/name` |
| Create PR | `gh pr create --title "Title" --body "Description"` |
| Build project | `mvn clean package` |
| Run tests | `mvn test` |
| Run server | `java -jar target/fin-manager.jar` |

---

## Standards

- **Git Commits:** Clear, descriptive messages
- **Branches:** Use `feature/`, `bugfix/`, `docs/` prefixes
- **PR Titles:** Clear and concise
- **PR Descriptions:** Explain what & why
- **Code Review:** All changes reviewed via PR before merge

---

**Following this workflow ensures clean history and full traceability of changes.**
