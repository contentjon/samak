(ns samak.tree.system
  (:require [clojure.zip :as z]
            [revent.core :as rv]))

(defn tree-zip [root]
  (z/zipper :children
            (comp seq :children)
            (fn [node children]
              (assoc node :children (vec children)))
            root))

(defn nil-proof-handler [move]
  (let [maybe-move (fn [loc]
                     (or (move loc) loc))]
    (fn [system]
      (swap! (:state system) maybe-move))))

(def tree-mov-handlers
  (->> [z/up z/down z/left z/right]
       (map nil-proof-handler)
       (zipmap [:up :down :left :right])))

(defn z-update [loc f & args]
  (z/replace loc
             (apply f (z/node loc) args)))

(defn maybe-children [loc]
  (when (z/branch? loc)
    (z/children loc)))

(defn spit [loc]
  (let [n (count (maybe-children loc))]
    (if (zero? n)
      loc
      (let [right (-> loc z/down z/rightmost)
            node (z/node right)
            move (if (> n 1)
                   z/up
                   identity)]
        (-> right
            z/remove
            move
            (z/insert-right node))))))

(defn slurp [loc]
  (let [right (z/right loc)
        move (if (empty? (maybe-children loc))
               identity
               z/up)]
    (if (and right (z/branch? loc))
      (-> right
          z/remove
          move
          (z/append-child (z/node right)))
      loc)))

(defn select-and-root [loc & _]
  (-> @loc
      (z-update assoc :selected? true)
      z/root))

(defn make-tree-system [root]
  (rv/make-system :init-state (tree-zip root)
                  :handlers tree-mov-handlers
                  :queries {:root select-and-root}))
