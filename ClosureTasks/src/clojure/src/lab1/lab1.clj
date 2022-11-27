(defn gen-strings [alphabet n]
  ; remove all same characters
  (let [alphabet (map str alphabet)]
    (if (= n 1)
      alphabet
      (let [prev (gen-strings alphabet (- n 1))]
        (reduce (fn [acc x]
                  (reduce (fn [acc y]
                            (if (not= x (last y))
                              (conj acc (str x y))
                              acc))
                          acc
                          prev))
                '()
                alphabet)))))

; define main function
(def n 2)
(def alphabet "abc")

(defn main []
  (println
    (gen-strings alphabet n)
    (count (gen-strings alphabet n))
    )
  )

(main)