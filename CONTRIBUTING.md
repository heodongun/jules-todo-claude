# Contributing to Board Application

Thank you for considering contributing to this project! This guide will help you understand our workflow and best practices.

## Git Flow Workflow

We follow the Git Flow branching model:

### Branch Types

- **master**: Production-ready code
- **develop**: Integration branch for features (not used in this project, we merge directly to master)
- **feature/**: New features and enhancements
- **bugfix/**: Bug fixes
- **hotfix/**: Urgent production fixes

### Branch Naming Convention

```
feature/issue-{number}-short-description
bugfix/issue-{number}-short-description
hotfix/issue-{number}-short-description
```

Examples:
- `feature/issue-9-search-functionality`
- `bugfix/issue-20-pagination-error`
- `hotfix/issue-25-security-patch`

## Development Workflow

### 1. Pick an Issue

Browse open issues and find one that interests you. Issues labeled `jules-ready` are ready for implementation.

### 2. Create a Branch

```bash
git checkout -b feature/issue-{number}-description
```

### 3. Implement the Feature

- Follow the acceptance criteria in the issue
- Write clean, maintainable code
- Add comprehensive tests
- Update documentation

### 4. Test Your Changes

Backend:
```bash
cd backend
./gradlew test
```

Frontend:
```bash
cd frontend
npm test
```

Full stack with Docker:
```bash
docker-compose up -d
# Test manually or run integration tests
```

### 5. Commit Your Changes

Follow conventional commit messages:

```
feat: add search functionality to posts
fix: resolve pagination offset calculation
test: add integration tests for post API
docs: update API documentation
refactor: simplify database query logic
perf: optimize post list query
style: format code according to guidelines
chore: update dependencies
```

### 6. Push and Create PR

```bash
git push origin feature/issue-{number}-description
gh pr create --title "feat: Add search functionality" --body "Closes #9"
```

## Code Standards

### Backend (Kotlin)

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Keep functions small and focused
- Use coroutines for async operations
- Handle errors gracefully

### Frontend (TypeScript)

- Use TypeScript for type safety
- Follow React best practices
- Use functional components with hooks
- Keep components small and reusable
- Handle loading and error states
- Use meaningful prop names

### Testing

- Aim for >80% code coverage
- Test happy paths and edge cases
- Mock external dependencies
- Use descriptive test names
- Test user workflows end-to-end

## Pull Request Process

1. Ensure all tests pass
2. Update documentation
3. Fill out the PR template completely
4. Request review from maintainers
5. Address review comments
6. Squash commits if needed
7. Merge after approval

## Jules Automation

Issues labeled `jules-assigned` are being worked on by our automation system. The workflow:

1. Issue is created or labeled
2. GitHub Actions creates a feature branch
3. Jules implements the feature
4. PR is created for review
5. After approval, branch is merged

## Getting Help

- Check existing issues and PRs
- Read the documentation
- Ask questions in issue comments
- Reach out to maintainers

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Help others learn and grow
- Follow project guidelines

Thank you for contributing!
