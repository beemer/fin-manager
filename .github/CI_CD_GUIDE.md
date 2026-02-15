# GitHub Actions CI/CD Pipeline

This document explains the automated CI/CD pipeline for the fin-manager repository.

## Workflow Overview

The Maven CI/CD pipeline automatically runs on every pull request and push to the main branch. It ensures code quality, successful compilation, and all tests pass before code can be merged.

## What the Pipeline Does

### 1. **Build and Test Job**
- ✅ Sets up Java 11 (Temurin distribution)
- ✅ Compiles the project (`mvn clean compile`)
- ✅ Runs all unit and integration tests (`mvn test`)
- ✅ Builds the JAR package (`mvn package`)
- ✅ Uploads test reports as artifacts
- ✅ Uploads build artifacts (JAR files)

### 2. **Code Quality Job**
- ✅ Runs PMD (Programming Mistake Detector) checks
- ✅ Verifies test results

### 3. **Test Report Job**
- ✅ Publishes detailed test results
- ✅ Posts test summary comments on PRs
- ✅ Tracks test trends

## Branch Protection Rules

To ensure quality gates are enforced, follow these steps to set up branch protection:

### Step 1: Enable Branch Protection on `main`
1. Go to Repository Settings → Branches
2. Add rule for `main` branch
3. Enable "Require a pull request before merging"

### Step 2: Require Status Checks to Pass
1. In the same branch protection rule, enable "Require status checks to pass before merging"
2. Make these status checks required:
   - `build` (Build and Test job)
   - `code-quality` (Code Quality job)

### Step 3: Dismiss Stale PR Approvals
- Enable "Dismiss stale pull request approvals when new commits are pushed"

### Step 4: Configure Status Check Settings
1. Make sure "Require branches to be up to date before merging" is checked
2. Enable "Require signed commits" (optional, for extra security)

## Workflow Triggers

### On Pull Request
- Runs whenever a PR is opened or updated targeting `main` branch
- Prevents merging if any check fails

### On Push to Main
- Runs on direct pushes to main (for compliance tracking)
- Useful for monitoring merged code quality

## Test Reports

After the workflow completes:
1. Check the "Checks" tab on your PR to see real-time status
2. View detailed test results in the "Checks" → "Test Results" tab
3. Download test reports and build artifacts from the workflow run

## Artifacts Generated

### Test Reports
- Located in `target/surefire-reports/`
- Retained for 30 days
- Includes XML and text reports for all tests

### Build Artifacts
- JAR files (fin-manager.jar)
- Retained for 7 days
- Only saved on successful builds

## Viewing Results

1. **In Pull Request**: See status at the bottom of PR
2. **Actions Tab**: View full workflow logs at: 
   `https://github.com/beemer/fin-manager/actions`
3. **Workflow Run**: Click on a specific run to see detailed logs

## Example Status Checks

A successful PR will show:
```
✅ build — All checks passed
✅ code-quality — All checks passed  
✅ report — Test Report published
```

## Troubleshooting

### Build Fails
- Check the workflow logs for specific error
- Run `mvn clean compile test` locally to reproduce
- Review the "Build and Test" job logs

### Tests Fail
- Check "Test Results" in the workflow
- Download surefire reports for detailed failure info
- Fix test issues locally and push new commit

### Status Check Not Appearing
- Wait 1-2 minutes for the workflow to start
- Check if Actions are enabled in Settings → Actions
- Verify the branch protection rule is configured correctly

## Local Development

Before pushing, run these commands locally:

```bash
# Full build with tests
mvn clean test

# Just compilation
mvn clean compile

# Build package
mvn clean package

# Run specific test
mvn test -Dtest=TestClassName
```

## CI/CD Best Practices

1. **Keep tests passing**: Don't merge PRs with failing tests
2. **Run locally first**: Test locally before pushing
3. **Small commits**: Smaller PRs are easier to review and debug
4. **Descriptive messages**: Use clear commit messages for better tracking
5. **Address failures quickly**: Fix pipeline failures promptly

## Manual Workflow Inputs

For advanced usage, you can manually trigger workflows from the Actions tab with custom inputs (if configured in future updates).

## Security

- Workflows run in isolated GitHub-hosted runners
- Code is checked out securely
- No secrets are exposed in logs
- Artifacts are scanned before storage

## Future Enhancements

Potential additions to the pipeline:
- Code coverage reporting (JaCoCo)
- Static analysis (SonarQube)
- Deployment automation
- Performance benchmarking
- Container image building (Docker)
