# Contributing

Thank you for your interest in contributing to @obsidian_north/react-native-mediastore!

## Code of Conduct

Please be respectful and constructive. We aim to foster an inclusive and welcoming community.

## Getting Started

1. Fork the repository
2. Clone your fork
3. Install dependencies: `npm install`
4. Build the project: `npm run build`

## Coding Style

### Kotlin (Android)

- Follow [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)
- Use 4-space indentation
- Max line length: 120 characters
- Single responsibility per class
- Favor composition over inheritance

### TypeScript

- Follow the existing code style
- Use 2-space indentation
- Use explicit types — no `any`
- Use `const` and `let` instead of `var`
- Prefer interfaces over type aliases for object shapes
- Async functions should return `Promise<T>`, not `void`

## Branch Naming

- `feature/description` — new functionality
- `fix/description` — bug fixes
- `chore/description` — maintenance, tooling, CI
- `docs/description` — documentation changes

Examples:
- `feature/add-thumbnail-support`
- `fix/cursor-leak-on-error`
- `chore/update-gradle-version`

## Commit Format

```
<type>: <description>
```

Types:
- `feat` — new feature
- `fix` — bug fix
- `docs` — documentation
- `chore` — maintenance
- `refactor` — code restructuring
- `perf` — performance improvement
- `test` — testing

Examples:
- `feat: add getLibrary batch query`
- `fix: close cursor on permission denied`
- `docs: update API reference with thumbnail methods`

## Pull Request Process

1. Create a feature branch from `main`
2. Make your changes
3. Ensure the project builds: `npm run build`
4. Write or update tests as needed
5. Open a PR against `main`
6. Describe your changes clearly in the PR description
7. Squash merge after approval

## Testing

- Unit tests should cover cursor mapping, MIME detection, sorting, filtering, search, and pagination logic
- Integration tests should validate behavior across supported Android versions

## Questions

Open a GitHub issue for questions, feature requests, or bug reports.
