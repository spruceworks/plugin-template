# spruceworks-plugin-template

Reusable starting point for Paper plugins. Targets **Paper 26.2**, **Java 25**,
**Gradle Kotlin DSL**.

## What's inside

- `build.gradle.kts` — paper-api `26.2.build.+`, Java 25 toolchain, shadowJar
  with bStats relocated into `dev.spruceworks.template.libs.bstats`.
- [run-paper](https://github.com/jpenilla/run-task) — `./gradlew runServer`
  downloads Paper 26.2 and boots a local test server with the plugin installed.
- `ConfigManager` + `Messages` — `config.yml` and `messages.yml` copied on
  first run, reloadable at runtime via `/papertemplate reload`. All
  player-facing text is MiniMessage in `messages.yml`; `<prefix>` works in
  every message.
- `SchedulerAdapter` — the one place that touches the Bukkit scheduler, so a
  Folia port only changes one class.
- `.github/workflows/build.yml` — builds on every push, uploads the plugin jar
  as an artifact.

## Requirements

- JDK 25 on your `PATH` (or any JDK 17+ to launch Gradle — the build
  auto-provisions a Java 25 toolchain via the foojay resolver).

## Usage

```console
./gradlew build        # plugin jar → build/libs/<name>-<version>.jar (shaded)
./gradlew runServer    # boot a local Paper 26.2 test server with the plugin
```

The first `runServer` stops and asks you to accept the Minecraft EULA: open
`run/eula.txt`, set `eula=true`, run again. The `run/` directory (server files,
worlds) is disposable and gitignored.

## Using the template for a new plugin

1. `settings.gradle.kts` — change `rootProject.name`.
2. `build.gradle.kts` — change `group`, `description`, and the `relocate`
   target package.
3. Rename the `dev.spruceworks.template` package.
4. `plugin.yml` — change `name`, `main`, and the permission nodes.
5. `TemplateCommand` — rename the `/papertemplate` literal.
6. `TemplatePlugin.BSTATS_SERVICE_ID` — register the plugin at
   [bstats.org](https://bstats.org) and set the real id.
7. Reset this README and `CHANGELOG.md`.

## Release checklist

1. Bump `version` in `build.gradle.kts` (semantic versioning).
2. Update `CHANGELOG.md`.
3. `./gradlew build`, then boot the jar on the latest Paper build and check the
   console for warnings or errors.
