; function to check the sequence of the words
(defn check_sequence [word letter]
  (not (.endsWith word letter)))

; function to filter the generated words
(defn filter_word [word alphabet]
  (filter (fn [letter]
            (check_sequence word letter))
          alphabet))

; add a letter to the end of the word
; only if the letter is not in the end of the word
(defn generate_iter [alphabet, words]
  (reduce concat ()
          (map (fn [word]
                 (map (fn [letter] (.concat word letter))
                      (filter_word word alphabet)))
                words)
          )
  )

(defn generate [alphabet n]
  (reduce (fn [words _] (generate_iter alphabet words)) alphabet (range (dec n))))

(def alphabet ["a" "b" "c"])
(def n 12)

(defn main []
  (println (generate alphabet n))
  (println (count (generate alphabet n)))
  )

(main)