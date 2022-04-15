# Battleships

## Table of contents
* [About](#about)
* [Features](#features)
* [Usage](#usage)
* [Installation](#installation)
* [Technologies used](#technologies-used)
* [Screenshots](#screenshots)


## About

Battleships is a classic strategy type guessing game for two players.
It is played on grids on which each player's fleet of warships are placed.
The locations of the fleets are concealed from the other player.
Players alternately choose the coordinates of the ships, thus "shooting" at the ships of the other player's fleet.
The objective of the game is to destroy the opposing player's fleet.

## Features

- Play battleships with someone on the same PC
- Play battleships against medium difficulty AI

## Usage

To play this game just run it with console and then you will be guided through every step.
First players will be asked to place their ships (AI will just inform you that it has finished)
and then, turn after turn, you will be asked for coordinates of the next shots.

## Installation

1. Import this repository to some folder with `git clone https://github.com/adrian-jrczk/Battleships.git`
2. Open this folder and install with `mvn clean install`
3. In `target` folder there will be executable jar file `battleships.jar` which you can move freely and run with `java -jar battleships.jar`

## Technologies used

- Java 17

## Screenshots

![screenshot 1](images/screenshot01.png?raw=true "The beginning of the game")
***
![screenshot 2](images/screenshot02.png?raw=true "Taking shots")
***
![screenshot 3](images/screenshot03.png?raw=true "Taking shots 2")
***
![screenshot 4](images/screenshot04.png?raw=true "Passing move to second human player")
***
![screenshot 5](images/screenshot05.png?raw=true "Second human player turn start")
