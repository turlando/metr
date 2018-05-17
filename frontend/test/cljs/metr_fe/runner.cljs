(ns metr-fe.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [metr-fe.core-test]))

(doo-tests 'metr-fe.core-test)
