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

3. View the web UI
- Visit http://localhost:8000 or https://localhost:8443 in your browser (or whatever ports you configured if you changed them)

## Configuration
- Use the [`minum.config`](./minum.config) file (see [the minum repo](https://github.com/byronka/minum/blob/master/minum.config) for all options)