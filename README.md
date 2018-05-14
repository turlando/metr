# metr

## Example

```
% lein repl
[...]
metr.core=> (require '[metr.state :as state])
nil
metr.core=> (require '[metr.db :as db])
nil
metr.core=> (-main) ;; this will import the data and could take some time
nil
metr.core=> (db/query-nearby-stops state/db 38.19185 15.55401 3)
({:id 390, :code 511, :name "Corso Cavour, Ist. Maurolico", :latitude 38.1918, :longitude 15.5531}
 {:id 1510, :code 1510, :name "Corso Cavour, Ist. Maurolico", :latitude 38.192, :longitude 15.5531}
 {:id 391, :code 512, :name "Corso Cavour, p.zza Antonello", :latitude 38.1941, :longitude 15.5544})
metr.core=> (db/query-routes-by-stop-id state/db 390)
({:name "PONTE GALLO - CAVALLOTTI"})
```

## GTFS Feed

Data taken from [SmartMe][SmartMe-GTFS].

[SmartMe-GTFS]: http://smartme-data.unime.it/dataset/dati-del-trasporto-pubblico-urbano-atm-in-formato-gtfs
