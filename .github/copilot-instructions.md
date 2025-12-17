# GitHub Copilot Instructions for music-extractor

## Project Overview

This is a music extraction tool project. The repository follows modern development best practices and uses MIT License.

## Development Guidelines

### Code Style & Conventions

- Follow language-specific best practices and conventions
- Write clear, self-documenting code with meaningful variable and function names
- Include comments only when necessary to explain complex logic or business rules
- Prefer composition over inheritance where applicable
- Keep functions and methods focused on a single responsibility

### Testing

- Write tests for all new features and bug fixes
- Ensure tests are clear, focused, and test one thing at a time
- Follow existing test patterns and structure in the repository
- Aim for high test coverage, especially for critical functionality
- Run tests before submitting code changes

### Documentation

- Update README.md when adding new features or changing functionality
- Include docstrings/comments for public APIs and complex functions
- Document any setup requirements, dependencies, or configuration needs
- Keep documentation in sync with code changes

### Git Commit Messages

- Follow Conventional Commits specification for semantic versioning
- Use the format: `<type>(<scope>): <description>`
- Common types:
  - `feat`: A new feature (triggers minor version bump in SemVer)
  - `fix`: A bug fix (triggers patch version bump in SemVer)
  - `docs`: Documentation only changes (no version bump)
  - `style`: Code style changes (formatting, missing semicolons, etc.) (no version bump)
  - `refactor`: Code changes that neither fix bugs nor add features (no version bump)
  - `perf`: Performance improvements (no version bump)
  - `test`: Adding or updating tests (no version bump)
  - `chore`: Changes to build process or auxiliary tools (no version bump)
  - `ci`: Changes to CI configuration files and scripts (no version bump)
- Breaking changes should include `BREAKING CHANGE:` in the footer or `!` after type/scope (triggers major version bump)
- Keep the subject line under 72 characters
- Include additional details in the commit body if needed
- Examples:
  - `feat(extractor): add support for MP3 file extraction`
  - `fix(parser): handle edge case with empty metadata`
  - `docs(readme): update installation instructions`
  - `feat(api)!: change response format for extraction endpoint`

### Code Review & Pull Requests

- Create focused pull requests that address a single issue or feature
- Write clear PR descriptions explaining what changed and why
- Reference related issues using GitHub's linking syntax (#issue-number)
- Respond to review comments and update code as needed
- Ensure CI/CD checks pass before requesting review

### Security

- Never commit secrets, API keys, or sensitive data to the repository
- Use environment variables for configuration and sensitive information
- Keep dependencies up to date to avoid known vulnerabilities
- Follow security best practices for the language and framework being used

## Build & Development Workflow

When setting up build, test, and lint commands, follow these guidelines:

1. **Setup**: Document any required environment setup in README.md
2. **Dependencies**: Use standard package managers for the language
3. **Build**: Ensure builds are reproducible and document build steps
4. **Testing**: Make tests easy to run with a single command
5. **Linting**: Configure and run linters to maintain code quality

## Task Guidelines

### Suitable Tasks for Copilot

- Implementing well-defined features with clear requirements
- Writing tests for existing or new code
- Bug fixes with clear reproduction steps
- Code refactoring with specific goals
- Documentation improvements
- Adding type hints or improving code clarity

### Tasks Requiring Human Review

- Major architectural changes
- Security-critical implementations
- Changes to core business logic
- Database schema migrations
- Breaking API changes

## Project-Specific Notes

- This is a new project; patterns and conventions will evolve
- Maintain consistency with existing code when adding new features
- When in doubt, ask for clarification rather than making assumptions
- Prioritize code quality and maintainability over clever solutions
