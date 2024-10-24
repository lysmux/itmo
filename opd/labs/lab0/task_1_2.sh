#!/usr/bin/bash

# Create root dir
mkdir lab0 && cd lab0

# Create dirs
mkdir crawdaunt7
mkdir crawdaunt7/duosion
mkdir crawdaunt7/golem
mkdir crawdaunt7/nincada

mkdir parasect2
mkdir parasect2/flaaffy
mkdir parasect2/darumaka

mkdir zorua3
mkdir zorua3/misdreavus
mkdir zorua3/cherrim

# Create files
echo "Возможности Overland=8 Jump=2 Power=4 Intelligence=4 Egg" "Warmer 0 Firestarter=0 Heater=0 Sinker=0" > camerupt4
echo "Живет" "Mountain" > conkeldurr6
echo "Развитые способности Forewarn" > crawdaunt7/slaking
echo "Возможности" "Overland=6 Surface=2 Sky=8 Jump=6 Power1=0 Intelligence=4" "Sprouter=0"> crawdaunt7/skiploom
echo "Развитые способности Overcoat" > crawdaunt7/wormadam
echo "Способности" "Torrent Thick Fat Hydration" > parasect2/seel
echo "Развитые способности" "Hydration" > parasect2/vaporeon
echo "Способности Overcharge Static Cute" "Charm"  > parasect2/pikachu
echo "Способности Swarm Hydration Sticky" "Hold" > parasect2/accelgor
echo "weight=208.6 height=59.0 atk=10 def=9" > samurottl
echo "satk=6" "sdef=8 spd=8" > zorua3/garbodor
echo "Тип покемона GRASS NONE" > zorua3/leafeon
echo "Типо покемона" "WATER NONE" > zorua3/golduck

# Set rights
chmod 004 camerupt4
chmod 622 conkeldurr6

chmod 375 crawdaunt7/

chmod u=,g=rw,o=w crawdaunt7/slaking
chmod u=,g=r,o=rw crawdaunt7/skiploom
chmod 555 crawdaunt7/duosion/
chmod 337 crawdaunt7/golem
chmod 555 crawdaunt7/nincada
chmod u=,g=,o=rw crawdaunt7/wormadam

chmod 335 parasect2/
chmod 752 parasect2/flaaffy/
chmod 640 parasect2/seel
chmod 440 parasect2/vaporeon
chmod 752 parasect2/darumaka/
chmod 046 parasect2/pikachu
chmod 044 parasect2/accelgor

chmod 400 samurottl
chmod 513 zorua3/

chmod u=rw,g=,o= zorua3/garbodor
chmod u=rx,g=x,o=wx zorua3/misdreavus/
chmod u=r,g=,o=r zorua3/leafeon
chmod u=rx,g=rwx,o=rwx zorua3/cherrim/
chmod 640 zorua3/golduck