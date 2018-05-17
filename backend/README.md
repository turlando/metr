# metr backend

## Quick start

### Run the server

```
% lein repl
[...]
metr.core=> (start!) ;; this will import the data and could take some time
nil
```

It will bind at `0.0.0.0:8080`.

## Hacking

```
metr.core=> (require '[metr.db :as db])
nil
metr.core=> (db/query-routes-by-stop-id db/db 390)
({:name "PONTE GALLO - CAVALLOTTI"})
```
## GTFS Feed

Data taken from [SmartMe][SmartMe-GTFS].

[SmartMe-GTFS]: http://smartme-data.unime.it/dataset/dati-del-trasporto-pubblico-urbano-atm-in-formato-gtfs
