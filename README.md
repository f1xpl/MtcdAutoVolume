# MtcdAutoVolume
Adjust the volume level to speed automatically.

## Install, configure and enjoy

* **No root access required**
* **No xposed framework needed**
* **Automatic startup**
* Volume level is set in percentage in range <0, 100>%.
* Speed range is <0, 139> Kph or <0, 86> Mph.
* Generation of volume level in linear manner.
* Keep music playing while configuring MtcdAutoVolume. Sliding on seekbar will change volume level what gives you preview of volume level settings.
* Volume level will not be dropped below volume level of the system

## Disable or enable automatic volume adjustment with MtcdTools

Create Broadcast Intent action with following parameters:

> Intent action: com.microntek.f1x.mtcdautovolume.toggle

Then bind it to the desired keys sequence. Toast message with current status of automatic volume adjustment will be displayed.