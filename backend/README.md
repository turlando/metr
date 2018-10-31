# metr backend

## Dependencies

* OpenJDK 8
* [Leiningen][Lein-Home]

## Hacking

The backend is made of two components: the database connection and the
HTTP server serving the APIs through the web. The database is a mandatory
component, while the HTTP server is not.

The data is imported to the database upon startup and it is never modified
afterwards. Because of this this you can run this software entirely in memory
and this is suggested in production.

For development purposes you can run the database off your filesystem and also
choose not to import the dataset upon startup. You can specify this when
starting the backend.

### Getting a REPL

The first time you invoke this command it will download the required runtime
dependencies.

```
% cd into-this-directory
% lein repl
[...]
metr.core=>
```

### Starting and stopping the backend

Calling the function `start!` as follows, without arguments, will cause the
backend to run off memory, import the data and start the HTTP server.

```
metr.core=> (def s (start!))
```

It will also bind the components state to the variable `s`. This will allow us
to stop the backend with the `stop!` function.

```
metr.core=> (stop! s)
```

### Startup options

Calling `start!` without arguments is the same as:

```clojure
(start! {:db-path            :memory
         :start-http-server? true
         :import-data?       true})
```

If you want to store the database to the filesystem you can change the
`:db-path` argument to a path, for instance `:db-path "metr.db"`.

The other parameters are self-explanatory.

For now the server is binding to `0.0.0.0:8080` and you cannot change it.

A suggested development workflow is to start the backend once with `:db-path`
being a path and `:import-data?` being set to `true`. The following times, you
will set `:import-data?` to `false` to avoid to wait for a new data import each
time you restart the backend.

I also suggest you to define a shorthand for the database handler, as you will
find yourself using it a lot:

```clojure
(def dbh (:db-conn s))
```

### Calling the APIs

TODO

## GTFS Feed

Data taken from [SmartMe][SmartMe-GTFS].

[Lein-Home]: https://leiningen.org/
[SmartMe-GTFS]: http://smartme-data.unime.it/dataset/dati-del-trasporto-pubblico-urbano-atm-in-formato-gtfs
