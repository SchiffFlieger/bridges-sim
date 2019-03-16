# Bridges Game Simulator
_Bridges_  is a logic puzzle from Japan, where the player needs to connect islands with bridges.

<h1 align="center">
<a href="http://trudesk.io"><img src="https://github.com/SchiffFlieger/bridges-sim/blob/master/sample.png" width="500" /></a>
</h1>


## The Rules
The board is a square grid with an arbitrary number of islands. Each island contains a number, which determines the number of bridges this islands needs to its neighbors. The goal is to connect all islands with the specified amount of bridges. The following rules must be obeyed:
* Each island needs as many islands as the number on it determines.
* A bridge can either be horizontal or vertical.
* Bridges are not allowed to cross each other.
* A bridge is not allow to run over an island.
* Two islands can be connected by either one or two bridges.
* At the end, all islands need to be connected to a single group.

## Installing

To get a local copy just run the following commands.

```
git clone https://github.com/SchiffFlieger/bridges-sim
cd bridges-sim
mvn package
```


## Built With

* [Java 8](https://java.com/de/download/) - Programming Language
* [JavaFX](https://openjfx.io/) - User Interface
* [Maven](https://maven.apache.org/) - Dependency/Build Management 

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
