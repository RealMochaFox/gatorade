# Gatorade

A NeoForge mod for Minecraft that adds Gatorade as a naturally occurring resource.

## Development

This project uses [asdf](https://asdf-vm.com/) to manage tool versions. Install the required tools with:

```sh
asdf install
```

Build the mod using the Gradle wrapper:

```sh
./gradlew build
```

## VSCode

Recommended extensions are listed in `.vscode/extensions.json` for optimal Java support.

## Package structure

Source code lives under `src/main/java/com/mochafox/gatorade`.

### Modules

- `blocks` – contains block classes such as `GatoradeBucket`
- `items` – item implementations like `SqueezeBottle`
- `fluids` – placeholder fluid classes for future expansion
