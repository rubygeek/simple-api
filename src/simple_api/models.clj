(ns simple-api.models
  (:require [schema.core :as s]
            [ring.swagger.schema :refer [coerce!]]))

(def Source {:name s/Str
             :url  s/Str})

(def Recipe {:id Long
             :url String
             :name String
             :source Source })

(def NewRecipe (dissoc Recipe :id))

(defonce id-seq (atom 0))
(defonce recipes (atom (array-map)))


(defn get-recipe [id] (@recipes id))
(defn get-recipes [] (-> recipes deref vals reverse))
(defn delete! [id] (swap! recipes dissoc id) nil)

(defn add! [new-recipe]
  (let [id (swap! id-seq inc)
        recipe (coerce! Recipe (assoc new-recipe :id id))]
    (swap! recipes assoc id recipe)
    recipe))

(defn update! [recipe]
  (let [recipe (coerce! Recipe recipe)]
    (swap! recipes assoc (:id recipe) recipe)
    (get-recipe (:id recipe))))
