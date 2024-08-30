# GithubActivityTracker

## Built with
- [HTMX](https://htmx.org/)
- [Minum](https://github.com/byronka/minum)

## Run
1. Build with gradle (or download the release .jar)
```sh
$ ./gradlew jar
```

2. Run with `java -jar`
- Server
```sh
$ java -jar gat.jar # or gat.jar server
```

- CLI
```sh
$ java -jar gat.jar cli <username>
```

## Configuration
- Use the [`minum.config`](./minum.config) file (see [the minum repo](https://github.com/byronka/minum/blob/master/minum.config) for all options)