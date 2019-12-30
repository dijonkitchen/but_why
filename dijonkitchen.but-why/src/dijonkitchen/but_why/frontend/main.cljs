(ns ^:figwheel-hooks dijonkitchen.but-why.frontend.main
  (:require
   [clojure.string :as string]
   [reagent.core :as r]))

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

(defn- question
  ([]
   (question 0))
  ([question-index]
   (question question-index nil))
  ([question-index answers]
   (cond
     (zero? question-index)
     "What do you want to do?"

     (and answers
          (string/includes? (nth answers
                                 (dec question-index)) "?"))
     "Why are you asking a question?"

     :else
     "But why?")))

(defn- form
  []
  (let [answers @answers
        value   @answer]
    [:form
     [:label
      [question (count answers) answers]
      [:br]
      [:input {:required   true
               :auto-focus true
               :value      value
               :on-change  answer-handler}]]
     [:input {:type     :submit
              :on-click submit-handler}]]))

(defn- previous-answer
  [key answer]
  [:p {:key key}
   [question key @answers]
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
