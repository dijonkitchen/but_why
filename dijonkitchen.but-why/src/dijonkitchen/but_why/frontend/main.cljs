(ns ^:figwheel-hooks dijonkitchen.but-why.frontend.main
  (:require [reagent.core :as r]))

(def answer
  (r/atom nil))

(def answers
  (r/atom []))

(defn- answer-handler
  [event]
  (reset! answer (-> event
                     .-target
                     .-value)))

(defn- submit-handler
  [event]
  (.preventDefault event)
  (when-let [response @answer]
    (swap! answers conj response)
    (reset! answer nil)))

(def initial-question
  "What do you want to do?")

(def final-question
  "But why?")

(defn- label
  ([]
   (label 0))
  ([label-number]
   (if (zero? label-number)
     initial-question
     final-question)))

(defn- form
  []
  [:form
   [:label
    [label (count @answers)]
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
   [label key]
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
