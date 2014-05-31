(ns chessground.data
  "Contains functions for manipulating and persisting the application data"
  (:refer-clojure :exclude [filter])
  (:require [cljs.core.async :as a]
            [chessground.drag :as drag]
            [chessground.common :as common :refer [pp square-key]]
            [chessground.chess :as chess]))

(def defaults
  "Default state, overridable by user configuration"
  {:fen nil
   :orientation :white
   :movable {:enabled :both ; :white | :black | :both | nil
             }
   :selected nil ; last clicked square. :a2 | nil
   :touch-device false
   })

(defn set-fen [state fen] (assoc state :chess (chess/make fen)))

(defn make [config] (-> (merge defaults config)
                        (set-fen (:fen config))
                        (dissoc :fen)))

(defn drag-start [state _]
  (dissoc state :selected))

(defn drag-end [state args]
  (or (when-let [[orig dest] (drag/end args)]
        (when-let [new-chess (chess/move-piece (:chess state) orig dest)]
          (assoc state :chess new-chess)))
      state))

(defn select-square [state key]
  (or (when-let [from (:selected state)]
        (when-let [new-chess (chess/move-piece (:chess state) from key)]
          (-> state
              (assoc :chess new-chess)
              (assoc :selected nil))))
      (assoc state :selected key)))

(defn clear [state] (set-fen state nil))

(defn set-orientation [state orientation-str]
  (let [orientation (keyword orientation-str)]
    (if (common/set-contains? chess/colors orientation)
      (assoc state :orientation orientation)
      state)))

(defn toggle-orientation [state]
  (set-orientation state (if (= (:orientation state) :white) :black :white)))