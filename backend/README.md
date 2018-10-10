# metr backend

## Quick start

### Run the server

```
% lein repl
[...]
metr.core=> (def _s start!) ;; this will import the data and could take some time
nil
```

It will bind at `0.0.0.0:8080`.

```
metr.core=> (stop! _s)
nil
```

Will stop the server and delete the database.

## GTFS Feed

Data taken from [SmartMe][SmartMe-GTFS].

[SmartMe-GTFS]: http://smartme-data.unime.it/dataset/dati-del-trasporto-pubblico-urbano-atm-in-formato-gtfs
