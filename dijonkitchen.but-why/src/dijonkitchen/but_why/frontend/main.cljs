(ns ^:figwheel-hooks dijonkitchen.but-why.frontend.main
  (:require [reagent.core :as r]))

(def answer
  (r/atom nil))

(defn- answer-handler
  [event]
  (reset! answer (-> event
                     .-target
                     .-value)))

(def answers
  (r/atom []))

(defn- submit-handler
  [event]
  (.preventDefault event)
  (when-let [response @answer]
    (swap! answers conj response)
    (reset! answer nil)))

(defn- form
  []
  [:form
   [:label
    "What do you want to do?"
    [:br]
    [:input {:required true
             :auto-focus true
             :value    @answer
             :on-change answer-handler}]]
   [:input {:type     :submit
            :on-click submit-handler}]])

(defn- previous-answer
  [key answer]
  [:p {:key key}
   "What do you want to do?"
   [:br]
   answer])

(defn- previous-answers
  []
  [:<>
   (map-indexed previous-answer @answers)])

(defn- root
  []
  [:<>
   [previous-answers]
   [form]])

;; This is called once
(defonce init
  (r/render [root]
            (js/document.getElementById "app")))

;; This is called every time you make a code change
(defn ^:after-load reload []
  (r/render [root]
            (js/document.getElementById "app")))
