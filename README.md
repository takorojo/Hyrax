# Hyrax

A simple Minecraft world backup manager for Bukkit/Paper servers.

## Development Dependencies

* Java 17 (Eclipse Temurin 17)

### Minecraft Plugin Dependencies

#### Required for basic functionality

* [Multiverse-Core](https://github.com/Multiverse/Multiverse-Core)

#### Required for full functionality

* A permission manager, such
  as [LuckPerms](https://github.com/LuckPerms/LuckPerms)

## Installation

Download the latest release and install in your server's `plugins` folder.

## Usage

Assuming you have the required permissions, you'll have access to the
following two commands:

* `backup-world`
* `restore-world`

### `backup-world`

Backs up the world you're currently in.  Takes no arguments.

The world will be backed up to a directory called `backups` in your server's
root directory as a timestamped zip file where the date is in the format
`yyyyMMddHHmmss`.  For example, if you ran the command on March 4, 2023 at
8:21:33 PM on a world named "jungle", the resulting file will be stored at
`SERVER_ROOT/backups/jungle_20230304202133.zip`.

### `restore-world`

Restores the latest backup of the world you're currently in.  Takes no
arguments.

All users in this world will be kicked off the server, the backup will be
restored from the zip file with the most recent timestamp, and **THE SERVER
WILL BE SHUT DOWN**.  You **MUST** restart the server again.
