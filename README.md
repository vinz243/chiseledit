# chiseledit & tesa
Chisel &amp; Bits + WorldEdit = :heart:


This adds a few commands that work similar to worldedit's, but on chisel level.

## Tesa

This is a new command that tessellates every block (and chiseled block) around an axis.
This has a temporary effect  (until cleared) where every block placement will be replicated
3 times around said axis (chisel blocks rotated accordingly of course) effectively 
creating a tessellated construction in real-time, allowing you to quickly preview changes.

```
/tesa revol|clear|update -iRr
```

### `/tesa revol`

Creates a tessellation of the current WorldEdit selection using a revolution around the player position as the axis. 
Before tessellating, it will also spin the blocks in the current selection around said axis (as a one shot tesa command)

If `-i` is passed, then the selection is infinite and any block placed into the world
will be spinned around the axis. A short warning message might get displayed to avoid mistakes. 

If `-r` is passed, during the first revolution, target blocks are first destroyed then replicated. Recommended option.

### `/tesa update`

Update the active tessellator region with the current WorldEdit selection (or infinite, if `-i` is passed)

### `/tesa clear`

Clear any active tessellator.


## CRot

```
/crot y <x> <z>
```

Rotate invidiual chisel for each block (but not individual block!). Use it with `//rotate`. Just use the same params you passed and it will work seamlessly

## CFlip

```
/cflip
```
stand looking to a direction and flip it that way. see `//flip`
