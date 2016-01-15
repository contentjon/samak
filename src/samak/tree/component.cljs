(ns samak.tree.component
  (:require [cljs.pprint :refer [pprint]]
            [goog.events :as events]
            [revent.core :as rv])
  (:import [goog.events EventType]))

(defmulti render :type)

(defn node [{:keys [selected?]
             :as node}]
  [:div.node
   (render node)])

(defmethod render :symbol [{:keys [label]}]
  [:p label])

(defmethod render :list [{:keys [children selected?]}]
  [:ul.list-group
   (map-indexed (fn [i child]
                  ^{:key i}
                  [:li.list-group-item
                   {:class (when (:selected? child) :active)}
                   (node child)])
                children)])

(defn tree-view [{:keys [selected?] :as root}]
  [:div.tree
   [:ul.list-group>li.list-group-item {:class (when selected? :active) }
    (node root)]])

(def keymap {37 :left
             38 :up
             39 :right
             40 :down})

(defn tree [system]
  (let [root (rv/query system :root)
        handle-key (fn [key-event]
                     (when-let [event (-> key-event
                                          .-keyCode
                                          keymap)]
                       (rv/send system event)))
        tree-with-handler (with-meta tree-view
                            {:component-did-mount
                             (fn [x]
                               (events/listen js/window
                                              EventType.KEYDOWN
                                              handle-key))
                             :component-will-unmount
                             (fn [x]
                               (events/unlisten js/window
                                                EventType.KEYDOWN
                                                handle-key))})]
    (fn []
      [tree-with-handler @root])))
