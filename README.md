# PocketFurnace
PocketFurnace is a SpigotMC plugin that allows players to open a pocketfurnace using the command '/pf #'. It is currently limited from 1-4. Players will also need the permission to use the command from 1-4.

It works by preallocating a chunk of furnaces at the bottom of the spawn chunk, to make sure the furnaces stay loaded, and assigning furnaces to players on use. The furnaces are accessed remotely. Player assigned furnaces are saved in the data file.
