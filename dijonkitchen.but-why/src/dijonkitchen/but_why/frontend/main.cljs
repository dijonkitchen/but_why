(ns ^:figwheel-hooks dijonkitchen.but-why.frontend.main
  (:require [reagent.core :as r]))

(def reason
  (r/atom nil))

(defn- reason-handler
  [event]
  (reset! reason (-> event
                     .-target
                     .-value)))

(def reasons
  (r/atom []))

(defn- submit-handler
  [event]
  (.preventDefault event)
  (when-let [why @reason]
    (swap! reasons conj why)
    (reset! reason nil)))

(defn- form
  []
  [:form
   [:label {:for :reason-why}
    "What do you want to do?"
    [:br]
    [:input {:id       :reason-why
             :required true
             :name     :reason
             :value    @reason
             :on-change reason-handler}]]
   [:input {:type     :submit
            :on-click submit-handler}]])

(defn- root
  []
  [form])

;; This is called once
(defonce init
  (r/render [root]
            (js/document.getElementById "app")))

;; This is called every time you make a code change
(defn ^:after-load reload []
  (r/render [root]
            (js/document.getElementById "app")))
